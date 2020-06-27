package com.wurmcraft.minecraftnotincluded.api;

import net.minecraft.block.state.IBlockState;

public abstract class GeyserData {

  public abstract IBlockState getBlock();

  public abstract int getTicksPerBlock();

  public abstract double getFrackingEfficiency();
}
