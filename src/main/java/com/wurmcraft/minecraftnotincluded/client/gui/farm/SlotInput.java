package com.wurmcraft.minecraftnotincluded.client.gui.farm;

import com.wurmcraft.minecraftnotincluded.api.Farmable;
import java.util.*;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class SlotInput extends Slot {

  public static NonNullList<ItemStack> slotItems = NonNullList.create();
  public static List<Fluid> fluids = new ArrayList<>();

  public SlotInput(IInventory inv, int index, int xPosition, int yPosition) {
    super(inv, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return canInputItem(stack, null);
  }

  public static boolean canInputItem(ItemStack stack, Farmable farm) {
    return validItem(stack) || isFluid(stack) && getStackFluid(stack) != null && validFluid(stack);
  }

  public static boolean isFluid(ItemStack stack) {
    return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
  }

  public static FluidStack getStackFluid(ItemStack stack) {
    return FluidUtil.getFluidContained(stack);
  }

  private static boolean validItem(ItemStack s) {
    for (ItemStack stack : slotItems) {
      if (stack.isItemEqual(s)) {
        return true;
      }
    }
    return false;
  }

  private static boolean validFluid(ItemStack stack) {
    FluidStack fluid = getStackFluid(stack);
    for (Fluid f : fluids) {
      if (fluid.getFluid().equals(f)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isFertilizer(ItemStack stack) {
    return stack.isItemEqual(new ItemStack(Items.DYE, 1, 15));
  }

  public static int getFertilizerWorth(ItemStack stack) {
    return stack.getCount();
  }

  public static double getFertilizerEfficiency(ItemStack stack) {
    return 1.2;
  }
}
