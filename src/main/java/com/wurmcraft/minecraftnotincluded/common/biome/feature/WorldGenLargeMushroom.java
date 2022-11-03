package com.wurmcraft.minecraftnotincluded.common.biome.feature;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenLargeMushroom extends WorldGenerator {

  private static final int DEFAULT_SIZE = 5;
  public IBlockState mushroomBlock;
  public IBlockState stemBlock;

  public WorldGenLargeMushroom(IBlockState mushroomBlock, IBlockState stemBlock) {
    this.mushroomBlock = mushroomBlock;
    this.stemBlock = stemBlock;
  }

  @Override
  public boolean generate(World world, Random rand, BlockPos pos) {
    return generate(world, rand, pos, DEFAULT_SIZE);
  }

  public boolean generate(World world, Random rand, BlockPos pos, int size) {
    if (canMushroomSpawn(world, pos, size)) {
      drawSquare(world, pos, size, mushroomBlock);
      for (int height = 1; height < size - 1; height++) {
        drawHollowSquareNoCorners(world, pos.add(-1, -height, -1), size, mushroomBlock);
      }
      int startY = 1;
      while (world.getBlockState(pos.add(0, -startY, 0)).getBlock() == Blocks.AIR) {
        startY++;
      }
      for (int y = 0; y < startY + 1; y++) {
        world.setBlockState(pos.down().add(size / 2, -y, size / 2), stemBlock);
      }
      return true;
    }
    return false;
  }

  private boolean canMushroomSpawn(World world, BlockPos pos, int size) {
    for (int x = -size; x < size; x++) {
      for (int z = -size; z < size; z++) {
        for (int y = -(size / 2); y < size; y++) {
          if (world.getBlockState(pos.add(x, y, z)).getBlock() != Blocks.AIR) {
            return false;
          }
        }
      }
    }
    for (int y = -(size * 2); y < -size; y++) {
      if (world.getBlockState(pos.add(0, y, 0)).getBlock() == Blocks.GRASS) {
        return true;
      }
    }
    return false;
  }

  public void drawLine(World world, BlockPos pos, String direction, int length, IBlockState state) {
    for (int x = 0; x < length; x++) {
      if (direction.equals("x")) {
        world.setBlockState(pos.add(x, 0, 0), state);
      } else if (direction.equals("y")) {
        world.setBlockState(pos.add(0, x, 0), state);
      } else if (direction.equals("z")) {
        world.setBlockState(pos.add(0, 0, x), state);
      }
    }
  }

  public void drawSquare(World world, BlockPos pos, int size, IBlockState state) {
    for (int s = 0; s < size; s++) {
      drawLine(world, pos.add(s, 0, 0), "z", size, state);
    }
  }

  public void drawHollowSquareNoCorners(World world, BlockPos pos, int size, IBlockState state) {
    drawLine(world, pos.add(0, 0, 1), "z", size, state);
    drawLine(world, pos.add(1, 0, 0), "x", size, state);
    drawLine(world, pos.add(1, 0, size + 1), "x", size, state);
    drawLine(world, pos.add(size + 1, 0, 1), "z", size, state);
  }
}
