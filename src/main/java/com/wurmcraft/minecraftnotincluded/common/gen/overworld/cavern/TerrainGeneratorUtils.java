package com.wurmcraft.minecraftnotincluded.common.gen.overworld.cavern;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class TerrainGeneratorUtils {

  public static void addFillerMaterial(BlockPos pos, World world, Biome biome) {
    for (int yx = 1; yx < 4; yx++) {
      if (world.getBlockState(pos.add(0, -yx, 0)).getMaterial() != Material.AIR) {
        world.setBlockState(pos.add(0, -yx, 0), biome.fillerBlock);
      }
    }
  }
}
