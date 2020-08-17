package com.wurmcraft.minecraftnotincluded.common.utils;

import com.wurmcraft.minecraftnotincluded.common.block.farm.t1.BlockCasing;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockUtils {

  public static int[][] getMultiblockDimensions(World world, BlockPos corePos) {
    return new int[][] {
      getDimension(world, corePos, "X"),
      getDimension(world, corePos, "Y"),
      getDimension(world, corePos, "Z")
    };
  }

  public static int[][] getAdjustedMultiblockDimensions(World world, BlockPos corePos) {
    int[][] dimensions = getMultiblockDimensions(world, corePos);
    // X
    dimensions[0][0] = dimensions[0][0] + 1;
    dimensions[0][1] = dimensions[0][0];
    // Y
    dimensions[1][0] = dimensions[1][0] + 1;
    dimensions[1][1] = dimensions[1][0];
    // Z
    dimensions[1][0] = dimensions[1][0] + 1;
    dimensions[1][1] = dimensions[1][0];
    return dimensions;
  }

  private static int[] getDimension(World world, BlockPos pos, String type) {
    if (type.equals("X")) {
      int minX = 0;
      while (true) {
        Block minBlock = world.getBlockState(pos.add(-minX, 0, 0)).getBlock();
        if (minBlock instanceof BlockCasing) {
          minX++;
        } else {
          break;
        }
      }
      int maxX = 0;
      while (true) {
        Block maxBlock = world.getBlockState(pos.add(maxX, 0, 0)).getBlock();
        if (maxBlock instanceof BlockCasing) {
          maxX++;
        } else {
          break;
        }
      }
      return new int[] {-minX, maxX};
    }
    if (type.equals("Y")) {
      int minY = 0;
      while (true) {
        Block minBlock = world.getBlockState(pos.add(0, -minY, 0)).getBlock();
        if (minBlock instanceof BlockCasing) {
          minY++;
        } else {
          break;
        }
      }
      int maxY = 0;
      while (true) {
        Block maxBlock = world.getBlockState(pos.add(0, maxY, 0)).getBlock();
        if (maxBlock instanceof BlockCasing) {
          maxY++;
        } else {
          break;
        }
      }
      return new int[] {-minY, maxY};
    }
    if (type.equals("Z")) {
      int minZ = 0;
      while (true) {
        Block minBlock = world.getBlockState(pos.add(0, 0, -minZ)).getBlock();
        if (minBlock instanceof BlockCasing) {
          minZ++;
        } else {
          break;
        }
      }
      int maxZ = 0;
      while (true) {
        Block maxBlock = world.getBlockState(pos.add(0, 0, maxZ)).getBlock();
        if (maxBlock instanceof BlockCasing) {
          maxZ++;
        } else {
          break;
        }
      }
      return new int[] {-minZ, maxZ};
    }
    return new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE};
  }
}
