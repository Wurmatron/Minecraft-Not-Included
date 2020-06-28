package com.wurmcraft.minecraftnotincluded.common.item.block;

import com.wurmcraft.minecraftnotincluded.api.GeyserData;
import com.wurmcraft.minecraftnotincluded.common.block.GeyserBlock;
import com.wurmcraft.minecraftnotincluded.common.utils.GeyserRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

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

  @Override
  public boolean placeBlockAt(
      ItemStack stack,
      EntityPlayer player,
      World world,
      BlockPos pos,
      EnumFacing side,
      float hitX,
      float hitY,
      float hitZ,
      IBlockState newState) {
    newState.withProperty(GeyserBlock.TYPE, stack.getItemDamage());
    return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
  }
}
