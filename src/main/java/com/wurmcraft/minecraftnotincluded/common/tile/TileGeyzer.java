package com.wurmcraft.minecraftnotincluded.common.tile;

import com.wurmcraft.minecraftnotincluded.api.GeyserData;
import com.wurmcraft.minecraftnotincluded.common.utils.BlockUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileGeyzer extends TileEntity implements ITickable {

  // Config
  private static final int TIME_BETWEEN_CHECK = 20;
  private static final int MAX_RADIUS = 3;
  private static final int MAX_HEIGHT = 2;

  // NBT
  private IBlockState fluid;
  private int ticksPerBlock;
  private double frackingMultiplier;
  private int ticksLeftTillBlockPlacement;

  // Dynamic
  private GeyserData geyserData;

  public TileGeyzer(GeyserData data) {
    this.geyserData = data;
    this.fluid = data.getBlock();
    this.ticksPerBlock = data.getTicksPerBlock();
    this.frackingMultiplier = 1;
    ticksLeftTillBlockPlacement = data.getTicksPerBlock();
  }

  public TileGeyzer(IBlockState fluid, int ticksPerBlock, double frackingMultiplier) {
    this.fluid = fluid;
    this.ticksPerBlock = ticksPerBlock;
    this.frackingMultiplier = frackingMultiplier;
    this.ticksLeftTillBlockPlacement = ticksPerBlock;
  }

  @Override
  public void update() {
    if (getWorld().getWorldTime() % TIME_BETWEEN_CHECK == 0) {
      if (ticksLeftTillBlockPlacement <= 0) {
        placeNextBlock();
        ticksLeftTillBlockPlacement =
            ticksPerBlock - (int) (ticksPerBlock * (1 - frackingMultiplier));
      } else {
        ticksLeftTillBlockPlacement -= TIME_BETWEEN_CHECK;
      }
    }
  }

  private void placeNextBlock() {
    if (canReplaceBlock(world.getBlockState(pos.add(0, 1, 0)))) {
      world.setBlockState(pos.add(0, 1, 0), fluid, 2);
      return;
    } else if (canReplaceBlock(world.getBlockState(pos.add(0, 2, 0)))) {
      world.setBlockState(pos.add(0, 2, 0), fluid, 2);
      return;
    } else {
      for (int currentHeight = 0; currentHeight <= MAX_HEIGHT; currentHeight++) {
        for (int currentRadius = 1; currentRadius <= MAX_RADIUS; currentRadius++) {
          boolean isValid = isValidExpand(currentRadius, currentHeight);
          if (isValid) {
            BlockPos nextLocation = findNextLocation(currentRadius, currentHeight);
            if (nextLocation != null) {
              world.setBlockState(nextLocation, fluid, 2);
              return;
            }
          }
        }
      }
    }
  }

  private boolean isValidExpand(int currentRadius, int height) {
    if (currentRadius > 0) {
      BlockPos nextLocation = findNextLocation(currentRadius, height);
      return nextLocation != null;
    } else {
      return canReplaceBlock(world.getBlockState(pos.add(0, height, 0)));
    }
  }

  private BlockPos findNextLocation(int currentRadius, int height) {
    for (int x = 0; x < currentRadius; x++) {
      for (int z = 0; z < currentRadius; z++) {
        for (int flip = 0; flip <= 1; flip++) {
          if (canReplaceBlock(world.getBlockState(pos.add(flip == 1 ? x : -x, height, z)))) {
            return pos.add(flip == 1 ? x : -x, height, z);
          } else if (canReplaceBlock(world.getBlockState(pos.add(x, height, flip == 1 ? z : -z)))) {
            return pos.add(x, height, flip == 1 ? z : -z);
          } else if (canReplaceBlock(
              world.getBlockState(pos.add(flip == 1 ? x : -x, height, flip == 1 ? z : -z)))) {
            return pos.add(flip == 1 ? x : -x, height, flip == 1 ? z : -z);
          }
        }
      }
    }
    return null;
  }

  private boolean canReplaceBlock(IBlockState state) {
    return state.getMaterial().equals(Material.AIR);
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    fluid = BlockUtils.getState(nbt.getString("fluid"));
    ticksPerBlock = nbt.getInteger("ticksPerBlock");
    frackingMultiplier = nbt.getDouble("frackingMultiplier");
    ticksLeftTillBlockPlacement = nbt.getInteger("timeLeft");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setString("fluid", BlockUtils.stateToString(fluid));
    nbt.setInteger("ticksPerBlock", ticksPerBlock);
    nbt.setDouble("frackingMultiplier", frackingMultiplier);
    nbt.setInteger("timeLeft,", ticksLeftTillBlockPlacement);
    return nbt;
  }
}
