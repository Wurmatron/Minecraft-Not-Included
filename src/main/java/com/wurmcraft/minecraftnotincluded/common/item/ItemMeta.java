package com.wurmcraft.minecraftnotincluded.common.item;

import com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;

public class ItemMeta extends Item {

  private final String[] metaItems;

  public ItemMeta(String[] metaItems) {
    this.metaItems = metaItems;
    setCreativeTab(MinecraftNotIncluded.tabMNI);
    setHasSubtypes(true);
    setMaxDamage(0);
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
    for (int meta = 0; meta < metaItems.length; meta++) {
      items.add(new ItemStack(this, 1, meta));
    }
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {
    if (stack.getItemDamage() < metaItems.length) {
      return I18n.translateToLocal(
          "item." + metaItems[stack.getItemDamage()].replaceAll("&", "\u00a7") + ".name");
    }
    return I18n.translateToLocal("item.invalid.name");
  }
}
