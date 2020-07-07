package com.wurmcraft.minecraftnotincluded.common.block.utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BasicBlock extends Block {

  public BasicBlock(Material material) {
    super(material);
    setLightLevel(1f);
  }
}
