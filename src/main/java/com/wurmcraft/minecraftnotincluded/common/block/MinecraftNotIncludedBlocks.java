package com.wurmcraft.minecraftnotincluded.common.block;

import com.wurmcraft.minecraftnotincluded.common.utils.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class MinecraftNotIncludedBlocks {

  public static Block geyserWater;

  public static Block blockDust;
  public static Block blockCompressedDust;

  public static void register() {
    Registry.registerBlock(
        geyserWater = new GeyserBlock(Blocks.WATER.getDefaultState()), "geyserWater");
    Registry.registerBlock(blockDust = new BasicBlock(Material.SAND), "dust");
    Registry.registerBlock(blockCompressedDust = new BasicBlock(Material.SAND), "compressedDust");
  }
}
