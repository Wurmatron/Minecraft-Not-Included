package com.wurmcraft.minecraftnotincluded.common.item.block;

import com.wurmcraft.minecraftnotincluded.api.GeyserData;
import com.wurmcraft.minecraftnotincluded.common.utils.GeyserRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemBlockGeyser extends ItemBlock {

  private final int shift;

  public ItemBlockGeyser(Block block, int shift) {
    super(block);
    this.shift = shift;
    setHasSubtypes(true);
  }

  @Override
  public int getMetadata(int damage) {
    return damage;
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {
    GeyserData data = GeyserRegistry.getDataFromID(shift + stack.getItemDamage());
    if (data != null) {
      return I18n.translateToLocal(
          "tile.geyser" + data.getBlock().getBlock().getRegistryName().getPath() + ".name");
    } else return "tile.null.name";
  }
}
