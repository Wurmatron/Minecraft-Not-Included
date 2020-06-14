package com.wurmcraft.minecraftnotincluded.common.world.overworld;

import static com.wurmcraft.minecraftnotincluded.common.world.overworld.cavern.TerrainGeneratorUtils.addFillerMaterial;

import com.wurmcraft.minecraftnotincluded.common.ConfigHandler.Wasteland;
import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystal;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystal.Type;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingDoublePlant.EnumPlantType;
import com.wurmcraft.minecraftnotincluded.common.world.overworld.cavern.CavernGenerator;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.core.world.cube.Cube;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.CubicBiome;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class OverworldTerrainBuilder {

  private World world;
  private CavernGenerator cavernGenerator;

  public OverworldTerrainBuilder(World world) {
    this.world = world;
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
    if (Wasteland.enabled) {
      if (cubeY > (Wasteland.startY / 16)) {
        return fillWithBlock(new CubePrimer(), Blocks.AIR.getDefaultState());
      }
    }
    CubePrimer maskPrimer = new CubePrimer();
    fillWithBlock(maskPrimer, Blocks.DIRT.getDefaultState());
    cavernGenerator.generate(world, maskPrimer, new CubePos(cubeX, cubeY, cubeZ));
    fillWithBlock(primer, Blocks.STONE.getDefaultState());
    CubePrimer tunnel = applyTunnelFilter(maskPrimer);
    for (int x = 0; x < Cube.SIZE; x++) {
      for (int y = 0; y < Cube.SIZE; y++) {
        for (int z = 0; z < Cube.SIZE; z++) {
          IBlockState tunnelState = tunnel.getBlockState(x, y, z);
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

  private boolean isGround(ICube cube, BlockPos pos) {
    return world.getBlockState(pos.down()).getMaterial() == Material.GRASS;
  }

  private boolean isCeiling(ICube cube, BlockPos pos) {
    return world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR
        && world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR
        && world.getBlockState(pos.add(0, 3, 0)).isFullCube()
        && world.getBlockState(pos.add(0, 3, 0)).getMaterial() != Material.LEAVES;
  }

  private void addMushroom(CubicBiome biome, BlockPos pos) {
    world.setBlockState(
        pos,
        MinecraftNotIncludedBlocks.glowingMushroom.getMushroomForBiome(world, biome.getBiome()),
        3);
  }

  private void addSmallCrystal(CubicBiome biome, BlockPos pos) {
    world.setBlockState(
        pos,
        MinecraftNotIncludedBlocks.glowingCrystal
            .getDefaultState()
            .withProperty(
                BlockGlowingCrystal.TYPE,
                BlockGlowingCrystal.Type.values()[world.rand.nextInt(Type.values().length - 1)]),
        3);
  }

  public void addCeilingPlant(CubicBiome biome, BlockPos pos) {
    world.setBlockState(pos.up().up(), MinecraftNotIncludedBlocks.largeVine.getDefaultState(), 3);
    MinecraftNotIncludedBlocks.glowingDoublePlant.placeAt(
        world, pos, EnumPlantType.values()[world.rand.nextInt(EnumPlantType.values().length)], 3);
  }

  public void addCeilingCrystal(CubicBiome biome, BlockPos pos) {
    if (world.rand.nextInt(4) == 0) {
      world.setBlockState(
          pos.up().up(), MinecraftNotIncludedBlocks.glowingVines.getDefaultState(), 3);
    } else {
      world.setBlockState(
          pos.up().up(),
          MinecraftNotIncludedBlocks.glowingCrystalHanging
              .getDefaultState()
              .withProperty(
                  BlockGlowingCrystal.TYPE,
                  BlockGlowingCrystal.Type.values()[world.rand.nextInt(Type.values().length - 1)]),
          3);
    }
  }

  public void addLighting(CubicBiome biome, ICube cube) {
    int maxGroundLights = (int) (8 + (biome.getBiome().getRainfall() * 10));
    int maxCeilingLights = (int) (biome.getBiome().getRainfall() * 10);
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        if (world.rand.nextInt(3) == 0) { // 25%
          for (int y = 16; y >= 0; y--) {
            BlockPos pos = cube.getCoords().getMinBlockPos().add(x, y, z);
            if (world.getBlockState(pos).getMaterial() == Material.AIR) {
              if (maxGroundLights > 0 && isGround(cube, pos) && world.rand.nextInt(4) == 0) {
                if (world.rand.nextInt(10) == 0) { // 5%
                  addSmallCrystal(biome, pos);
                } else {
                  addMushroom(biome, pos);
                }
                maxGroundLights--;
              }
              if (maxCeilingLights > 0 && isCeiling(cube, pos) && world.rand.nextInt(6) == 0) {
                if (world.rand.nextInt(8) == 0) { // 12.5%
                  addCeilingCrystal(biome, pos);
                } else {
                  addCeilingPlant(biome, pos);
                }
                maxCeilingLights--;
              }
            }
            if (maxCeilingLights <= 0 && maxGroundLights <= 0) {
              return;
            }
          }
        }
      }
    }
  }

  //  public void addLighting(CubicBiome cubicBiome, ICube cube) {
  //    int groundLightsPerCube = 20 + ((int) cubicBiome.getBiome().getRainfall() * 10);
  //    int ceilingLights = 5 + ((int) cubicBiome.getBiome().getRainfall() * 15);
  //    ALL:
  //    for (int x = 0; x < 16; x++) {
  //      for (int y = 16; y >= 0; y--) {
  //        for (int z = 0; z < 16; z++) {
  //          if (world.rand.nextInt(3) == 0
  //              && world.isAirBlock(cube.getCoords().getMinBlockPos().add(x, y, z))) {
  //            // Add Mushrooms
  //            if (groundLightsPerCube > 0
  //                && world
  //                        .getBlockState(cube.getCoords().getMinBlockPos().add(x, y - 1, z))
  //                        .getMaterial()
  //                    != Material.AIR
  //                && world.getLight(cube.getCoords().getMinBlockPos().add(x, y, z)) < 4
  //                && world.isBlockFullCube(cube.getCoords().getMinBlockPos().add(x, y - 1, z))) {
  //              BlockPos pos = cube.getCoords().getMinBlockPos().add(x, y, z);
  //              world.setBlockState(
  //                  pos,
  //                  MinecraftNotIncludedBlocks.glowingMushroom.getMushroomForBiome(
  //                      world, cubicBiome.getBiome()),
  //                  3);
  //              groundLightsPerCube--;
  //            }
  //            // Add Hanging
  //            if (ceilingLights > 0
  //                && world
  //                        .getBlockState(cube.getCoords().getMinBlockPos().add(x, y + 1, z))
  //                        .getMaterial()
  //                    != Material.AIR
  //                && world
  //                        .getBlockState(cube.getCoords().getMinBlockPos().add(x, y - 2, z))
  //                        .getMaterial()
  //                    == Material.AIR) {
  //              MinecraftNotIncludedBlocks.glowingDoublePlant.placeAt(
  //                  world,
  //                  cube.getCoords().getMinBlockPos().add(x, y - 1, z),
  //                  EnumPlantType.Hibiscus,
  //                  3);
  //              ceilingLights--;
  //            }
  //          }
  //          if (groundLightsPerCube <= 0 && ceilingLights <= 0) {
  //            break ALL;
  //          }
  //        }
  //      }
  //    }
  //  }
}
