package com.wurmcraft.minecraftnotincluded.common.gen;

import static io.github.opencubicchunks.cubicchunks.api.world.ICube.SIZE;

import com.wurmcraft.minecraftnotincluded.common.biome.BiomeRegistry;
import io.github.opencubicchunks.cubicchunks.api.util.Box;
import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.core.asm.mixin.core.common.IGameRegistry;
import io.github.opencubicchunks.cubicchunks.core.util.CompatHandler;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

// Modified version of https://github.com/OpenCubicChunks/CubicChunks/blob/MC_1.12/src/main/java/io/github/opencubicchunks/cubicchunks/core/worldgen/generator/vanilla/VanillaCompatibilityGenerator.java
public class MNIWorldType extends WorldType implements ICubicWorldType {

  private IChunkGenerator vanillaGenerator;

  public MNIWorldType() {
    super("MNI");
  }

  @Override
  public IntRange calculateGenerationHeightRange(WorldServer world) {
    return new IntRange(0, 0);
  }

  @Override
  public boolean hasCubicGeneratorForWorld(World w) {
    return true;
  }

  public BiomeProvider getBiomeProvider(World world) {
    CustomGeneratorSettings conf = CustomGeneratorSettings.load(world);
    return makeBiomeProvider(world, conf);
  }

  public static BiomeProvider makeBiomeProvider(World world, CustomGeneratorSettings conf) {
    return new BiomeProviderSingle(BiomeRegistry.wasteland);
  }

  @Override
  public ICubeGenerator createCubeGenerator(World world) {
    return new ICubeGenerator() {
      @Override
      public CubePrimer generateCube(int cubeX, int cubeY, int cubeZ) {
        CubePrimer primer = new CubePrimer();
        if (vanillaGenerator == null) {
          vanillaGenerator =
              new ChunkGeneratorOverworld(
                  world,
                  world.getSeed(),
                  world.getWorldInfo().isMapFeaturesEnabled(),
                  world.getWorldInfo().getGeneratorOptions());
        }
        Chunk chunk = vanillaGenerator.generateChunk(cubeX, cubeZ);
        if (cubeY < 0) {
          for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
              for (int z = 0; z < SIZE; z++) {
                primer.setBlockState(x, y, z, Blocks.OBSIDIAN.getDefaultState());
              }
            }
          }
        } else if (cubeY <= 8) {
          for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
              for (int y = 0; y < SIZE; y++) {
                primer.setBlockState(x, y, z, chunk.getBlockState(x, (cubeY * 16) + y, z));
              }
            }
          }
        }

        return primer;
      }

      @Override
      public void generateColumn(Chunk chunk) {}

      @Override
      public void populate(ICube cube) {}

      // Copied from https://github.com/OpenCubicChunks/CubicChunks/blob/MC_1.12/src/main/java/io/github/opencubicchunks/cubicchunks/core/worldgen/generator/vanilla/VanillaCompatibilityGenerator.java#L187
      // All Credit to Barteks2x
      private Random getCubeSpecificRandom(int cubeX, int cubeY, int cubeZ) {
        Random rand = new Random(world.getSeed());
        rand.setSeed(rand.nextInt() ^ cubeX);
        rand.setSeed(rand.nextInt() ^ cubeZ);
        rand.setSeed(rand.nextInt() ^ cubeY);
        return rand;
      }

      // Copied from https://github.com/OpenCubicChunks/CubicChunks/blob/MC_1.12/src/main/java/io/github/opencubicchunks/cubicchunks/core/worldgen/generator/vanilla/VanillaCompatibilityGenerator.java#L311
      // All Credit to Barteks2x
      private void applyModGenerators(
          int x, int z, World world, IChunkGenerator vanillaGen, IChunkProvider provider) {
        List<IWorldGenerator> generators = IGameRegistry.getSortedGeneratorList();
        if (generators == null) {
          IGameRegistry.computeGenerators();
          generators = IGameRegistry.getSortedGeneratorList();
          assert generators != null;
        }
        long worldSeed = world.getSeed();
        Random fmlRandom = new Random(worldSeed);
        long xSeed = fmlRandom.nextLong() >> 2 + 1L;
        long zSeed = fmlRandom.nextLong() >> 2 + 1L;
        long chunkSeed = (xSeed * x + zSeed * z) ^ worldSeed;

        for (IWorldGenerator generator : generators) {
          fmlRandom.setSeed(chunkSeed);
          try {
            CompatHandler.beforeGenerate(world, generator);
            generator.generate(fmlRandom, x, z, world, vanillaGen, provider);
          } finally {
            CompatHandler.afterGenerate(world);
          }
        }
      }

      @Override
      public Box getFullPopulationRequirements(ICube iCube) {
        return new Box(0, 0, 0, 0, 0, 0);
      }

      @Override
      public Box getPopulationPregenerationRequirements(ICube iCube) {
        return new Box(0, 0, 0, 0, 0, 0);
      }

      @Override
      public void recreateStructures(ICube iCube) {}

      @Override
      public void recreateStructures(Chunk chunk) {}

      @Override
      public List<SpawnListEntry> getPossibleCreatures(
          EnumCreatureType enumCreatureType, BlockPos blockPos) {
        return new ArrayList<>();
      }

      @Override
      public BlockPos getClosestStructure(String s, BlockPos blockPos, boolean b) {
        return null;
      }
    };
  }

  public boolean isCustomizable() {
    return false;
  }
}
