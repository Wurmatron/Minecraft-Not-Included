package com.wurmcraft.minecraftnotincluded.common.block.farm;

import com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded;
import com.wurmcraft.minecraftnotincluded.common.gui.GuiHandler;
import com.wurmcraft.minecraftnotincluded.common.network.NetworkHandler;
import com.wurmcraft.minecraftnotincluded.common.network.packets.OpenGuiMessage;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityFarm;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFarmTile extends BlockContainer {

  protected static final AxisAlignedBB FARM_AABB = new AxisAlignedBB(0, 0, 0, 1, 1.2, 1);

  public static final PropertyEnum<Facing> FACING = PropertyEnum.create("facing", Facing.class);

  public BlockFarmTile() {
    super(Material.ANVIL);
    setCreativeTab(MinecraftNotIncluded.tabMNI);
    setHardness(5f);
    setResistance(10f);
    setLightLevel(1f);
    setDefaultState(blockState.getBaseState().withProperty(FACING, Facing.UP));
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileEntityFarm();
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return FARM_AABB;
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
      NetworkHandler.sendToServer(new OpenGuiMessage(GuiHandler.FARM_TILE, pos));
    }
    return true;
  }

  @Override
  public IBlockState getStateForPlacement(
      World worldIn,
      BlockPos pos,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      EntityLivingBase placer) {
    if (facing == EnumFacing.UP) {
      return blockState.getBaseState().withProperty(FACING, Facing.DOWN);
    } else {
      return blockState.getBaseState().withProperty(FACING, Facing.UP);
    }
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    return state.withProperty(FACING, state.getValue(FACING));
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return blockState.getBaseState().withProperty(FACING, Facing.values()[meta]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).meta;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public ItemStack getPickBlock(
      IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    return new ItemStack(this, 1, 0);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean isFullBlock(IBlockState state) {
    return false;
  }

  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }

  public enum Facing implements IStringSerializable {
    UP("up", 0),
    DOWN("down", 1);

    public String name;
    public int meta;

    Facing(String name, int meta) {
      this.meta = meta;
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }
  }
}
