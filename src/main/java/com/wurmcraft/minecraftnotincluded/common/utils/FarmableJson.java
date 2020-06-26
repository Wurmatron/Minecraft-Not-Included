package com.wurmcraft.minecraftnotincluded.common.utils;

import static com.wurmcraft.minecraftnotincluded.common.utils.BlockUtils.getStackFromString;
import static com.wurmcraft.minecraftnotincluded.common.utils.BlockUtils.getState;

import com.wurmcraft.minecraftnotincluded.api.Farmable;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FarmableJson extends Farmable {

  public String name;
  private String seed;
  private String renderingBlock;
  private int mbPerSecond;
  private String fluid;
  private String soil;
  private double itemPerSecond;
  private int ticksToGrow;
  private int powerPerTick;
  private TILE_TYPE[] growthTiles;
  private DropChance[] dropChance;

  public FarmableJson(
      String name,
      String seed,
      String singlePos,
      int mbPerSecond,
      String fluid,
      String soil,
      double itemPerSecond,
      int ticksToGrow,
      DropChance[] dropChance) {
    this.name = name;
    this.seed = seed;
    this.renderingBlock = singlePos;
    this.mbPerSecond = mbPerSecond;
    this.fluid = fluid;
    this.soil = soil;
    this.itemPerSecond = itemPerSecond;
    this.ticksToGrow = ticksToGrow;
    this.powerPerTick = 0;
    this.growthTiles =
        new TILE_TYPE[] {TILE_TYPE.FARM_TILE, TILE_TYPE.HYDROPONICS, TILE_TYPE.MULTI_BLOCK};
    this.dropChance = dropChance;
  }

  public FarmableJson(
      String name,
      String seed,
      String singlePos,
      int mbPerSecond,
      String fluid,
      String soil,
      double itemPerSecond,
      int ticksToGrow,
      TILE_TYPE[] tileType,
      DropChance[] dropChance) {
    this.name = name;
    this.seed = seed;
    this.renderingBlock = singlePos;
    this.mbPerSecond = mbPerSecond;
    this.fluid = fluid;
    this.soil = soil;
    this.itemPerSecond = itemPerSecond;
    this.ticksToGrow = ticksToGrow;
    this.powerPerTick = 0;
    this.growthTiles = tileType;
    this.dropChance = dropChance;
  }

  public FarmableJson(
      String name,
      ItemStack seed,
      IBlockState singlePos,
      int mbPerSecond,
      String fluid,
      ItemStack soil,
      double itemPerSecond,
      int ticksToGrow,
      DropChance[] dropChance) {
    this.name = name;
    this.seed = seed.serializeNBT().toString();
    this.renderingBlock = BlockUtils.stateToString(singlePos);
    this.mbPerSecond = mbPerSecond;
    this.fluid = fluid;
    this.soil = soil.serializeNBT().toString();
    this.itemPerSecond = itemPerSecond;
    this.ticksToGrow = ticksToGrow;
    this.growthTiles =
        new TILE_TYPE[] {TILE_TYPE.FARM_TILE, TILE_TYPE.HYDROPONICS, TILE_TYPE.MULTI_BLOCK};
    this.dropChance = dropChance;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ItemStack getSeed() {
    return getStackFromString(seed);
  }

  @Override
  public Map<BlockPos, IBlockState> getCropForRendering() {
    HashMap<BlockPos, IBlockState> map = new HashMap<>();
    IBlockState state = getState(renderingBlock);
    map.put(new BlockPos(0, 0, 0), state);
    return map;
  }

  @Override
  public int getMBPerSecond() {
    return mbPerSecond;
  }

  @Override
  public Fluid getFluid() {
    return FluidRegistry.getFluid(fluid);
  }

  @Override
  public ItemStack getSoil() {
    return getStackFromString(soil);
  }

  @Override
  public double getItemPerSecond() {
    return itemPerSecond;
  }

  @Override
  public int secondsToGrow() {
    return ticksToGrow;
  }

  @Override
  public int powerPerTick() {
    return powerPerTick;
  }

  @Override
  public TILE_TYPE[] getFarmType() {
    return growthTiles;
  }

  @Override
  public DropChance[] getDropChances() {
    return dropChance;
  }
}
