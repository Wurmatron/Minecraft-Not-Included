package com.wurmcraft.minecraftnotincluded.common.block.generation;

import com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockLargeVine extends Block {

  public BlockLargeVine() {
    super(Material.VINE);
    setLightLevel(.5f);
    setCreativeTab(MinecraftNotIncluded.tabMNI);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.STICK;
  }

  @Override
  public int quantityDropped(Random random) {
    return 3 + random.nextInt(4);
  }
}
