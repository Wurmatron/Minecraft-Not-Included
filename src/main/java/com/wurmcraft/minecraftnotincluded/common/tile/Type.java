package com.wurmcraft.minecraftnotincluded.common.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public enum Type {
  WATER(Blocks.WATER.getDefaultState()), LAVA(Blocks.LAVA.getDefaultState());

  private IBlockState type;

  private Type(IBlockState state) {
    this.type = state;
  }

  public IBlockState getBlock() {
    return type;
  }
}
