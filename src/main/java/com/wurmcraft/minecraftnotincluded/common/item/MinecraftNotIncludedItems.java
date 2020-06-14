package com.wurmcraft.minecraftnotincluded.common.item;

import com.wurmcraft.minecraftnotincluded.common.utils.Registry;

public class MinecraftNotIncludedItems {

  public static final String[] META_ITEMS =
      new String[] {
        "pinkCrystal", "blueCrystal", "redCrystal", "orangeCrystal", "greenCrystal", "whiteCrystal"
      };

  public static ItemMeta itemMeta;

  public static void register() {
    Registry.registerItem(itemMeta = new ItemMeta(META_ITEMS), "itemMeta");
  }
}
