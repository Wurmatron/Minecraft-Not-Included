package com.wurmcraft.minecraftnotincluded.common.references.json;

import com.wurmcraft.minecraftnotincluded.common.world.generation.OreGenerator.GenerationType;

public class OreConfig {

  public GenerationType generationType;
  public OreGeneration[] ores;

  public OreConfig(GenerationType generationType, OreGeneration[] ores) {
    this.generationType = generationType;
    this.ores = ores;
  }

  public static class OreGeneration {

    public int minHeight;
    public int maxHeight;
    public String[] biomes;
    public String block;
    public int rarity;

    public OreGeneration(int minHeight, int maxHeight, String[] biomes, String block, int rarity) {
      this.minHeight = minHeight;
      this.maxHeight = maxHeight;
      this.biomes = biomes;
      this.block = block;
      this.rarity = rarity;
    }
  }
}
