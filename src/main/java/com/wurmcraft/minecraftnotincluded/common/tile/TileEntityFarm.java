package com.wurmcraft.minecraftnotincluded.common.tile;

import com.wurmcraft.minecraftnotincluded.api.Farmable;
import com.wurmcraft.minecraftnotincluded.api.Farmable.DropChance;
import com.wurmcraft.minecraftnotincluded.api.Farmable.TILE_TYPE;
import com.wurmcraft.minecraftnotincluded.client.gui.farm.SlotInput;
import com.wurmcraft.minecraftnotincluded.common.utils.FarmRegistry;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class TileEntityFarm extends TileEntity implements ITickable, IInventory, ISidedInventory {

  // Config
  public static final int MAX_SOIL_STORAGE = 256;
  public static final int MAX_FLUID_STORAGE = 4000; // MB
  public static final int MAX_FERTILIZER_STORAGE = 128;

  public static final int TICKS_BETWEEN_INPUT = 10;

  // Stored in NBT
  private ItemStack[] inv;
  private double soilAmount;
  private ItemStack soilType;
  private double fluidAmount;
  private String fluidType;
  private double fertilizerAmount;
  private double fertilizerEfficiency;
  private int growthTicks;

  // Dynamic / Calculated based on stored values
  private int ticksUntillPull;
  protected Farmable selectedCrop;

  public TileEntityFarm() {
    inv = new ItemStack[6];
    Arrays.fill(inv, ItemStack.EMPTY);
    soilAmount = 0;
    soilType = ItemStack.EMPTY;
    fluidAmount = 0;
    fluidType = "";
    fertilizerAmount = 0;
    fertilizerEfficiency = 0;
    growthTicks = -1;
    ticksUntillPull = TICKS_BETWEEN_INPUT;
    selectedCrop = null;
  }

  @Override
  public void update() {
    if (selectedCrop != null) {
      // Input Processing
      if (ticksUntillPull <= 0) {
        handleInput();
        ticksUntillPull = TICKS_BETWEEN_INPUT;
      } else {
        ticksUntillPull--;
      }
    } else {
      // Get selected farmable
      if (ticksUntillPull <= 0 && !getStackInSlot(1).isEmpty()) {
        selectedCrop = FarmRegistry.getFarmableFromSeed(TILE_TYPE.FARM_TILE, getStackInSlot(1));
        if (selectedCrop != null) {
          this.soilType = selectedCrop.getSoil();
          this.fluidType = selectedCrop.getFluid().getName();
          markDirty();
        }
      } else {
        ticksUntillPull--;
      }
    }
    // Crop Growth
    if (selectedCrop != null) {
      if (growthTicks == 0) { // Repeated Farming
        if (consumeForGrowth(true)) {
          if (outputCrops()) {
            consumeForGrowth(false);
            setupNextGrowth();
          }
        } else {
          growthTicks = -1;
          setupNextGrowth();
        }
      } else if (growthTicks == -1) {
        setupNextGrowth();
      } else {
        growthTicks--;
        markDirty();
      }
    }
  }

  private boolean outputCrops() {
    for (DropChance item : selectedCrop.getDropChances()) {
      if (item.chance == 1) {
        if (!addOutput(item.stack)) {
          return false;
        }
      } else if (Math.random() < item.chance) {
        if (!addOutput(item.stack)) {
          return false;
        }
      }
    }
    return true;
  }

  protected boolean addOutput(ItemStack stack) {
    for (int x = 2; x < 6; x++) {
      ItemStack slotStack = getStackInSlot(x);
      if (slotStack.isEmpty()) {
        setInventorySlotContents(x, stack);
        return true;
      } else if (stack.isItemEqual(slotStack)) {
        if (stack.getCount() + slotStack.getCount() <= slotStack.getMaxStackSize()) {
          slotStack.setCount(slotStack.getCount() + stack.getCount());
          setInventorySlotContents(x, slotStack);
          return true;
        } else {
          int amountLeftToAdd = slotStack.getMaxStackSize() - slotStack.getCount();
          slotStack.setCount(slotStack.getMaxStackSize());
          stack.setCount(amountLeftToAdd);
          return addOutput(stack);
        }
      }
    }
    return false;
  }

  private void setupNextGrowth() {
    if (consumeForGrowth(true)) {
      growthTicks = selectedCrop.secondsToGrow();
    }
  }

  private boolean consumeForGrowth(boolean simulate) {
    if (simulate) {
      return (soilAmount - (selectedCrop.getItemPerSecond() * selectedCrop.secondsToGrow())) > 0
          && (fluidAmount - (selectedCrop.getMBPerSecond() * selectedCrop.secondsToGrow())) > 0;
    } else {
      soilAmount -= (selectedCrop.getItemPerSecond() * selectedCrop.secondsToGrow());
      fluidAmount -= (selectedCrop.getMBPerSecond() * selectedCrop.secondsToGrow());
      markDirty();
      return true;
    }
  }

  protected void handleInput() {
    if (!getStackInSlot(0).isEmpty()) {
      if (getStackInSlot(0).isItemEqual(selectedCrop.getSoil())) { // Soil
        addSoil();
      } else if (SlotInput.isFertilizer(getStackInSlot(0))) { // Fertilizer
        addFertilizer();
      } else if (SlotInput.isFluid(getStackInSlot(0))
          && SlotInput.getStackFluid(getStackInSlot(0)) != null
          && SlotInput.getStackFluid(getStackInSlot(0))
              .getFluid()
              .equals(selectedCrop.getFluid())) { // Fluid
        addFluid();
      }
    }
  }

  private void addSoil() {
    ItemStack soilStack = getStackInSlot(0);
    if (soilAmount + soilStack.getCount() <= MAX_SOIL_STORAGE) {
      soilAmount += soilStack.getCount();
      setInventorySlotContents(0, ItemStack.EMPTY);
    } else {
      int amountLeftToAdd = MAX_SOIL_STORAGE - (int) soilAmount;
      soilAmount = MAX_SOIL_STORAGE;
      decrStackSize(0, amountLeftToAdd);
    }
  }

  private void addFertilizer() {
    ItemStack fertilizer = getStackInSlot(0);
    double fertilizerTier = SlotInput.getFertilizerEfficiency(fertilizer);
    if (fertilizerEfficiency == 0) {
      fertilizerEfficiency = fertilizerTier;
    }
    if (fertilizerTier == fertilizerEfficiency) {
      ItemStack fertilizerAmountOne = fertilizer.copy();
      fertilizerAmountOne.setCount(1);
      int worthPerFertilizer = SlotInput.getFertilizerWorth(fertilizerAmountOne);
      if (fertilizerAmount + (fertilizer.getCount() * worthPerFertilizer)
          <= MAX_FERTILIZER_STORAGE) {
        fertilizerAmount += (fertilizer.getCount() * worthPerFertilizer);
        setInventorySlotContents(0, ItemStack.EMPTY);
      } else {
        int amountLeftToAdd = MAX_FERTILIZER_STORAGE - (int) fertilizerAmount;
        fertilizerAmount = MAX_FERTILIZER_STORAGE;
        decrStackSize(0, amountLeftToAdd);
      }
    }
  }

  private void addFluid() {
    ItemStack fluidStack = getStackInSlot(0);
    FluidStack fluid = FluidUtil.getFluidContained(fluidStack);
    if (fluid != null) {
      if (fluidAmount + fluid.amount <= MAX_FLUID_STORAGE) {
        fluidAmount += fluid.amount;
        if (fluidStack.getItem() instanceof ItemBucket) {
          setInventorySlotContents(0, new ItemStack(Items.BUCKET, 1, 0));
        } else {
          setInventorySlotContents(0, ItemStack.EMPTY);
        }
      } else {
        int amountLeftToAdd = MAX_FLUID_STORAGE - (int) fluidAmount;
        fluidAmount = MAX_FLUID_STORAGE;
        if (fluidStack.getItem() instanceof ItemBucket) {
          setInventorySlotContents(0, new ItemStack(Items.BUCKET, 1, 0));
        } else {
          IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(fluidStack);
          if (fluidHandler != null) {
            fluidHandler.drain(amountLeftToAdd, true);
            setInventorySlotContents(0, fluidHandler.getContainer());
          }
        }
      }
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    NBTTagList itemNBT = new NBTTagList();
    for (ItemStack item : inv) {
      if (item == null) {
        item = ItemStack.EMPTY;
      }
      itemNBT.appendTag(item.writeToNBT(new NBTTagCompound()));
    }
    nbt.setTag("items", itemNBT);
    nbt.setDouble("soil", soilAmount);
    nbt.setTag("soilType", soilType.writeToNBT(new NBTTagCompound()));
    nbt.setDouble("fluid", fluidAmount);
    nbt.setString("fluidType", fluidType);
    nbt.setDouble("fertilizer", fertilizerAmount);
    nbt.setDouble("efficiency", fertilizerEfficiency);
    nbt.setInteger("growth", growthTicks);
    return nbt;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    NBTTagList itemList = nbt.getTagList("items", NBT.TAG_COMPOUND);
    for (int x = 0; x < getSizeInventory(); x++) {
      inv[x] = new ItemStack(itemList.getCompoundTagAt(x));
    }
    this.soilAmount = nbt.getDouble("soil");
    this.soilType = new ItemStack(nbt.getCompoundTag("soilType"));
    this.fluidAmount = nbt.getDouble("fluid");
    this.fluidType = nbt.getString("fluidType");
    this.fertilizerAmount = nbt.getDouble("fertilizer");
    this.fertilizerEfficiency = nbt.getDouble("efficiency");
    this.growthTicks = nbt.getInteger("growth");
  }

  @Override
  public int getSizeInventory() {
    return 6;
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack stack : inv) {
      if (!stack.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    if (inv.length > index) {
      return inv[index];
    }
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack slotStack = getStackInSlot(index);
    slotStack.setCount(slotStack.getCount() - count);
    if (slotStack.getCount() <= 0) {
      slotStack = ItemStack.EMPTY;
    }
    markDirty();
    return slotStack;
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    if (inv.length > index) {
      inv[index] = ItemStack.EMPTY;
      markDirty();
      return inv[index];
    }
    return ItemStack.EMPTY;
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    if (inv.length > index) {
      inv[index] = stack;
      markDirty();
    }
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public boolean isUsableByPlayer(EntityPlayer player) {
    return !player.isSneaking();
  }

  @Override
  public void openInventory(EntityPlayer player) {}

  @Override
  public void closeInventory(EntityPlayer player) {}

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index == 0 && selectedCrop != null) { // Input Slot
      return SlotInput.canInputItem(stack, selectedCrop);
    }
    return false;
  }

  @Override
  public int getField(int id) {
    if (id == 0) {
      return (int) (soilAmount * 100);
    } else if (id == 1) {
      return (int) (fluidAmount * 100);
    } else if (id == 2) {
      return (int) (fertilizerAmount * 100);
    } else if (id == 3) {
      return growthTicks;
    } else if (id == 4) {
      return (int) (fertilizerEfficiency * 100);
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    if (id == 0) {
      this.soilAmount = value * .01;
    } else if (id == 1) {
      this.fluidAmount = value * .01;
    } else if (id == 2) {
      this.fertilizerAmount = value * .01;
    } else if (id == 3) {
      this.growthTicks = value;
    } else if (id == 4) {
      this.fertilizerEfficiency = value * .01;
    }
  }

  @Override
  public int getFieldCount() {
    return 4;
  }

  @Override
  public void clear() {
    inv = new ItemStack[6];
  }

  @Override
  public String getName() {
    return "farm_tile";
  }

  @Override
  public boolean hasCustomName() {
    return true;
  }

  public double getSoilPercentage() {
    return (soilAmount / (float) MAX_SOIL_STORAGE);
  }

  public double getFluidPercentage() {
    return (fluidAmount / (float) MAX_FLUID_STORAGE);
  }

  public double getFertilizerPercentage() {
    return (fertilizerAmount / (float) MAX_FERTILIZER_STORAGE);
  }

  public int getMaxSoilStorage() {
    return MAX_SOIL_STORAGE;
  }

  public int getMaxFluidStorage() {
    return MAX_FLUID_STORAGE;
  }

  public int getMaxFertilizerStorage() {
    return MAX_FERTILIZER_STORAGE;
  }

  public int getSoilAmount() {
    return (int) soilAmount;
  }

  public int getFluidAmount() {
    return (int) fluidAmount;
  }

  public int getFertilizerAmount() {
    return (int) fertilizerAmount;
  }

  public int getGrowth() {
    return growthTicks;
  }

  public float getGrowthPercentage() {
    if (selectedCrop != null) {
      return (selectedCrop.secondsToGrow() - (float) growthTicks) / selectedCrop.secondsToGrow();
    }
    return 0;
  }

  public double getFertilizerEfficiency() {
    return fertilizerEfficiency;
  }

  public void emptySoil() {
    soilAmount = 0;
    soilType = ItemStack.EMPTY;
  }

  public void emptyFluid() {
    fluidAmount = 0;
    fluidType = "";
  }

  public void emptyFertilizer() {
    fertilizerAmount = 0;
    fertilizerEfficiency = 0;
  }

  public Farmable getFarmable() {
    return selectedCrop;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[0];
  }

  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return false;
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    return false;
  }
}
