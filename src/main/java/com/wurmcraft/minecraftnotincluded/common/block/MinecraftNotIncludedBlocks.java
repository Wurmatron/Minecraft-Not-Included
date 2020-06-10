package com.wurmcraft.minecraftnotincluded.common.block;

import com.wurmcraft.minecraftnotincluded.common.item.block.ItemGlowingMushroom;
import com.wurmcraft.minecraftnotincluded.common.tile.Type;
import com.wurmcraft.minecraftnotincluded.common.utils.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class MinecraftNotIncludedBlocks {

  // Geyser
  public static Block geyserWater;
  public static Block geyserLava;

  // Surface
  public static Block blockDust;
  public static Block blockCompressedDust;

  // Underground plants
  public static BlockGlowingMushroom glowingMushroom;

  public static void register() {
    // Geyser
    Registry.registerBlock(geyserWater = new GeyserBlock(Type.WATER), "geyserWater");
    Registry.registerBlock(geyserLava = new GeyserBlock(Type.LAVA), "geyserLava");
    // Surface
    Registry.registerBlock(blockDust = new BasicBlock(Material.SAND), "dust");
    Registry.registerBlock(blockCompressedDust = new BasicBlock(Material.SAND), "compressedDust");
    // Underground plants
    Registry.registerBlock(
        glowingMushroom = new BlockGlowingMushroom(),
        "glowingMushroom",
        new ItemGlowingMushroom(glowingMushroom));
  }
}
