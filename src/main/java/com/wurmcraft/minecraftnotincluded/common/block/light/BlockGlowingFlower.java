package com.wurmcraft.minecraftnotincluded.common.block.light;

import com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGlowingFlower extends BlockBush {

  public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

  public BlockGlowingFlower() {
    super(Material.PLANTS);
    setCreativeTab(MinecraftNotIncluded.tabMNI);
    setHardness(0);
    setLightOpacity(240);
    setResistance(1f);
    setLightLevel(1f);
    setDefaultState(blockState.getBaseState().withProperty(TYPE, Type.RED));
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
    for (BlockGlowingFlower.Type type : BlockGlowingFlower.Type.values()) {
      items.add(new ItemStack(this, 1, type.getMeta()));
    }
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
  public IBlockState getStateFromMeta(int meta) {
    return blockState.getBaseState().withProperty(TYPE, Type.values()[meta]);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, TYPE);
  }

  @Override
  public ItemStack getPickBlock(
      IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    return new ItemStack(this, 1, state.getValue(TYPE).getMeta());
  }

  public enum Type implements IStringSerializable {
    RED("red", 0),
    PINK("pink", 1),
    GREEN("green", 2),
    PURPLE("purple", 3);

    private String name;
    private int meta;

    Type(String name, int meta) {
      this.name = name;
      this.meta = meta;
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
