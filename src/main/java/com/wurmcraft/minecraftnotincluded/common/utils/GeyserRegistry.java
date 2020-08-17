package com.wurmcraft.minecraftnotincluded.common.utils;

import com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded;
import com.wurmcraft.minecraftnotincluded.api.GeyserData;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Loader;

public class GeyserRegistry {

  private static HashMap<String, GeyserData> geyserData = new HashMap<>();
  private static List<GeyserData> geyserIndex = new ArrayList<>();

  public static final File GEYSER_DIR =
      new File(
          Loader.instance().getConfigDir()
              + File.separator
              + Global.NAME.replaceAll(" ", "_")
              + File.separator
              + "Geyser");

  public static void loadAndSetup() {
    if (GEYSER_DIR.exists()) {
      for (File file : GEYSER_DIR.listFiles()) {
        try {
          List<String> json = Files.readAllLines(file.toPath());
          GeyserDataJson farm =
              MinecraftNotIncluded.GSON.fromJson(String.join("", json), GeyserDataJson.class);
          if (farm != null) {
            register(farm);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      createDefaultGeysers();
    }
  }

  private static void createDefaultGeysers() {
    GeyserDataJson waterGeyser = new GeyserDataJson(Blocks.WATER.getDefaultState(), 100, 1.85);
    GeyserDataJson lavaGeyser = new GeyserDataJson(Blocks.LAVA.getDefaultState(), 200, 1.35);
    saveGeyser(lavaGeyser);
    saveGeyser(waterGeyser);
  }

  private static void saveGeyser(GeyserDataJson geyser) {
    File geyserFile =
        new File(
            GEYSER_DIR
                + File.separator
                + geyser.getBlock().getBlock().getRegistryName().getResourcePath()
                + ".json");
    if (!geyserFile.getParentFile().exists()) {
      geyserFile.getParentFile().mkdirs();
    }
    try {
      geyserFile.createNewFile();
      Files.write(geyserFile.toPath(), MinecraftNotIncluded.GSON.toJson(geyser).getBytes());
      register(geyser);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void register(GeyserData data) {
    geyserIndex.add(data);
    geyserData.put(BlockUtils.stateToString(data.getBlock()), data);
  }

  public static int getIDFromData(GeyserData data) {
    for (int index = 0; index < geyserIndex.size(); index++) {
      if (geyserIndex.get(index).getBlock().equals(data.getBlock())) {
        return index;
      }
    }
    return -1;
  }

  public static GeyserData getDataFromID(int id) {
    return geyserIndex.get(id);
  }

  public static GeyserData getFromState(String blockState) {
    return geyserData.getOrDefault(blockState, null);
  }

  public static int getCount() {
    return geyserData.size();
  }
}
