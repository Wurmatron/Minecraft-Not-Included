package com.wurmcraft.minecraftnotincluded.common.gui.farm;

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
    addPlayerSlots();
    // Input Slots
    addSlotToContainer(new SlotInput(te, 0, 28, 39));
    addSlotToContainer(new SlotSeed(te, 1, 83, 39));
    // Output Slots
    addSlotToContainer(new SlotOutput(te, 2, 126, 16));
    addSlotToContainer(new SlotOutput(te, 3, 144, 16));
    addSlotToContainer(new SlotOutput(te, 4, 126, 34));
    addSlotToContainer(new SlotOutput(te, 5, 144, 34));
  }

  private void addPlayerSlots() {
    // Hotbar
    for (int x = 0; x < 9; ++x) {
      addSlotToContainer(new Slot(player.inventory, x, 8 + x * 18, 126));
    }
    // Main Inventory
    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, 8 + x * 18, 68 + y * 18));
      }
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
  }

  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.te.setField(id, data);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    return super.transferStackInSlot(playerIn, index);
  }

  @Override
  protected boolean mergeItemStack(
      ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
    return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
  }
}
