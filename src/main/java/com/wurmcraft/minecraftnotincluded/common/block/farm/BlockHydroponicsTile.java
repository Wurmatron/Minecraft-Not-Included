package com.wurmcraft.minecraftnotincluded.common.block.farm;

import com.wurmcraft.minecraftnotincluded.client.gui.GuiHandler;
import com.wurmcraft.minecraftnotincluded.common.network.NetworkHandler;
import com.wurmcraft.minecraftnotincluded.common.network.packets.OpenGuiMessage;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityHydroponics;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHydroponicsTile extends BlockFarmTile {

  public BlockHydroponicsTile() {
    super();
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileEntityHydroponics();
  }

  @Override
  public boolean onBlockActivated(
      World world,
      BlockPos pos,
      IBlockState state,
      EntityPlayer player,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ) {
    if (!world.isRemote) {
      NetworkHandler.sendToServer(new OpenGuiMessage(GuiHandler.HYDROPONICS_TILE, pos));
    }
    return true;
  }
}
