package com.wurmcraft.minecraftnotincluded.common.world.overworld;

import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import io.github.opencubicchunks.cubicchunks.api.util.Coords;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.cubicgen.BasicCubeGenerator;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.CubicBiome;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;

public class MNIOverworldGenerator extends BasicCubeGenerator {

  private MNIBiomeSource biomeSource;
  private OverworldTerrainBuilder terrainBuilder;

  public MNIOverworldGenerator(World world) {
    this(world, world.getBiomeProvider());
  }

  public MNIOverworldGenerator(World world, BiomeProvider provider) {
    super(world);
    biomeSource = new MNIBiomeSource(world, provider);
    terrainBuilder = new OverworldTerrainBuilder(world, biomeSource);
  }

  @Override
  public CubePrimer generateCube(int cubeX, int cubeY, int cubeZ) {
    CubePrimer cp = new CubePrimer();
    fillBiomes(cp, cubeX, cubeY, cubeZ);
    terrainBuilder.createPrimer(cp, cubeX, cubeY, cubeZ);
    return cp;
  }

  private void fillBiomes(CubePrimer cp, int cubeX, int cubeY, int cubeZ) {
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        for (int z = 0; z < 4; z++) {
          cp.setBiome(x, y, z, biomeSource.getBiome(cubeX + x, cubeY + y, cubeZ + cubeZ));
        }
      }
    }
  }

  @Override
  public void populate(ICube cube) {
    terrainBuilder.applyBiomeFilter(cube.getWorld(), cube);

    // Apply Biome Decorator
    CubicBiome cubicBiome =
        CubicBiome.getCubic(cube.getWorld().getBiome(Coords.getCubeCenter(cube)));
    //    cubicBiome.getBiome().decorator.mushroomBrownGen =
    //        new WorldGenBush(MinecraftNotIncludedBlocks.glowingMushroom);
    cubicBiome.getBiome().decorator.mushroomsPerChunk = 0;
    cubicBiome
        .getDecorator(new CustomGeneratorSettings())
        .generate(world, world.rand, cube.getCoords(), cubicBiome.getBiome());

    for (int x = 0; x < 16; x++) {
      for (int y = 16; y >= 0; y--) {
        for (int z = 0; z < 16; z++) {
          if (world.isAirBlock(cube.getCoords().getMinBlockPos().add(x, y, z))
              && world
                      .getBlockState(cube.getCoords().getMinBlockPos().add(x, y - 1, z))
                      .getMaterial()
                  != Material.AIR
              && world.getLight(cube.getCoords().getMinBlockPos().add(x, y, z)) < 4) {
            world.setBlockState(
                cube.getCoords().getMinBlockPos().add(x, y, z),
                MinecraftNotIncludedBlocks.glowingMushroom.getMushroomForBiome(
                    world, cubicBiome.getBiome()),
                3);
          }
        }
      }
    }
  }
}
