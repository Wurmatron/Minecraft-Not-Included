package com.wurmcraft.minecraftnotincluded.common.utils;

import com.wurmcraft.minecraftnotincluded.api.GeyserData;
import net.minecraft.block.state.IBlockState;

public class GeyserDataJson extends GeyserData {

  private String block;
  private int ticksPerBlock;
  private double frackingEfficiency;

  public GeyserDataJson(String block, int ticksPerBlock, double frackingEfficiency) {
    this.block = block;
    this.ticksPerBlock = ticksPerBlock;
    this.frackingEfficiency = frackingEfficiency;
  }

  public GeyserDataJson(IBlockState block, int ticksPerBlock, double frackingEfficiency) {
    this.block = BlockUtils.stateToString(block);
    this.ticksPerBlock = ticksPerBlock;
    this.frackingEfficiency = frackingEfficiency;
  }

  @Override
  public IBlockState getBlock() {
    return BlockUtils.getState(block);
  }

  @Override
  public int getTicksPerBlock() {
    return ticksPerBlock;
  }

  @Override
  public double getFrackingEfficiency() {
    return frackingEfficiency;
  }
}
