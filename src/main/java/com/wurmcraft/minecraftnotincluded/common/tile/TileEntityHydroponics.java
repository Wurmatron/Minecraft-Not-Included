package com.wurmcraft.minecraftnotincluded.common.tile;

import com.wurmcraft.minecraftnotincluded.client.gui.farm.SlotInput;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityHydroponics extends TileEntityFarm {

  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[]{0, 2, 3, 4, 5};
  }

  @Override
  public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
    return index == 0 && SlotInput.canInputItem(stack, selectedCrop);
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    if (index >= 2) {
      return true;
    }
    return false;
  }

  @Override
  protected void handleInput() {
    super.handleInput();
    if (getStackInSlot(0).getItem() instanceof ItemBucket) {
      FluidStack bucketFluid = SlotInput.getStackFluid(getStackInSlot(0));
      if (bucketFluid == null || bucketFluid.amount == 0) {
        ItemStack bucket = getStackInSlot(0).copy();
        if (addOutput(bucket)) {
          setInventorySlotContents(0, ItemStack.EMPTY);
        }
      }
    }
  }
}
