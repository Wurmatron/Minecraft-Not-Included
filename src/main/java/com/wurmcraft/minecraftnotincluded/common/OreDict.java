package com.wurmcraft.minecraftnotincluded.common;

import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystal;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingMushroom;
import com.wurmcraft.minecraftnotincluded.common.item.MinecraftNotIncludedItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDict {

  public static void register() {
    registerBlocks();
  }

  private static void registerBlocks() {
    for (BlockGlowingMushroom.EnumType mr : BlockGlowingMushroom.EnumType.values()) {
      OreDictionary.registerOre(
          "mushroom",
          new ItemStack(
              Item.getItemFromBlock(MinecraftNotIncludedBlocks.glowingMushroom),
              1,
              mr.getMetadata()));
      OreDictionary.registerOre(
          "listAllMushroom",
          new ItemStack(
              Item.getItemFromBlock(MinecraftNotIncludedBlocks.glowingMushroom),
              1,
              mr.getMetadata()));
    }
    OreDictionary.registerOre("vine", MinecraftNotIncludedBlocks.glowingVines);
    for (BlockGlowingCrystal.Type cry : BlockGlowingCrystal.Type.values())
      OreDictionary.registerOre(
          "crystal", new ItemStack(MinecraftNotIncludedItems.itemMeta, 0, cry.getMeta()));
  }
}
