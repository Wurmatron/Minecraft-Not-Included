package com.wurmcraft.minecraftnotincluded.common.biome;

import com.wurmcraft.minecraftnotincluded.common.biome.feature.WorldGenLargeMushroom;
import com.wurmcraft.minecraftnotincluded.common.biome.feature.WorldGenTree;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenLakes;


public class MNISwampBiome extends Biome {

  public int maxPopulationPerChunk = 5;
  public static WorldGenTree tree = new WorldGenTree(false, 4,
      Blocks.LOG.getStateFromMeta(0), Blocks.LEAVES.getStateFromMeta(0), true);
  public static WorldGenBigMushroom brownMushroom = new WorldGenBigMushroom(
      Blocks.BROWN_MUSHROOM_BLOCK);
  public static WorldGenLakes lake = new WorldGenLakes(Blocks.WATER);
  public static WorldGenLargeMushroom largeGlowingMushroom = new WorldGenLargeMushroom(
      Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(
      BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));

  public MNISwampBiome() {
    super(new BiomeProperties("mniSwamp").setBaseHeight(1).setHeightVariation(.2f)
        .setTemperature(.8f).setWaterColor(Color.GREEN.getRGB()));
    setRegistryName("mniSwamp");
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
                int type = world.rand.nextInt(3);
                if (type == 0) {
                  tree.generate(world, random, a.add(0, y, 0));
                  break;
                } else if (type == 1) {
                  if (world.rand.nextInt(2) == 0) {
                    largeGlowingMushroom.generate(world, random, a.add(0, y, 0),3 + (2 * world.rand.nextInt(4)));
                  } else {
                    brownMushroom.generate(world, random, a.add(0, y, 0));
                  }
                } else if (type == 2) {
                  if (world.rand.nextInt(2) == 0) {
                    lake.generate(world, random, a.add(0, y - 1, 0));
                    return;
                  } else {
                    tree.generate(world, random, a.add(0, y, 0));
                  }
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

  private BlockPos[] spreadFeatures(World world, BlockPos pos, int maxAmount,
      int distance) {
    List<BlockPos> features = new ArrayList<>();
    features.add(pos.add(world.rand.nextInt(8), 0, world.rand.nextInt(8)));
    for (int f = 0; f < maxAmount; f++) {
      BlockPos newPos = pos.add(world.rand.nextInt(8), 0, world.rand.nextInt(8));
      if (!isNear(features, newPos, distance)) {
        features.add(newPos);
      }
      if (world.rand.nextInt(maxAmount) > 0) {
        break;
      }
    }

    return features.toArray(new BlockPos[0]);
  }

  private boolean isNear(List<BlockPos> current, BlockPos pos, int distance) {
    for (BlockPos p : current) {
      if (p.getX() + distance < pos.getX() && pos.getX() - distance > pos.getX()) {
        return false;
      }
      if (p.getZ() + distance < pos.getZ() && pos.getZ() - distance > pos.getZ()) {
        return false;
      }
    }
    return true;
  }


}
