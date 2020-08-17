package com.wurmcraft.minecraftnotincluded.common.utils;

import com.wurmcraft.minecraftnotincluded.common.references.Global;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Global.MODID)
public class Registry {

  public static List<Item> items = new ArrayList<>();
  public static List<Block> blocks = new ArrayList<>();
  public static HashMap<Block, Item> blockItems = new HashMap<>();

  public static void registerItem(Item item, String registryName) {
    item.setRegistryName(registryName);
    items.add(item);
  }

  public static Block registerBlock(Block block, String registryName) {
    block.setRegistryName(registryName);
    ItemBlock itemBlock = new ItemBlock(block);
    itemBlock.setRegistryName(registryName);
    blocks.add(block);
    blockItems.put(block, itemBlock);
    return block;
  }

  public static Block registerBlock(Block block, String registryName, boolean noItem) {
    if (!noItem) {
      return registerBlock(block, registryName);
    }
    block.setRegistryName(registryName);
    blocks.add(block);
    return block;
  }

  public static Block registerBlock(Block block, String registryName, ItemBlock itemBlock) {
    block.setRegistryName(registryName);
    itemBlock.setRegistryName(registryName);
    blocks.add(block);
    blockItems.put(block, itemBlock);
    return block;
  }

  @SubscribeEvent
  public void registerBlocks(RegistryEvent.Register<Block> e) {
    e.getRegistry().registerAll(blocks.toArray(new Block[0]));
  }

  @SubscribeEvent
  public void registerItems(RegistryEvent.Register<Item> e) {
    e.getRegistry().registerAll(items.toArray(new Item[0]));
    for (Block block : blockItems.keySet()) {
      e.getRegistry().register(blockItems.get(block));
    }
  }
}
