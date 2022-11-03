package com.wurmcraft.minecraftnotincluded.common.block.light;

import net.minecraft.block.Block;
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
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlowingMushroom extends Block {

  protected static final AxisAlignedBB MUSHROOM_AABB =
      new AxisAlignedBB(
          0.30000001192092896D,
          0.0D,
          0.30000001192092896D,
          0.699999988079071D,
          0.4000000059604645D,
          0.699999988079071D);

  public static int BIOME_VARIATION_RARITY = 3;

  public static final PropertyEnum<BlockGlowingMushroom.EnumType> VARIANT =
      PropertyEnum.create("variant", BlockGlowingMushroom.EnumType.class);

  public BlockGlowingMushroom() {
    super(Material.PLANTS);
    setCreativeTab(CreativeTabs.BREWING);
    setLightLevel(1f);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumType.BROWN));
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return MUSHROOM_AABB;
  }

  public AxisAlignedBB getCollisionBoundingBox(
      IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  public int damageDropped(IBlockState state) {
    return state.getValue(VARIANT).getMetadata();
  }

  @Override
  public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
    for (BlockGlowingMushroom.EnumType type : BlockGlowingMushroom.EnumType.values()) {
      items.add(new ItemStack(this, 1, type.getMetadata()));
    }
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState()
        .withProperty(VARIANT, BlockGlowingMushroom.EnumType.byMetadata(meta));
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
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(VARIANT).getMetadata();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, VARIANT);
  }

  public IBlockState getMushroomForBiome(World world, Biome biome) {
    EnumType typeForBiome = getTypeForRainfall(biome.getRainfall());
    if (world.rand.nextInt(BIOME_VARIATION_RARITY) == 0) {
      return getStateFromMeta(world.rand.nextInt(EnumType.values().length - 1));
    } else {
      return getStateFromMeta(typeForBiome.meta);
    }
  }

  @Override
  public ItemStack getPickBlock(
      IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    return new ItemStack(this, 1, state.getValue(VARIANT).getMetadata());
  }

  private static EnumType getTypeForRainfall(float rainfall) {
    if (rainfall < .3) {
      return EnumType.GOLD;
    } else if (rainfall < .5) {
      return EnumType.BLUE;
    } else if (rainfall < .75) {
      return EnumType.RED;
    } else {
      return EnumType.BROWN;
    }
  }

  public enum EnumType implements IStringSerializable {
    BROWN(0, "brown"),
    RED(1, "red"),
    GOLD(2, "gold"),
    BLUE(3, "blue");

    private static final BlockGlowingMushroom.EnumType[] META_LOOKUP =
        new BlockGlowingMushroom.EnumType[values().length];
    private final int meta;
    private final String name;

    EnumType(int meta, String name) {
      this.meta = meta;
      this.name = name;
    }

    public int getMetadata() {
      return this.meta;
    }

    public String toString() {
      return this.name;
    }

    public static BlockGlowingMushroom.EnumType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

    public String getName() {
      return this.name;
    }

    static {
      for (BlockGlowingMushroom.EnumType type : values()) {
        META_LOOKUP[type.getMetadata()] = type;
      }
    }
  }
}
