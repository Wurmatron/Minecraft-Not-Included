package com.wurmcraft.minecraftnotincluded.common.block;

import com.wurmcraft.minecraftnotincluded.common.tile.Type;
import com.wurmcraft.minecraftnotincluded.common.utils.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;

public class MinecraftNotIncludedBlocks {

  public static Block geyserWater;
  public static Block geyserLava;

  public static Block blockDust;
  public static Block blockCompressedDust;

  public static void register() {
    Registry.registerBlock(geyserWater = new GeyserBlock(Type.WATER), "geyserWater");
    Registry.registerBlock(geyserLava = new GeyserBlock(Type.LAVA), "geyserLava");
    Registry.registerBlock(blockDust = new BasicBlock(Material.SAND), "dust");
    Registry.registerBlock(blockCompressedDust = new BasicBlock(Material.SAND), "compressedDust");
  }
}
