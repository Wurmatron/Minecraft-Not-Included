package com.wurmcraft.minecraftnotincluded.common.block;

import com.wurmcraft.minecraftnotincluded.common.tile.TileGeyzer;
import com.wurmcraft.minecraftnotincluded.common.tile.Type;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GeyserBlock extends Block implements ITileEntityProvider {

  public Type type;

  public GeyserBlock(Type state) {
    super(Material.IRON);
    this.type = state;
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    setTranslationKey("geyser" + state.getBlock().getBlock().getTranslationKey().substring(5));
    setHardness(-1);
    setResistance(-1);
  }
  //
  //  @Override
  //  public int getLightValue(IBlockState state) {
  //    return 12;
  //  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    super.breakBlock(world, pos, state);
    world.removeTileEntity(pos);
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileGeyzer(type);
  }
}
