package com.wurmcraft.minecraftnotincluded.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {

  public void preInit(FMLPreInitializationEvent e) {}

  public void init(FMLInitializationEvent e) {}

  public void postInit(FMLPostInitializationEvent e) {}

  public void serverStarting(FMLServerStartingEvent e) {}

  public IThreadListener getThreadListener(MessageContext context) {
    if (context.side.isServer()) {
      return context.getServerHandler().player.getServer();
    }
    return null;
  }

  public EntityPlayer getPlayer(MessageContext ctx) {
    if (ctx.side.isServer()) {
      return ctx.getServerHandler().player;
    }
    return null;
  }
}
