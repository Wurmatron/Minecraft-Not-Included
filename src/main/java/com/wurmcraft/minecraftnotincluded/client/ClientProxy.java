package com.wurmcraft.minecraftnotincluded.client;

import com.wurmcraft.minecraftnotincluded.common.CommonProxy;
import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystal;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingFlower;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingMushroom;
import com.wurmcraft.minecraftnotincluded.common.item.MinecraftNotIncludedItems;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {

  @Override
  public void preInit(FMLPreInitializationEvent e) {
    super.preInit(e);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void init(FMLInitializationEvent e) {
    super.init(e);
  }

  @Override
  public void postInit(FMLPostInitializationEvent e) {
    super.postInit(e);
  }

  @Override
  public void serverStarting(FMLServerStartingEvent e) {
    super.serverStarting(e);
  }

  private static void createModel(Item item, int meta, String name) {
    ModelLoader.setCustomModelResourceLocation(
        item, meta, new ModelResourceLocation(Global.MODID + ":" + name, "inventory"));
  }

  @Override
  public IThreadListener getThreadListener(MessageContext ctx) {
    if (ctx.side.isClient()) {
      return Minecraft.getMinecraft();
    }
    return null;
  }

  @Override
  public EntityPlayer getPlayer(MessageContext ctx) {
    if (ctx.side.isClient()) {
      return Minecraft.getMinecraft().player;
    } else {
      return null;
    }
  }

  @SubscribeEvent
  public void loadModel(ModelRegistryEvent e) {
    for (BlockGlowingMushroom.EnumType type : BlockGlowingMushroom.EnumType.values()) {
      createModel(
          Item.getItemFromBlock(MinecraftNotIncludedBlocks.glowingMushroom),
          type.getMetadata(),
          "glowingmushroom_" + type.getName());
    }
    for (BlockGlowingCrystal.Type type : BlockGlowingCrystal.Type.values()) {
      createModel(
          Item.getItemFromBlock(MinecraftNotIncludedBlocks.glowingCrystal),
          type.getMeta(),
          "glowingcrystal_" + type.getName());
    }
    for (BlockGlowingFlower.Type type : BlockGlowingFlower.Type.values()) {
      createModel(
          Item.getItemFromBlock(MinecraftNotIncludedBlocks.glowingFlower),
          type.getMeta(),
          "glowingflower_" + type.getName());
    }
    for (int index = 0; index < MinecraftNotIncludedItems.META_ITEMS.length; index++) {
      createModel(
          MinecraftNotIncludedItems.itemMeta, index, MinecraftNotIncludedItems.META_ITEMS[index]);
    }
    createModel(Item.getItemFromBlock(MinecraftNotIncludedBlocks.glowingVines), 0, "glowingvines");
    createModel(Item.getItemFromBlock(MinecraftNotIncludedBlocks.blockDust), 0, "dust");
    createModel(
        Item.getItemFromBlock(MinecraftNotIncludedBlocks.blockCompressedDust),
        0,
        "compressed_dust");
  }
}
