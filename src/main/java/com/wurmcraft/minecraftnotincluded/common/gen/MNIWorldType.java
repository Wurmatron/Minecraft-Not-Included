package com.wurmcraft.minecraftnotincluded.common.gen;

import static io.github.opencubicchunks.cubicchunks.api.world.ICube.SIZE;

import io.github.opencubicchunks.cubicchunks.api.util.Box;
import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubeGeneratorsRegistry;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.core.asm.mixin.core.common.IGameRegistry;
import io.github.opencubicchunks.cubicchunks.core.util.CompatHandler;
import io.github.opencubicchunks.cubicchunks.core.worldgen.WorldgenHangWatchdog;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Post;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Pre;
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
    WorldSettings fakeSettings = new WorldSettings(world.getWorldInfo());
    ChunkGeneratorSettings.Factory fakeGenOpts = new ChunkGeneratorSettings.Factory();
    fakeGenOpts.biomeSize = conf.biomeSize;
    fakeGenOpts.riverSize = conf.riverSize;
    fakeSettings.setGeneratorOptions(fakeGenOpts.toString());
    WorldInfo fakeInfo = new WorldInfo(fakeSettings, world.getWorldInfo().getWorldName());
    fakeInfo.setTerrainType(WorldType.AMPLIFIED);
    Biome biome = Biome.getBiomeForId(conf.biome);
    return conf.biome < 0
        ? new BiomeProvider(fakeInfo)
        : new BiomeProviderSingle(biome == null ? Biomes.OCEAN : biome);
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
        Chunk chunk = vanillaGenerator.generateChunk(Math.abs(cubeX), Math.abs(cubeZ));
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

      // Modified from https://github.com/OpenCubicChunks/CubicChunks/blob/MC_1.12/src/main/java/io/github/opencubicchunks/cubicchunks/core/worldgen/generator/vanilla/VanillaCompatibilityGenerator.java#L275
      // All Credit to Barteks2x
      @Override
      public void populate(ICube cube) {
        try {
          WorldgenHangWatchdog.startWorldGen();
          MinecraftForge.EVENT_BUS.post(
              new Pre(vanillaGenerator, world, world.rand, cube.getX(), cube.getZ(), false));
          Random rand = getCubeSpecificRandom(cube.getX(), cube.getY(), cube.getZ());
          CubeGeneratorsRegistry.populateVanillaCubic(world, rand, cube);
          if (cube.getY() < 0) {
            //            || cube.getY() >= worldHeightCubes) {
            return;
          }
          //          if (cube.getY() >= 0 && cube.getY() < worldHeightCubes) {
          //            for (int y = worldHeightCubes - 1; y >= 0; y--) {
          //              ((ICubicWorldInternal) world).getCubeFromCubeCoords(cube.getX(), y, cube.getZ()).setPopulated(true);
          //            }

          try {
            vanillaGenerator.populate(Math.abs(cube.getX()), Math.abs(cube.getZ()));
          } catch (IllegalArgumentException ex) {
            StackTraceElement[] stack = ex.getStackTrace();
            if (stack == null
                || stack.length < 1
                || !stack[0].getClassName().equals(Random.class.getName())
                || !stack[0].getMethodName().equals("nextInt")) {
              throw ex;
            } else {
              ex.printStackTrace();
            }
          }
          applyModGenerators(
              Math.abs(cube.getX()),
              Math.abs(cube.getZ()),
              world,
              vanillaGenerator,
              world.getChunkProvider());
          MinecraftForge.EVENT_BUS.post(
              new Post(vanillaGenerator, world, world.rand, cube.getX(), cube.getZ(), false));
        } finally {
          WorldgenHangWatchdog.endWorldGen();
        }
      }

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
