package com.wurmcraft.minecraftnotincluded.common.biome;

import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import java.util.*;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeRegistry {

  private static final int DEFAULT_BIOME_ID = 0;

  public static List<Biome> biomeIDS = new ArrayList<>();
  public static HashMap<ResourceLocation, Integer> biomeIdCache = new HashMap<>();
  public static int BIOME_COUNT = 0;

  public static Biome wasteland;

  public static void setup() {
    if (biomeIDS.size() == 0) {
      for (int index = 0; index < ForgeRegistries.BIOMES.getValues().size(); index++) {
        Biome biome = ForgeRegistries.BIOMES.getValues().get(index);
        biomeIDS.add(biome);
        biomeIdCache.put(biome.getRegistryName(), index);
      }
      BIOME_COUNT = biomeIDS.size();
    }
  }

  public static int getBiomeID(Biome biome) {
    return biomeIdCache.getOrDefault(biome.getRegistryName(), DEFAULT_BIOME_ID);
  }

  public static Biome getBiome(int id) {
    return biomeIDS.get(id) != null ? biomeIDS.get(id) : Biome.getBiome(DEFAULT_BIOME_ID);
  }

  public static final Biome getDefaultBiome() {
    return getBiome(DEFAULT_BIOME_ID);
  }

  public static final MNISwampBiome swamp = new MNISwampBiome();
  public static final MNIForestBiome forest = new MNIForestBiome();
  public static final MNIMushroomForestBiome mushroomForest = new MNIMushroomForestBiome();

  @SubscribeEvent
  public void registerBiome(RegistryEvent.Register<Biome> e) {
    e.getRegistry().register(swamp);
    e.getRegistry().register(forest);
    e.getRegistry().register(mushroomForest);
    wasteland =
        new Biome(
            new BiomeProperties("Wasteland")
                .setTemperature(2.8f)
                .setBaseHeight(.75f)
                .setRainDisabled()
                .setWaterColor(0x8A3324)) {

          @Override
          public int getSkyColorByTemp(float currentTemperature) {
            return 0xC0A080;
          }

          @Override
          public BiomeDecorator createBiomeDecorator() {
            return new BiomeDecorator() {
              @Override
              protected void genDecorations(Biome biomeIn, World worldIn, Random random) {
                net.minecraft.util.math.ChunkPos forgeChunkPos =
                    new net.minecraft.util.math.ChunkPos(
                        chunkPos); // actual ChunkPos instead of BlockPos, used for events
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(
                    new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(
                        worldIn, random, forgeChunkPos));

                if (net.minecraftforge.event.terraingen.TerrainGen.decorate(
                    worldIn,
                    random,
                    forgeChunkPos,
                    net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType
                        .SAND)) {
                  for (int i = 0; i < this.sandPatchesPerChunk; ++i) {
                    int j = random.nextInt(16) + 8;
                    int k = random.nextInt(16) + 8;
                    this.sandGen.generate(
                        worldIn,
                        random,
                        worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(j, 0, k)));
                  }
                }

                if (net.minecraftforge.event.terraingen.TerrainGen.decorate(
                    worldIn,
                    random,
                    forgeChunkPos,
                    net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType
                        .CLAY)) {
                  for (int i1 = 0; i1 < this.clayPerChunk; ++i1) {
                    int l1 = random.nextInt(16) + 8;
                    int i6 = random.nextInt(16) + 8;
                    this.clayGen.generate(
                        worldIn,
                        random,
                        worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(l1, 0, i6)));
                  }
                }

                if (net.minecraftforge.event.terraingen.TerrainGen.decorate(
                    worldIn,
                    random,
                    forgeChunkPos,
                    net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType
                        .SAND_PASS2)) {
                  for (int j1 = 0; j1 < this.gravelPatchesPerChunk; ++j1) {
                    int i2 = random.nextInt(16) + 8;
                    int j6 = random.nextInt(16) + 8;
                    this.gravelGen.generate(
                        worldIn,
                        random,
                        worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(i2, 0, j6)));
                  }
                }

                if (net.minecraftforge.event.terraingen.TerrainGen.decorate(
                    worldIn,
                    random,
                    forgeChunkPos,
                    net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType
                        .DEAD_BUSH)) {
                  for (int j3 = 0; j3 < this.deadBushPerChunk; ++j3) {
                    int k7 = random.nextInt(16) + 8;
                    int j11 = random.nextInt(16) + 8;
                    int l14 = worldIn.getHeight(this.chunkPos.add(k7, 0, j11)).getY() * 2;

                    if (l14 > 0) {
                      int i18 = random.nextInt(l14);
                      (new WorldGenDeadBush())
                          .generate(worldIn, random, this.chunkPos.add(k7, i18, j11));
                    }
                  }
                }

                if (this.generateFalls) {
                  if (net.minecraftforge.event.terraingen.TerrainGen.decorate(
                      worldIn,
                      random,
                      forgeChunkPos,
                      net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType
                          .LAKE_WATER)) {
                    for (int k5 = 0; k5 < 50; ++k5) {
                      int i10 = random.nextInt(16) + 8;
                      int l13 = random.nextInt(16) + 8;
                      int i17 = random.nextInt(248) + 8;

                      if (i17 > 0) {
                        int k19 = random.nextInt(i17);
                        BlockPos blockpos6 = this.chunkPos.add(i10, k19, l13);
                        (new WorldGenLiquids(Blocks.FLOWING_LAVA))
                            .generate(worldIn, random, blockpos6);
                      }
                    }
                  }

                  if (net.minecraftforge.event.terraingen.TerrainGen.decorate(
                      worldIn,
                      random,
                      forgeChunkPos,
                      net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType
                          .LAKE_LAVA)) {
                    for (int l5 = 0; l5 < 20; ++l5) {
                      int j10 = random.nextInt(16) + 8;
                      int i14 = random.nextInt(16) + 8;
                      int j17 = random.nextInt(random.nextInt(random.nextInt(240) + 8) + 8);
                      BlockPos blockpos3 = this.chunkPos.add(j10, j17, i14);
                      (new WorldGenLiquids(Blocks.FLOWING_LAVA))
                          .generate(worldIn, random, blockpos3);
                    }
                  }
                }
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(
                    new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post(
                        worldIn, random, forgeChunkPos));
              }
            };
          }
        };
    wasteland.setRegistryName(Global.MODID, "wasteland");
    wasteland.fillerBlock = MinecraftNotIncludedBlocks.blockDust.getDefaultState();
    wasteland.topBlock = MinecraftNotIncludedBlocks.blockCompressedDust.getDefaultState();
    e.getRegistry().register(wasteland);
  }
}
