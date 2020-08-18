package com.wurmcraft.minecraftnotincluded.common.biome.feature;

import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import java.util.Random;
import net.minecraft.block.BlockCocoa;
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
    int i = rand.nextInt(3) + this.minTreeHeightIn;
    boolean flag = true;

    if (pos.getY() >= 1 && pos.getY() + i + 1 <= world.getHeight()) {
      for (int j = pos.getY(); j <= pos.getY() + 1 + i; ++j) {
        int k = 1;

        if (j == pos.getY()) {
          k = 0;
        }

        if (j >= pos.getY() + 1 + i - 2) {
          k = 2;
        }

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int l = pos.getX() - k; l <= pos.getX() + k && flag; ++l) {
          for (int i1 = pos.getZ() - k; i1 <= pos.getZ() + k && flag; ++i1) {
            if (j >= 0 && j < world.getHeight()) {
              if (!this.isReplaceable(world, blockpos$mutableblockpos.setPos(l, j, i1))) {
                flag = false;
              }
            } else {
              flag = false;
            }
          }
        }
      }

      if (!flag) {
        return false;
      } else {
        IBlockState state = world.getBlockState(pos.down());

        if (state.getBlock()
            .canSustainPlant(state, world, pos.down(), net.minecraft.util.EnumFacing.UP,
                (net.minecraft.block.BlockSapling) Blocks.SAPLING)
            && pos.getY() < world.getHeight() - i - 1) {
          state.getBlock().onPlantGrow(state, world, pos.down(), pos);
          int k2 = 3;
          int l2 = 0;

          for (int i3 = pos.getY() - 3 + i; i3 <= pos.getY() + i; ++i3) {
            int i4 = i3 - (pos.getY() + i);
            int j1 = 1 - i4 / 2;

            for (int k1 = pos.getX() - j1; k1 <= pos.getX() + j1; ++k1) {
              int l1 = k1 - pos.getX();

              for (int i2 = pos.getZ() - j1; i2 <= pos.getZ() + j1; ++i2) {
                int j2 = i2 - pos.getZ();

                if (Math.abs(l1) != j1 || Math.abs(j2) != j1
                    || rand.nextInt(2) != 0 && i4 != 0) {
                  BlockPos blockpos = new BlockPos(k1, i3, i2);
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

          for (int j3 = 0; j3 < i; ++j3) {
            BlockPos upN = pos.up(j3);
            state = world.getBlockState(upN);

            if (state.getBlock().isAir(state, world, upN) || state.getBlock()
                .isLeaves(state, world, upN) || state.getMaterial() == Material.VINE) {
              this.setBlockAndNotifyAdequately(world, pos.up(j3), this.woodMeta);

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

          if (this.growVines) {
            for (int k3 = pos.getY() - 3 + i; k3 <= pos.getY() + i; ++k3) {
              int j4 = k3 - (pos.getY() + i);
              int k4 = 2 - j4 / 2;
              BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

              for (int l4 = pos.getX() - k4; l4 <= pos.getX() + k4; ++l4) {
                for (int i5 = pos.getZ() - k4; i5 <= pos.getZ() + k4; ++i5) {
                  blockpos$mutableblockpos1.setPos(l4, k3, i5);

                  state = world.getBlockState(blockpos$mutableblockpos1);
                  if (state.getBlock()
                      .isLeaves(state, world, blockpos$mutableblockpos1)) {
                    BlockPos blockpos2 = blockpos$mutableblockpos1.west();
                    BlockPos blockpos3 = blockpos$mutableblockpos1.east();
                    BlockPos blockpos4 = blockpos$mutableblockpos1.north();
                    BlockPos blockpos1 = blockpos$mutableblockpos1.south();

                    if (rand.nextInt(4) == 0 && world.isAirBlock(blockpos2)) {
                      this.addHangingVine(world, blockpos2, BlockVine.EAST);
                    }

                    if (rand.nextInt(4) == 0 && world.isAirBlock(blockpos3)) {
                      this.addHangingVine(world, blockpos3, BlockVine.WEST);
                    }

                    if (rand.nextInt(4) == 0 && world.isAirBlock(blockpos4)) {
                      this.addHangingVine(world, blockpos4, BlockVine.SOUTH);
                    }

                    if (rand.nextInt(4) == 0 && world.isAirBlock(blockpos1)) {
                      this.addHangingVine(world, blockpos1, BlockVine.NORTH);
                    }
                  }
                }
              }
            }

            if (rand.nextInt(5) == 0 && i > 5) {
              for (int l3 = 0; l3 < 2; ++l3) {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                  if (rand.nextInt(4 - l3) == 0) {
                    EnumFacing enumfacing1 = enumfacing.getOpposite();
                    this.placeBean(world, rand.nextInt(3),
                        pos.add(enumfacing1.getFrontOffsetX(), i - 5 + l3,
                            enumfacing1.getFrontOffsetZ()), enumfacing);
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
