package com.wurmcraft.minecraftnotincluded.common.item.block;

import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystal;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemGlowingCrystal extends ItemBlock {

  public ItemGlowingCrystal(Block block) {
    super(block);
    setMaxDamage(0);
    setHasSubtypes(true);
  }

  @Override
  public int getMetadata(int damage) {
    return damage;
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {
    return I18n.translateToLocal(
        "item.glowingCrystal_"
            + BlockGlowingCrystal.Type.values()[stack.getItemDamage()].getName()
            + ".name");
  }
}
