package com.wurmcraft.minecraftnotincluded.common.block;

import com.wurmcraft.minecraftnotincluded.api.GeyserData;
import com.wurmcraft.minecraftnotincluded.common.tile.TileGeyzer;
import com.wurmcraft.minecraftnotincluded.common.utils.GeyserRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GeyserBlock extends Block implements ITileEntityProvider {

  public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 15);

  public final int shift;

  public GeyserBlock(int shift) {
    super(Material.IRON);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    setTranslationKey("geyser");
    setHardness(-1);
    setResistance(-1);
    this.shift = shift;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    super.breakBlock(world, pos, state);
    world.removeTileEntity(pos);
  }

  @Override
  public boolean isTranslucent(IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    if (shift + meta > GeyserRegistry.getCount()) {
      return null;
    }
    GeyserData data = GeyserRegistry.getDataFromID(shift + meta);
    return new TileGeyzer(data.getBlock(), data.getTicksPerBlock(), 1);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(TYPE, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(TYPE);
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty(TYPE, state.getValue(TYPE));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, TYPE);
  }

  @Override
  public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
    for (int index = 0; index < 16; index++) {
      if (GeyserRegistry.getCount() > (shift + index)) {
        items.add(new ItemStack(this, 1, index));
      }
    }
  }
}
