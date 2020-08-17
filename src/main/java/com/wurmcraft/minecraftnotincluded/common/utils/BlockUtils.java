package com.wurmcraft.minecraftnotincluded.common.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockUtils {

  public static String getModID(String line) {
    if (line.contains(":")) {
      String[] spacer = line.split(":");
      return spacer[0];
    } else {
      return "";
    }
  }

  public static String getName(String line) {
    if (line.contains(":")) {
      String[] spacer = line.split(":");
      return spacer[1];
    } else {
      return line;
    }
  }

  public static int getMeta(String line) {
    if (line.contains(":")) {
      String[] spacer = line.split(":");
      if (spacer.length > 2) {
        try {
          return Integer.parseInt(spacer[2]);
        } catch (NumberFormatException e) {
          return -1;
        }
      } else {
        return -1;
      }
    } else {
      return -1;
    }
  }

  public static Block getBlock(String modid, String name) {
    return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modid, name));
  }

  public static IBlockState getState(String str) {
    Block block = getBlock(getModID(str), getName(str));
    int meta = getMeta(str);
    if (meta >= 0) {
      return block.getStateFromMeta(meta);
    } else {
      return block.getDefaultState();
    }
  }

  public static String stateToString(IBlockState state) {
    return state.getBlock().getRegistryName().getResourceDomain()
        + ":"
        + state.getBlock().getRegistryName().getResourcePath()
        + ":"
        + state.getBlock().getMetaFromState(state);
  }

  public static ItemStack getStackFromString(String item) {
    try {
      NBTTagCompound nbt = JsonToNBT.getTagFromJson(item);
      return new ItemStack(nbt);
    } catch (NBTException e) {
      e.printStackTrace();
    }
    return ItemStack.EMPTY;
  }
}
