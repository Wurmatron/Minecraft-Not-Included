package com.wurmcraft.minecraftnotincluded.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileGeyzer extends TileEntity implements ITickable {

  public static final int UPDATE_TIMER = 1;
  public static final int SLEEP_TIME = 60;
  public static final int MAX_RADIUS = 6;
  public static final int MAX_HEIGHT = 4;

  // NBT Saved
  public Type type;
  public int speed;
  private int nextSleep;
  // Temp Var's
  public int currentRadius = 0;
  private BlockPos currentPos;
  private BlockPos lastPos;
  private int sleepLeft = 0;

  public TileGeyzer(Type type) {
    this.type = type;
    this.speed = 10;
    this.nextSleep = SLEEP_TIME;
    this.sleepLeft = SLEEP_TIME;
  }

  @Override
  public void update() {
    if (!world.isRemote && world.getWorldTime() % UPDATE_TIMER == 0) {
      if (nextSleep != 0) {
        nextSleep--;
        if (currentPos == null) {
          currentPos = new BlockPos(0, 1, 0);
          if (lastPos == null) {
            lastPos = currentPos;
          }
        }
        if (currentRadius > MAX_RADIUS) {
          if (currentPos.getY() < MAX_HEIGHT) {
            currentPos = new BlockPos(0, currentPos.getY() + 1, 0);
            currentRadius = 0;
          }
        } else {
          currentPos = getNextPlacement(currentPos);
          lastPos = currentPos;
          if (world.isAirBlock(getPos().add(currentPos))) {
            world.setBlockState(getPos().add(currentPos), type.getBlock(), 2);
          } else if (!world.getBlockState(getPos().add(currentPos)).equals(type.getBlock())) {
            if (currentPos.getY() < MAX_HEIGHT) {
              currentPos = new BlockPos(0, currentPos.getY() + 1, 0);
              currentRadius = 0;
            }
          }
        }
      } else {
        sleepLeft--;
        if (sleepLeft == 0) {
          nextSleep = SLEEP_TIME;
          sleepLeft = SLEEP_TIME;
        }
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    try {
      type = Type.valueOf(nbt.getString("type"));
    } catch (Exception e) {
      type = Type.values()[0];
    }
    speed = nbt.getInteger("speed");
    nextSleep = nbt.getInteger("sleep");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    nbt.setString("type", type.name());
    nbt.setInteger("speed", speed);
    nbt.setInteger("sleep", nextSleep);
    return nbt;
  }

  private BlockPos getNextPlacement(BlockPos currentPos) {
    if (currentRadius == 0) {
      currentRadius++;
      return currentPos;
    }
    if (currentRadius > 0) {
      if (currentPos.getZ() < currentRadius
          && currentPos.getX() < currentRadius
          && currentPos.getX() != -currentRadius
          && currentPos.getZ() != -currentRadius) { // UP
        return currentPos.add(0, 0, 1);
      }
      if (currentPos.getZ() == currentRadius
          && -currentPos.getX() < currentRadius
          && currentPos.getX() != currentRadius) { // RIGHT
        return currentPos.add(-1, 0, 0);
      }
      if (-currentPos.getZ() < currentRadius
          && currentPos.getX() == -currentRadius
          && -currentPos.getZ() != currentRadius) { // DOWN
        return currentPos.add(0, 0, -1);
      }
      if (-currentPos.getZ() == currentRadius && currentPos.getX() < currentRadius) { // LEFT
        return currentPos.add(1, 0, 0);
      }
      if (currentPos.getX() == currentRadius && currentPos.getZ() < currentRadius) {
        return currentPos.add(0, 0, 1);
      }
    }
    if (currentPos.getX() == currentRadius && currentPos.getZ() == currentRadius) {
      currentRadius++;
    }
    return currentPos;
  }
}
