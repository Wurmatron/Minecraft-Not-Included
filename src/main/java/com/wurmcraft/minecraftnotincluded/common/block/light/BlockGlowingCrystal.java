package com.wurmcraft.minecraftnotincluded.common.block.light;

import com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded;
import com.wurmcraft.minecraftnotincluded.common.item.MinecraftNotIncludedItems;
import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlowingCrystal extends BlockBush {

  public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

  public BlockGlowingCrystal() {
    super(Material.IRON);
    setCreativeTab(MinecraftNotIncluded.tabMNI);
    setHardness(3.5f);
    setLightOpacity(200);
    setResistance(3.5f);
    setHarvestLevel("pickaxe", 2);
    setSoundType(SoundType.METAL);
    setLightLevel(.9f);
    setDefaultState(blockState.getBaseState().withProperty(TYPE, Type.PINK));
  }

  @Override
  public boolean isPassable(IBlockAccess world, BlockPos pos) {
    return true;
  }

  @Override
  protected boolean canSustainBush(IBlockState state) {
    return state.getMaterial() != Material.AIR;
  }

  @Override
  public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
    for (Type type : Type.values()) {
      items.add(new ItemStack(this, 1, type.getMeta()));
    }
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return FULL_BLOCK_AABB;
  }

  @Override
  public ItemStack getPickBlock(
      IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    return new ItemStack(this, 1, state.getValue(TYPE).getMeta());
  }

  @Override
  public void getDrops(
      NonNullList<ItemStack> drops,
      IBlockAccess world,
      BlockPos pos,
      IBlockState state,
      int fortune) {
    Random rand = world instanceof World ? ((World) world).rand : RANDOM;
    int count = quantityDropped(state, fortune, rand);
    drops.add(new ItemStack(MinecraftNotIncludedItems.itemMeta, count, state.getValue(TYPE).meta));
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
  public int quantityDropped(Random random) {
    return 2 + random.nextInt(2);
  }

  @Override
  protected boolean canSilkHarvest() {
    return true;
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(TYPE, Type.values()[meta]);
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    return state.withProperty(TYPE, state.getValue(TYPE));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(TYPE).meta;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, TYPE);
  }

  public enum Type implements IStringSerializable {
    PINK("pink", 0),
    BLUE("blue", 1),
    RED("red", 2),
    ORANGE("orange", 3),
    GREEN("green", 4),
    WHITE("white", 5);

    private String name;
    private int meta;

    Type(String name, int crystalMeta) {
      this.name = name;
      this.meta = crystalMeta;
    }

    @Override
    public String getName() {
      return name;
    }

    public int getMeta() {
      return meta;
    }
  }
}
