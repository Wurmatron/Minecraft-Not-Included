package com.wurmcraft.minecraftnotincluded.common.block;

import com.wurmcraft.minecraftnotincluded.common.utils.Registry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class MinecraftNotIncludedBlocks {

  public static Block geyserWater;

  public static void register() {
    Registry.registerBlock(
        geyserWater = new GeyserBlock(Blocks.WATER.getDefaultState()), "geyserWater");
  }
}
