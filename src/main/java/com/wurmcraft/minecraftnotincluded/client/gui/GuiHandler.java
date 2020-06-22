package com.wurmcraft.minecraftnotincluded.client.gui;

import com.wurmcraft.minecraftnotincluded.client.gui.farm.ContainerFarmTile;
import com.wurmcraft.minecraftnotincluded.client.gui.farm.GuiFarmTile;
import com.wurmcraft.minecraftnotincluded.client.gui.farm.GuiHydroponicsTile;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityFarm;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityHydroponics;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

  public static final int FARM_TILE = 0;
  public static final int HYDROPONICS_TILE = 1;

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == FARM_TILE) {
      return new ContainerFarmTile(
          (TileEntityFarm) world.getTileEntity(new BlockPos(x, y, z)), player);
    } else if (ID == HYDROPONICS_TILE) {
      return new ContainerFarmTile(
          (TileEntityHydroponics) world.getTileEntity(new BlockPos(x, y, z)), player);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == FARM_TILE) {
      return new GuiFarmTile(
          new ContainerFarmTile(
              (TileEntityFarm) world.getTileEntity(new BlockPos(x, y, z)), player));
    } else if (ID == HYDROPONICS_TILE) {
      return new GuiHydroponicsTile(
          new ContainerFarmTile(
              (TileEntityHydroponics) world.getTileEntity(new BlockPos(x, y, z)), player));
    }
    return null;
  }
}
