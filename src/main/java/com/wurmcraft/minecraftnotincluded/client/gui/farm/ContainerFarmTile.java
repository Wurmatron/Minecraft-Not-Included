package com.wurmcraft.minecraftnotincluded.client.gui.farm;

import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityFarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFarmTile extends Container {

  public TileEntityFarm te;
  private EntityPlayer player;

  // Tile NBT
  private int storedSoil;
  private int storedFluid;
  private int storedFertilizer;
  private int growth;
  private int fertilizerEfficiency;

  public ContainerFarmTile(TileEntityFarm te, EntityPlayer player) {
    this.te = te;
    this.player = player;
    // Input Slots
    addSlotToContainer(new SlotInput(te, 0, 28, 39));
    addSlotToContainer(new SlotSeed(te, 1, 83, 39));
    // Output Slots
    addSlotToContainer(new SlotOutput(te, 2, 126, 16));
    addSlotToContainer(new SlotOutput(te, 3, 144, 16));
    addSlotToContainer(new SlotOutput(te, 4, 126, 34));
    addSlotToContainer(new SlotOutput(te, 5, 144, 34));
    addPlayerSlots();
  }

  private void addPlayerSlots() {
    // Main Inventory
    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, 8 + x * 18, 68 + y * 18));
      }
    }
    // Hotbar
    for (int x = 0; x < 9; ++x) {
      addSlotToContainer(new Slot(player.inventory, x, 8 + x * 18, 126));
    }
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return te.isUsableByPlayer(player);
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (IContainerListener listener : listeners) {
      if (this.storedSoil != te.getField(0)) {
        listener.sendWindowProperty(this, 0, te.getField(0));
      }
      if (this.storedFertilizer != te.getField(1)) {
        listener.sendWindowProperty(this, 1, te.getField(1));
      }
      if (storedFluid != te.getField(2)) {
        listener.sendWindowProperty(this, 2, te.getField(2));
      }
      if (growth != te.getField(3)) {
        listener.sendWindowProperty(this, 3, te.getField(3));
      }
      if (fertilizerEfficiency != te.getField(4)) {
        listener.sendWindowProperty(this, 4, te.getField(4));
      }
    }
    this.storedSoil = te.getField(0);
    this.storedFertilizer = te.getField(1);
    this.storedFluid = te.getField(2);
    this.fertilizerEfficiency = te.getField(3);
    this.growth = te.getField(4);
  }

  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.te.setField(id, data);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int index) {
    ItemStack previous = ItemStack.EMPTY;
    Slot slot = inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack slotStack = slot.getStack();
      ItemStack stack = slotStack.copy();
      if (index == 2 || index == 3 || index == 4 || index == 5) {
        if (mergeItemStack(stack, 0, 41, true)) {
          slot.putStack(ItemStack.EMPTY);
          return ItemStack.EMPTY;
        }
        slot.onSlotChange(slotStack, stack);
      } else if (index == 0 || index == 1) {
        if (mergeItemStack(stack, 0, 41, true)) {
          slot.putStack(ItemStack.EMPTY);
          return ItemStack.EMPTY;
        }
        slot.onSlotChange(slotStack, stack);
      } else {
        if (!inventorySlots.get(1).getHasStack() && SlotSeed.canInputItem(slotStack)) {
          if (mergeItemStack(stack, 0, 1, true)) {
            slot.putStack(ItemStack.EMPTY);
            return ItemStack.EMPTY;
          }
        } else if (SlotInput.canInputItem(slotStack, te.getFarmable())) {
          if (mergeItemStack(stack, 0, 1, false)) {
            slot.putStack(ItemStack.EMPTY);
            return ItemStack.EMPTY;
          }
        }
      }
    }
    return previous;
  }

  @Override
  protected boolean mergeItemStack(
      ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
    if (reverseDirection) {
      for (int x = endIndex; x > startIndex; x--) {
        Slot currentSlot = getSlot(x);
        if (putStackInSlot(currentSlot, stack)) {
          currentSlot.putStack(stack);
          return true;
        }
      }
    } else {
      for (int x = startIndex; x < endIndex; x++) {
        Slot currentSlot = getSlot(x);
        if (putStackInSlot(currentSlot, stack)) {
          currentSlot.putStack(stack);
          return true;
        }
      }
    }
    return false;
  }

  private boolean putStackInSlot(Slot slot, ItemStack stack) {
    if (!slot.getHasStack()) return true;
    if (slot.getHasStack()
        && slot.getStack().isItemEqual(stack)
        && (slot.getStack().getCount() + stack.getCount()) <= stack.getMaxStackSize()) return true;
    return false;
  }
}
