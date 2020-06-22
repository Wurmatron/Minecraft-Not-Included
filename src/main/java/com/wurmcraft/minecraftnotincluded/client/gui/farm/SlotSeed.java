package com.wurmcraft.minecraftnotincluded.client.gui.farm;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;

public class SlotSeed extends Slot {

  public SlotSeed(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return canInputItem(stack);
  }

  public static boolean canInputItem(ItemStack stack) {
    return stack.getItem() instanceof ItemSeeds;
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {
    return 1;
  }
}
