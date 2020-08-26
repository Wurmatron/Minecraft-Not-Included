package com.wurmcraft.minecraftnotincluded.common.biome;

import static com.wurmcraft.minecraftnotincluded.common.biome.BiomeUtils.spreadFeatures;

import java.awt.Color;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenCactus;


public class MNIBarronBiome extends Biome {

  public static final int maxPopulationPerChunk = 3;
  public static WorldGenBush bushGen = new WorldGenBush(Blocks.DEADBUSH);
  public static WorldGenCactus cactusGen = new WorldGenCactus();
  // TODO Biome Needs Light

  public MNIBarronBiome() {
    super(
        new BiomeProperties("mniBarron").setBaseHeight(1).setHeightVariation(.2f)
            .setTemperature(.8f).setWaterColor(Color.MAGENTA.getRGB()));
    setRegistryName("mniBarron");
    this.topBlock = Blocks.SAND.getDefaultState();
    this.fillerBlock = Blocks.SANDSTONE.getDefaultState();
  }

  @Override
  public BiomeDecorator createBiomeDecorator() {
    BiomeDecorator decor = new BiomeDecorator() {
      @Override
      public void decorate(World world, Random random, Biome biome, BlockPos pos) {
        if (this.decorating) {
          throw new RuntimeException("Already decorating");
        } else {
          this.chunkPos = pos;
          BlockPos[] features = spreadFeatures(world, pos, maxPopulationPerChunk, 6);
          for (BlockPos a : features) {
            for (int y = 0; y < 16; y++) {
              if (world.getBlockState(a.add(0, y, 0)).getBlock() == Blocks.AIR) {
                int type = random.nextInt(2);
                if(type == 0) {
                  bushGen.generate(world, random, a.add(0, y, 0));
                } else {
                  cactusGen.generate(world, random, a.add(0,y,0));
                }
              }
            }
          }
          this.decorating = false;
        }
      }
    };
    return decor;
  }
}
