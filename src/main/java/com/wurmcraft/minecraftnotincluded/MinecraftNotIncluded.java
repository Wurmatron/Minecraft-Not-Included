package com.wurmcraft.minecraftnotincluded;

import com.wurmcraft.minecraftnotincluded.common.CommonProxy;
import com.wurmcraft.minecraftnotincluded.common.ConfigHandler;
import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import com.wurmcraft.minecraftnotincluded.common.event.SurfaceRadiationEvent;
import com.wurmcraft.minecraftnotincluded.common.gen.MNIWorldType;
import com.wurmcraft.minecraftnotincluded.common.item.MinecraftNotIncludedItems;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import com.wurmcraft.minecraftnotincluded.common.utils.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
  modid = Global.MODID,
  name = Global.NAME,
  version = Global.VERSION,
  dependencies = Global.DEPENDENCIES
)
public class MinecraftNotIncluded {

  @SidedProxy(clientSide = Global.CLIENT_PROXY, serverSide = Global.COMMON_PROXY)
  public static CommonProxy proxy;

  @Instance(value = Global.MODID)
  public static MinecraftNotIncluded instance;

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    proxy.preInit(e);
    MinecraftForge.EVENT_BUS.register(new Registry());
    MinecraftNotIncludedItems.register();
    MinecraftNotIncludedBlocks.register();
    new MNIWorldType();
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    proxy.init(e);
    // TODO Add Protection Items
    if (ConfigHandler.radiationDamage && ConfigHandler.radiationDamagePerSecond > 0) {
      MinecraftForge.EVENT_BUS.register(new SurfaceRadiationEvent());
    }
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e) {
    proxy.postInit(e);
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    proxy.serverStarting(e);
  }

  //  @SubscribeEvent
  //  public void onCubeGen(PopulateCubeEvent.Pre e) {
  //    CubePrimer cube = (e.getGenerator()).generateCube(e.getCubeX(), e.getCubeY(), e.getCubeZ());
  //    for (int x = 0; x < Cube.SIZE; x++)
  //      for (int y = 0; y < Cube.SIZE; y++)
  //        for (int z = 0; z < Cube.SIZE; z++) {
  //          cube.setBlockState(x, y, z, Blocks.OBSIDIAN.getDefaultState());
  //        }
  //  }
}
