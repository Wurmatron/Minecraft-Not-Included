package com.wurmcraft.minecraftnotincluded.common.block.farm.t1;

import com.wurmcraft.minecraftnotincluded.client.gui.GuiHandler;
import com.wurmcraft.minecraftnotincluded.common.network.NetworkHandler;
import com.wurmcraft.minecraftnotincluded.common.network.packets.OpenGuiMessage;
import com.wurmcraft.minecraftnotincluded.common.tile.TileControllerT1;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockController extends BlockCasing implements ITileEntityProvider {

  public BlockController() {
    super(Material.IRON);
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
      NetworkHandler.sendToServer(new OpenGuiMessage(GuiHandler.T1_MAIN, pos));
    }
    return true;
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileControllerT1();
  }
}
