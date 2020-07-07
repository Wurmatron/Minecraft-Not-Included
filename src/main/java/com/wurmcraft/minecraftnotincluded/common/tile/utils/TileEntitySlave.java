package com.wurmcraft.minecraftnotincluded.common.tile.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntitySlave extends TileEntity {

  protected BlockPos controllerPos;

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    int[] pos = nbt.getIntArray("controllerPos");
    controllerPos = new BlockPos(pos[0], pos[1], pos[2]);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setIntArray(
        "controllerPos",
        new int[] {controllerPos.getX(), controllerPos.getY(), controllerPos.getZ()});
    return nbt;
  }

  public boolean hasControllerPos() {
    return controllerPos != null;
  }

  public void updateController() {
    this.controllerPos = null;
    this.controllerPos = findControllerPos(5);
  }

  protected BlockPos findControllerPos(int checkAmount) {
    if (checkAmount == 0) return null;
    if (controllerPos == null) {
      for (EnumFacing dir : EnumFacing.values()) {
        TileEntitySlave slave = getTE(dir);
        if (slave != null) {
          this.controllerPos = slave.getControllerPos(checkAmount);
        }
      }
    }
    return controllerPos;
  }

  public BlockPos getControllerPos() {
    return getControllerPos(5);
  }

  protected BlockPos getControllerPos(int checkAmount) {
    if (controllerPos != null) {
      return controllerPos;
    } else {
      return findControllerPos(--checkAmount);
    }
  }

  protected TileEntitySlave getTE(EnumFacing facing) {
    if (facing == EnumFacing.DOWN && world.getTileEntity(pos.down()) instanceof TileEntitySlave) {
      return (TileEntitySlave) world.getTileEntity(pos.down());
    } else if (facing == EnumFacing.UP
        && world.getTileEntity(pos.up()) instanceof TileEntitySlave) {
      return (TileEntitySlave) world.getTileEntity(pos.up());
    } else if (facing == EnumFacing.NORTH
        && world.getTileEntity(pos.north()) instanceof TileEntitySlave) {
      return (TileEntitySlave) world.getTileEntity(pos.north());
    } else if (facing == EnumFacing.SOUTH
        && world.getTileEntity(pos.south()) instanceof TileEntitySlave) {
      return (TileEntitySlave) world.getTileEntity(pos.south());
    } else if (facing == EnumFacing.WEST
        && world.getTileEntity(pos.west()) instanceof TileEntitySlave) {
      return (TileEntitySlave) world.getTileEntity(pos.west());
    } else if (facing == EnumFacing.EAST
        && world.getTileEntity(pos.east()) instanceof TileEntitySlave) {
      return (TileEntitySlave) world.getTileEntity(pos.east());
    }
    return null;
  }
}
