package com.wurmcraft.minecraftnotincluded.common.block.utils;

import com.wurmcraft.minecraftnotincluded.common.tile.utils.TileEntitySlave;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockSlave extends BlockContainer {

  public BlockSlave(Material material) {
    super(material);
    hasTileEntity = true;
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileEntitySlave();
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    super.breakBlock(world, pos, state);
    world.removeTileEntity(pos);
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    super.onBlockAdded(world, pos, state);
    TileEntitySlave slave = (TileEntitySlave) world.getTileEntity(pos);
    slave.updateController();
  }

  @Override
  public void neighborChanged(
      IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
    super.neighborChanged(state, world, pos, block, fromPos);
    TileEntitySlave slave = (TileEntitySlave) world.getTileEntity(pos);
    slave.updateController();
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
    TileEntitySlave slave = (TileEntitySlave) world.getTileEntity(pos);
    player.sendMessage(new TextComponentString("Controller Pos: " + slave.getControllerPos()));
    return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
  }
}
