package com.wurmcraft.minecraftnotincluded.common.block.light;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGlowingCrystalHanging extends BlockGlowingCrystal {

  public BlockGlowingCrystalHanging() {
    super();
  }

  @Override
  public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
    return canSustainBush(world.getBlockState(pos.up()));
  }
}
