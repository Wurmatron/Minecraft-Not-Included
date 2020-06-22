package com.wurmcraft.minecraftnotincluded.common.item.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFarmTile extends ItemBlock {

  public ItemFarmTile(Block block) {
    super(block);
    setMaxDamage(0);
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<String> tip, ITooltipFlag flagIn) {
    tip.add(TextFormatting.GRAY + I18n.translateToLocal("tooltip.noAutomation.name"));
  }
}
