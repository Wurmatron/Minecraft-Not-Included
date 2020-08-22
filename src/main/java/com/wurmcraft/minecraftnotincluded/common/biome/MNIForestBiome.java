package com.wurmcraft.minecraftnotincluded.common.biome;

import com.wurmcraft.minecraftnotincluded.common.biome.feature.WorldGenTree;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;

public class MNIForestBiome extends Biome {

  public static WorldGenTree treeGen = new WorldGenTree(false, 5,
      Blocks.LOG.getStateFromMeta(0), Blocks.LEAVES.getStateFromMeta(0), false);
  public static WorldGenTree specialTreeGen = new WorldGenTree(false, 5,
      Blocks.LOG.getStateFromMeta(1), Blocks.LEAVES.getStateFromMeta(1), false);

  public MNIForestBiome() {
    super(new BiomeProperties("forest"));
    setRegistryName("mniForest");
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
          BlockPos[] treePos = BiomeUtils.spreadFeatures(world, pos, 10, 2);
          for (BlockPos tPos : treePos) {
            Y_LOOP: for (int y = 0; y < 16; y++) {
              if (world.getBlockState(tPos.add(0, y, 0)).getBlock() == Blocks.AIR) {
                treeGen.generate(world, random, tPos.add(0, y, 0));
                break Y_LOOP;
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
