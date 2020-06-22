package com.wurmcraft.minecraftnotincluded.client.gui.farm;

import com.wurmcraft.minecraftnotincluded.api.Farmable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class SlotInput extends Slot {

  private static final ItemStack BONE_MEAL = new ItemStack(Items.DYE, 1, 15);
  private static final ItemStack SOIL = new ItemStack(Blocks.DIRT, 1);

  public SlotInput(IInventory inv, int index, int xPosition, int yPosition) {
    super(inv, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return canInputItem(stack, null);
  }

  public static boolean canInputItem(ItemStack stack, Farmable farm) {
    if (farm != null) {
      FluidStack fluid = getStackFluid(stack);
      return stack.isItemEqual(farm.getSoil())
          || isFertilizer(stack)
          || fluid != null && (fluid.getFluid().equals(farm.getFluid()));
    }
    return false;
  }

  public static boolean isSoil(ItemStack stack) {
    return stack.isItemEqual(SOIL);
  }

  public static boolean isFertilizer(ItemStack stack) {
    return stack.isItemEqual(BONE_MEAL);
  }

  public static boolean isFluid(ItemStack stack) {
    return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
  }

  public static FluidStack getStackFluid(ItemStack stack) {
    return FluidUtil.getFluidContained(stack);
  }

  public static int getFertilizerWorth(ItemStack stack) {
    return stack.getCount();
  }

  public static double getFertilizerEfficiency(ItemStack stack) {
    return 1.2;
  }
}
