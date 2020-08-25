package com.wurmcraft.minecraftnotincluded.common.biome.feature;

import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import io.github.opencubicchunks.cubicchunks.core.CubicChunks;
import java.util.Random;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class WorldGenTree extends WorldGenAbstractTree {

  private int minTreeHeightIn;
  private IBlockState woodMeta;
  private IBlockState leavesMeta;
  private boolean growVines;
  public IBlockState beanMeta;

  public WorldGenTree(boolean notify, int minTreeHeightIn,
      IBlockState woodMeta, IBlockState leavesMeta, boolean growVines) {
    super(notify);
    this.minTreeHeightIn = minTreeHeightIn;
    this.woodMeta = woodMeta;
    this.leavesMeta = leavesMeta;
    this.growVines = growVines;
    this.beanMeta = null;

  }

  public WorldGenTree(boolean notify, int minTreeHeightIn,
      IBlockState woodMeta, IBlockState leavesMeta, boolean growVines,
      IBlockState beanMeta) {
    super(notify);
    this.minTreeHeightIn = minTreeHeightIn;
    this.woodMeta = woodMeta;
    this.leavesMeta = leavesMeta;
    this.growVines = growVines;
    this.beanMeta = beanMeta;
  }

  @Override
  public boolean generate(World world, Random rand, BlockPos pos) {
    int treeHeight = rand.nextInt(3) + this.minTreeHeightIn;
    boolean hasChecked = true;
    if (pos.getY() >= CubicChunks.MIN_SUPPORTED_BLOCK_Y
        && pos.getY() + treeHeight + CubicChunks.MIN_SUPPORTED_BLOCK_Y <= world
        .getHeight()) {
      for (int treeY = pos.getY(); treeY <= pos.getY() + 1 + treeHeight; ++treeY) {
        int currentTreeHeight = 1;
        if (treeY == pos.getY()) {
          currentTreeHeight = 0;
        }
        if (treeY >= pos.getY() + 1 + treeHeight - 2) {
          currentTreeHeight = 2;
        }
        BlockPos.MutableBlockPos leavesPos = new BlockPos.MutableBlockPos();
        for (int l = pos.getX() - currentTreeHeight;
            l <= pos.getX() + currentTreeHeight && hasChecked; ++l) {
          for (int i1 = pos.getZ() - currentTreeHeight;
              i1 <= pos.getZ() + currentTreeHeight && hasChecked; ++i1) {
            if (treeY >= CubicChunks.MIN_SUPPORTED_BLOCK_Y && treeY < world.getHeight()) {
              if (!this.isReplaceable(world, leavesPos.setPos(l, treeY, i1))) {
                hasChecked = false;
              }
            } else {
              hasChecked = false;
            }
          }
        }
      }
      if (!hasChecked) {
        return false;
      } else {
        IBlockState state = world.getBlockState(pos.down());
        if (state.getBlock().canSustainPlant(state, world, pos.down(), EnumFacing.UP,
            (BlockSapling) Blocks.SAPLING)
            && pos.getY() < world.getHeight() - treeHeight - 1) {
          state.getBlock().onPlantGrow(state, world, pos.down(), pos);
          for (int leaveY = pos.getY() - 3 + treeHeight;
              leaveY <= pos.getY() + treeHeight;
              ++leaveY) {
            int adjLeaveHeight = leaveY - (pos.getY() + treeHeight);
            int startingPos = 1 - adjLeaveHeight / 2;

            for (int leaveX = pos.getX() - startingPos;
                leaveX <= pos.getX() + startingPos; ++leaveX) {
              int adjX = leaveX - pos.getX();

              for (int leaveZ = pos.getZ() - startingPos;
                  leaveZ <= pos.getZ() + startingPos; ++leaveZ) {
                int adjZ = leaveZ - pos.getZ();

                if (Math.abs(adjX) != startingPos || Math.abs(adjZ) != startingPos
                    || rand.nextInt(2) != 0 && adjLeaveHeight != 0) {
                  BlockPos blockpos = new BlockPos(leaveX, leaveY, leaveZ);
                  state = world.getBlockState(blockpos);
                  if (state.getBlock().isAir(state, world, blockpos) || state.getBlock()
                      .isLeaves(state, world, blockpos)
                      || state.getMaterial() == Material.VINE) {
                    this.setBlockAndNotifyAdequately(world, blockpos, this.leavesMeta);
                  }
                }
              }
            }
          }
          // Add Wood / Direct vines
          for (int j3 = 0; j3 < treeHeight; ++j3) {
            BlockPos upN = pos.up(j3);
            state = world.getBlockState(upN);
            if (state.getBlock().isAir(state, world, upN) || state.getBlock()
                .isLeaves(state, world, upN) || state.getMaterial() == Material.VINE) {
              this.setBlockAndNotifyAdequately(world, pos.up(j3), this.woodMeta);
              // Direct Vines
              if (this.growVines && j3 > 0) {
                if (rand.nextInt(3) > 0 && world.isAirBlock(pos.add(-1, j3, 0))) {
                  this.addVine(world, pos.add(-1, j3, 0), BlockVine.EAST);
                }
                if (rand.nextInt(3) > 0 && world.isAirBlock(pos.add(1, j3, 0))) {
                  this.addVine(world, pos.add(1, j3, 0), BlockVine.WEST);
                }
                if (rand.nextInt(3) > 0 && world.isAirBlock(pos.add(0, j3, -1))) {
                  this.addVine(world, pos.add(0, j3, -1), BlockVine.SOUTH);
                }
                if (rand.nextInt(3) > 0 && world.isAirBlock(pos.add(0, j3, 1))) {
                  this.addVine(world, pos.add(0, j3, 1), BlockVine.NORTH);
                }
              }
            }
          }
          // Grow InDirect
          if (this.growVines) {
            for (int y = pos.getY() - 3 + treeHeight; y <= pos.getY() + treeHeight; ++y) {
              int maxHeight = y - (pos.getY() + treeHeight);
              int adjHeight = 2 - maxHeight / 2;
              BlockPos.MutableBlockPos vineStartPos = new BlockPos.MutableBlockPos();
              for (int x = pos.getX() - adjHeight; x <= pos.getX() + adjHeight; ++x) {
                for (int z = pos.getZ() - adjHeight; z <= pos.getZ() + adjHeight; ++z) {
                  vineStartPos.setPos(x, y, z);
                  state = world.getBlockState(vineStartPos);
                  if (state.getBlock()
                      .isLeaves(state, world, vineStartPos)) {
                    BlockPos west = vineStartPos.west();
                    BlockPos east = vineStartPos.east();
                    BlockPos north = vineStartPos.north();
                    BlockPos south = vineStartPos.south();
                    if (rand.nextInt(4) == 0 && world.isAirBlock(west)) {
                      this.addHangingVine(world, west, BlockVine.EAST);
                    }
                    if (rand.nextInt(4) == 0 && world.isAirBlock(east)) {
                      this.addHangingVine(world, east, BlockVine.WEST);
                    }
                    if (rand.nextInt(4) == 0 && world.isAirBlock(north)) {
                      this.addHangingVine(world, north, BlockVine.SOUTH);
                    }
                    if (rand.nextInt(4) == 0 && world.isAirBlock(south)) {
                      this.addHangingVine(world, south, BlockVine.NORTH);
                    }
                  }
                }
              }
            }
            // Place Bean
            if (beanMeta != null && rand.nextInt(5) == 0 && treeHeight > 5) {
              for (int height = 0; height < 2; ++height) {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                  if (rand.nextInt(4 - height) == 0) {
                    EnumFacing facing = enumfacing.getOpposite();
                    this.placeBean(world, rand.nextInt(3),
                        pos.add(facing.getFrontOffsetX(), treeHeight - 5 + height,
                            facing.getFrontOffsetZ()), enumfacing);
                  }
                }
              }
            }
          }

          return true;
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
  }

  private void addVine(World worldIn, BlockPos pos, PropertyBool prop) {
    this.setBlockAndNotifyAdequately(worldIn, pos,
        MinecraftNotIncludedBlocks.glowingVines.getDefaultState()
            .withProperty(prop, Boolean.valueOf(true)));
  }

  private void addHangingVine(World worldIn, BlockPos pos, PropertyBool prop) {
    this.addVine(worldIn, pos, prop);
    int i = 4;

    for (BlockPos blockpos = pos.down(); worldIn.isAirBlock(blockpos) && i > 0; --i) {
      this.addVine(worldIn, blockpos, prop);
      blockpos = blockpos.down();
    }
  }

  private void placeBean(World worldIn, int p_181652_2_, BlockPos pos, EnumFacing side) {
    if (beanMeta != null) {
      this.setBlockAndNotifyAdequately(worldIn, pos,
          beanMeta.getBlock().getDefaultState().withProperty(
              BlockCocoa.AGE, Integer.valueOf(p_181652_2_))
              .withProperty(BlockCocoa.FACING, side));
    }
  }
}
