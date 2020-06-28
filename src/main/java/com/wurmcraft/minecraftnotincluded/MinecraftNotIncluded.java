package com.wurmcraft.minecraftnotincluded;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.minecraftnotincluded.client.gui.GuiHandler;
import com.wurmcraft.minecraftnotincluded.common.CommonProxy;
import com.wurmcraft.minecraftnotincluded.common.ConfigHandler;
import com.wurmcraft.minecraftnotincluded.common.ConfigHandler.Wasteland;
import com.wurmcraft.minecraftnotincluded.common.OreDict;
import com.wurmcraft.minecraftnotincluded.common.biome.BiomeRegistry;
import com.wurmcraft.minecraftnotincluded.common.block.MinecraftNotIncludedBlocks;
import com.wurmcraft.minecraftnotincluded.common.event.SurfaceRadiationEvent;
import com.wurmcraft.minecraftnotincluded.common.item.MinecraftNotIncludedItems;
import com.wurmcraft.minecraftnotincluded.common.network.NetworkHandler;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import com.wurmcraft.minecraftnotincluded.common.tile.TileGeyzer;
import com.wurmcraft.minecraftnotincluded.common.utils.FarmRegistry;
import com.wurmcraft.minecraftnotincluded.common.utils.GeyserRegistry;
import com.wurmcraft.minecraftnotincluded.common.utils.Registry;
import com.wurmcraft.minecraftnotincluded.common.world.MNIWorldType;
import java.io.File;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

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

  public static Logger logger;
  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  public static File oreConfig;

  public static CreativeTabs tabMNI =
      new CreativeTabs("tabMNI") {
        @Override
        public ItemStack createIcon() {
          return new ItemStack(MinecraftNotIncludedBlocks.glowingMushroom, 1, 2);
        }
      };

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    logger = e.getModLog();
    proxy.preInit(e);
    GeyserRegistry.loadAndSetup();
    MinecraftForge.EVENT_BUS.register(new Registry());
    MinecraftForge.EVENT_BUS.register(new BiomeRegistry());
    GameRegistry.registerTileEntity(TileGeyzer.class, new ResourceLocation(Global.MODID, "geyzer"));
    MinecraftNotIncludedItems.register();
    MinecraftNotIncludedBlocks.register();
    new MNIWorldType();
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    proxy.init(e);
    if (Wasteland.enabled) {
      logger.info("Wasteland has been enabled");
      // TODO Add Protection Items
      if (ConfigHandler.Wasteland.radiationDamage
          && ConfigHandler.Wasteland.radiationDamagePerSecond > 0) {
        MinecraftForge.EVENT_BUS.register(new SurfaceRadiationEvent());
      }
    }
    MinecraftForge.EVENT_BUS.register(new MinecraftNotIncluded());
    NetworkHandler.registerPackets();
    NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e) {
    proxy.postInit(e);
    BiomeRegistry.setup();
    if (ConfigHandler.enableOreDictEntries) {
      OreDict.register();
    }
    oreConfig =
        new File(
            Loader.instance().getConfigDir()
                + File.separator
                + Global.NAME.replaceAll(" ", "_")
                + File.separator
                + "ore.json");
    if (ConfigHandler.replaceDefaultGenerator) {
      MNIWorldType.replaceDefaultGenerator();
    }
    FarmRegistry.loadAndSetup();
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    proxy.serverStarting(e);
  }
}
