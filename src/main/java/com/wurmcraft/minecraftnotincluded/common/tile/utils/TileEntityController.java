package com.wurmcraft.minecraftnotincluded.common.tile.utils;

import net.minecraft.util.ITickable;

public class TileEntityController extends TileEntitySlave implements ITickable {

  @Override
  public void update() {
    if (this.controllerPos == null) this.controllerPos = getPos();
  }
}
