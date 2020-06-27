package com.wurmcraft.minecraftnotincluded.common.utils;

import com.wurmcraft.minecraftnotincluded.api.GeyserData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeyserRegistry {

  private static HashMap<String, GeyserData> geyserData = new HashMap<>();
  private static List<GeyserData> geyserIndex = new ArrayList<>();

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
