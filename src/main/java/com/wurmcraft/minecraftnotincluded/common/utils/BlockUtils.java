package com.wurmcraft.minecraftnotincluded.common.utils;

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
      } else return -1;
    } else {
      return -1;
    }
  }
}
