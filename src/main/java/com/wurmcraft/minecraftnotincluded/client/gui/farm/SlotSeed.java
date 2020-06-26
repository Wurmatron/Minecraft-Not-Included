package com.wurmcraft.minecraftnotincluded.client.gui.farm;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SlotSeed extends Slot {

  public static NonNullList<ItemStack> supportedSeeds = NonNullList.create();

  public SlotSeed(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return canInputItem(stack);
  }

  public static boolean canInputItem(ItemStack stack) {
    for (ItemStack seed : supportedSeeds) if (stack.isItemEqual(seed)) return true;
    return false;
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {
    return 1;
  }
}
