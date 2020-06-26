package com.wurmcraft.minecraftnotincluded.api;

import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;

public abstract class Farmable {

  /** name of the crop grown */
  public abstract String getName();

  /** Seed used to distinguish this farmable */
  public abstract ItemStack getSeed();

  /** How the crop is rendered within the farm-plot */
  public abstract Map<BlockPos, IBlockState> getCropForRendering();

  /** The Amount of fluid needed per second */
  public abstract int getMBPerSecond();

  /** The fluid this crop requires to grow */
  public abstract Fluid getFluid();

  /** Soil required for this crop to grow */
  public abstract ItemStack getSoil();

  /** How many of the soil's are required per second for this crop to grow, can be less then 1 */
  public abstract double getItemPerSecond();

  /** how many ticks are required for this crop to grow */
  public abstract int secondsToGrow();

  /** Gow much power is required for this crop to continue growing */
  public abstract int powerPerTick();

  /** Types of farm tiles this crop can grow within */
  public abstract TILE_TYPE[] getFarmType();

  public abstract DropChance[] getDropChances();

  public enum TILE_TYPE {
    FARM_TILE,
    HYDROPONICS,
    MULTI_BLOCK;
  }

  public static class DropChance {

    public double chance;
    public String stack;

    public DropChance(double chance, ItemStack stack) {
      this.chance = chance;
      this.stack = stack.serializeNBT().toString();
    }

    public DropChance(double chance, String stack) {
      this.chance = chance;
      this.stack = stack;
    }
  }
}
