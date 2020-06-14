package com.wurmcraft.minecraftnotincluded.common.block.light;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGlowingDoublePlant extends BlockBush {

  public static final PropertyEnum<EnumPlantType> VARIANT =
      PropertyEnum.create("variant", BlockGlowingDoublePlant.EnumPlantType.class);
  public static final PropertyEnum<BlockGlowingDoublePlant.EnumBlockHalf> HALF =
      PropertyEnum.create("half", BlockGlowingDoublePlant.EnumBlockHalf.class);
  public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;

  public BlockGlowingDoublePlant() {
    super(Material.PLANTS);
    setCreativeTab(CreativeTabs.BREWING);
    setHardness(.5f);
    setLightOpacity(150);
    setResistance(1f);
    setLightLevel(1f);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumPlantType.Hibiscus));
  }

  @Override
  public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
    if (state.getBlock() == this) {
      IBlockState base =
          (world.getBlockState(pos.up()).getBlock() == this)
              ? world.getBlockState(pos.up().up())
              : world.getBlockState(pos.up());
      return canSustainBush(base);
    }
    return this.canSustainBush(world.getBlockState(pos.up()));
  }

  @Override
  protected boolean canSustainBush(IBlockState state) {
    return state.getBlock() == this && state.getValue(HALF) != EnumBlockHalf.LOWER
        || state.getMaterial() != Material.AIR
            && state.getMaterial() != Material.LEAVES
            && state.getBlock() != this;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    if (state.getValue(HALF) == BlockGlowingDoublePlant.EnumBlockHalf.UPPER) {
      return Items.AIR;
    } else {
      BlockGlowingDoublePlant.EnumPlantType plantType = state.getValue(VARIANT);
      if (plantType == EnumPlantType.Hibiscus) {
        return rand.nextInt(8) == 0 ? Items.POISONOUS_POTATO : Items.AIR;
      } else {
        return super.getItemDropped(state, rand, fortune);
      }
    }
  }

  @Override
  public int damageDropped(IBlockState state) {
    return 0;
  }

  public void placeAt(
      World world, BlockPos lowerPos, BlockGlowingDoublePlant.EnumPlantType variant, int flags) {
    world.setBlockState(
        lowerPos,
        getDefaultState()
            .withProperty(HALF, BlockGlowingDoublePlant.EnumBlockHalf.LOWER)
            .withProperty(VARIANT, variant),
        flags);
    world.setBlockState(
        lowerPos.up(),
        getDefaultState().withProperty(HALF, BlockGlowingDoublePlant.EnumBlockHalf.UPPER),
        flags);
  }

  @Override
  public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (state.getValue(HALF) == BlockGlowingDoublePlant.EnumBlockHalf.UPPER) {
      if (world.getBlockState(pos.down()).getBlock() == this) {
        world.setBlockToAir(pos.down());
      }
    } else if (world.getBlockState(pos.up()).getBlock() == this) {
      world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
    }
    super.onBlockHarvested(world, pos, state, player);
  }

  public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
    for (BlockGlowingDoublePlant.EnumPlantType type :
        BlockGlowingDoublePlant.EnumPlantType.values()) {
      items.add(new ItemStack(this, 1, type.getMeta()));
    }
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return (meta & 8) > 0
        ? getDefaultState().withProperty(HALF, BlockGlowingDoublePlant.EnumBlockHalf.UPPER)
        : getDefaultState()
            .withProperty(HALF, BlockGlowingDoublePlant.EnumBlockHalf.LOWER)
            .withProperty(VARIANT, BlockGlowingDoublePlant.EnumPlantType.byMetadata(meta & 7));
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    if (state.getValue(HALF) == BlockGlowingDoublePlant.EnumBlockHalf.UPPER) {
      IBlockState iblockstate = world.getBlockState(pos.down());

      if (iblockstate.getBlock() == this) {
        state = state.withProperty(VARIANT, iblockstate.getValue(VARIANT));
      }
    }

    return state;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(HALF) == BlockGlowingDoublePlant.EnumBlockHalf.UPPER
        ? 8 | (state.getValue(FACING)).getHorizontalIndex()
        : (state.getValue(VARIANT)).getMeta();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] {HALF, VARIANT, FACING});
  }

  public static enum EnumBlockHalf implements IStringSerializable {
    UPPER,
    LOWER;

    public String toString() {
      return this.getName();
    }

    public String getName() {
      return this == UPPER ? "upper" : "lower";
    }
  }

  public enum EnumPlantType implements IStringSerializable {
    Hibiscus(0, "hibiscus"),
    CONE_FLOWER(1, "cone_flower");

    private static final BlockGlowingDoublePlant.EnumPlantType[] META_LOOKUP =
        new BlockGlowingDoublePlant.EnumPlantType[values().length];

    private final int meta;
    private final String name;

    EnumPlantType(int meta, String name) {
      this.meta = meta;
      this.name = name;
    }

    public int getMeta() {
      return meta;
    }

    public String toString() {
      return name;
    }

    public static BlockGlowingDoublePlant.EnumPlantType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

    public String getName() {
      return name;
    }

    static {
      for (BlockGlowingDoublePlant.EnumPlantType type : values()) {
        META_LOOKUP[type.getMeta()] = type;
      }
    }
  }
}
