package com.wurmcraft.minecraftnotincluded.common.block.farm.t1;

import com.wurmcraft.minecraftnotincluded.client.gui.GuiHandler;
import com.wurmcraft.minecraftnotincluded.common.network.NetworkHandler;
import com.wurmcraft.minecraftnotincluded.common.network.packets.OpenGuiMessage;
import com.wurmcraft.minecraftnotincluded.common.tile.TileControllerT1;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityCasing;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockCasing extends Block {

  public BlockCasing(Material material) {
    super(material);
  }

  public BlockCasing() {
    super(Material.CIRCUITS);
  }

  @Override
  public void neighborChanged(
      IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
    super.neighborChanged(state, world, pos, block, fromPos);
    if (world.getTileEntity(pos) != null) {
      TileEntityCasing casing = (TileEntityCasing) world.getTileEntity(pos);
      if (casing != null && world.getTileEntity(casing.controllerPos) != null) {
        TileControllerT1 controllerT1 =
            (TileControllerT1) world.getTileEntity(casing.controllerPos);
        controllerT1.verifyAndUpdateStructure();
      }
    }
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    super.onBlockAdded(world, pos, state);
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
    if (world.getTileEntity(pos) != null) {
      TileEntityCasing casing = (TileEntityCasing) world.getTileEntity(pos);
      player.sendMessage(
          new TextComponentString(
              "Pos: "
                  + casing.controllerPos.getX()
                  + ", "
                  + casing.controllerPos.getY()
                  + ", "
                  + casing.controllerPos.getZ()));
    } else {
      player.sendMessage(new TextComponentString("Pos: Null"));
    }
    if (!world.isRemote && world.getTileEntity(pos) != null) {
      TileEntityCasing casing = (TileEntityCasing) world.getTileEntity(pos);
      NetworkHandler.sendToServer(new OpenGuiMessage(GuiHandler.T1_MAIN, casing.controllerPos));
    }
    return true;
  }
}
