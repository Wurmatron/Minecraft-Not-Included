package com.wurmcraft.minecraftnotincluded.common.item.block;

import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingFlower;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemGlowingFlower extends ItemBlock {

  public ItemGlowingFlower(Block block) {
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
        "item.glowingFlower_"
            + BlockGlowingFlower.Type.values()[stack.getItemDamage()].getName()
            + ".name");
  }
}
