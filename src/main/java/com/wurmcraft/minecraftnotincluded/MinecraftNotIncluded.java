package com.wurmcraft.minecraftnotincluded;

import com.wurmcraft.minecraftnotincluded.common.CommonProxy;
import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import com.wurmcraft.minecraftnotincluded.common.item.MinecraftNotIncludedItems;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Global.MODID, name = Global.NAME, version = Global.VERSION, dependencies = Global.DEPENDENCIES)
public class MinecraftNotIncluded {

  @SidedProxy(clientSide = Global.CLIENT_PROXY, serverSide = Global.COMMON_PROXY)
  public static CommonProxy proxy;

  @Instance(value = Global.MODID)
  public static MinecraftNotIncluded instance;

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    proxy.preInit(e);
    MinecraftNotIncludedItems.register();
    MinecraftNotIncludedBlocks.register();
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    proxy.init(e);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e) {
    proxy.postInit(e);
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    proxy.serverStarting(e);
  }
}
