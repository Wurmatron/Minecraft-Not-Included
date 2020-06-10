package com.wurmcraft.minecraftnotincluded.common.world.overworld;

import static com.wurmcraft.minecraftnotincluded.common.world.overworld.cavern.TerrainGeneratorUtils.addFillerMaterial;

import com.wurmcraft.minecraftnotincluded.common.world.overworld.cavern.CavernGenerator;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.core.world.cube.Cube;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class OverworldTerrainBuilder {

  private World world;
  private MNIBiomeSource biomeSource;
  private CavernGenerator cavernGenerator;

  public OverworldTerrainBuilder(World world, MNIBiomeSource biomeSource) {
    this.world = world;
    this.biomeSource = biomeSource;
    cavernGenerator = new CavernGenerator();
  }

  private CubePrimer fillWithBlock(CubePrimer cp, IBlockState state) {
    for (int x = 0; x < Cube.SIZE; x++) {
      for (int y = 0; y < Cube.SIZE; y++) {
        for (int z = 0; z < Cube.SIZE; z++) {
          cp.setBlockState(x, y, z, state);
        }
      }
    }
    return cp;
  }

  public CubePrimer createPrimer(CubePrimer primer, int cubeX, int cubeY, int cubeZ) {
    CubePrimer maskPrimer = new CubePrimer();
    fillWithBlock(maskPrimer, Blocks.DIRT.getDefaultState());
    cavernGenerator.generate(world, maskPrimer, new CubePos(cubeX, cubeY, cubeZ));
    fillWithBlock(primer, Blocks.STONE.getDefaultState());
    CubePrimer tunnel = applyTunnelFilter(maskPrimer);
    for (int x = 0; x < Cube.SIZE; x++) {
      for (int y = 0; y < Cube.SIZE; y++) {
        for (int z = 0; z < Cube.SIZE; z++) {
          IBlockState tunnelState = tunnel.getBlockState(x, y, z);
          Biome biome = primer.getBiome(x / 4, y / 4, z / 4);
          if (tunnelState.getBlock() == Blocks.STONE) {
            primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
          } else if (tunnelState.getBlock() == Blocks.GRASS) {
            primer.setBlockState(x, y, z, Blocks.GRASS.getDefaultState());
          }
        }
      }
    }
    return primer;
  }

  public void applyBiomeFilter(World world, ICube cube) {
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        // Adding Missing Grass Blocks on Cube Boundary
        for (int y = 15; y < 17; y++) {
          BlockPos checkPos = cube.getCoords().getMinBlockPos().add(x, y, z);
          Biome biome = world.getBiome(checkPos);
          if (world.getBlockState(checkPos).getBlock() == Blocks.STONE
              && world.getBlockState(checkPos.add(0, 1, 0)).getMaterial() == Material.AIR) {
            world.setBlockState(checkPos, biome.topBlock, 3);
            addFillerMaterial(checkPos, world, biome);
          }
        }
        // Adding Filler Material
        for (int y = 0; y < 16; y++) {
          BlockPos checkPos = cube.getCoords().getMinBlockPos().add(x, y, z);
          Biome biome = world.getBiome(checkPos);
          if (world.getBlockState(checkPos).getMaterial() == Material.GRASS) {
            world.setBlockState(checkPos, biome.topBlock);
            addFillerMaterial(checkPos, world, biome);
          }
        }
      }
    }
  }

  private CubePrimer applyTunnelFilter(CubePrimer primer) {
    for (int x = 0; x < Cube.SIZE; x++) {
      for (int y = 0; y < Cube.SIZE; y++) {
        for (int z = 0; z < Cube.SIZE; z++) {
          IBlockState state = primer.getBlockState(x, y, z);
          if (state.getMaterial() == Material.AIR) {
            primer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
          } else if (state.getBlock() == Blocks.GRASS) {
            primer.setBlockState(x, y, z, Blocks.GRASS.getDefaultState());
          } else {
            primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
          }
        }
      }
    }
    return primer;
  }
}
