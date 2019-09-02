package com.wurmcraft.minecraftnotincluded.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class GeyserBlock extends Block {

  public GeyserBlock(IBlockState state) {
    super(Material.IRON);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    setTranslationKey("geyser" + state.getBlock().getTranslationKey().substring(5));
  }

  @Override
  public int getLightValue(IBlockState state) {
    return 12;
  }
}
