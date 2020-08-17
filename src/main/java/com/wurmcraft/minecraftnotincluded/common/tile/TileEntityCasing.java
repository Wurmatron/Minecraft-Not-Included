package com.wurmcraft.minecraftnotincluded.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityCasing extends TileEntity {

  public BlockPos controllerPos;

  public TileEntityCasing() {
    this.controllerPos = null;
  }

  public TileEntityCasing(BlockPos controllerPos) {
    this.controllerPos = controllerPos;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    int[] posArr = nbt.getIntArray("controllerPos");
    controllerPos = new BlockPos(posArr[0], posArr[1], posArr[2]);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setIntArray(
        "controllerPos",
        new int[] {controllerPos.getX(), controllerPos.getY(), controllerPos.getZ()});
    return nbt;
  }
}
