package com.wurmcraft.minecraftnotincluded.common.world.overworld;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class MNIBiomeSource {

  private World world;
  private BiomeProvider provider;

  // Cached Data
  private NonBlockingHashMap<ChunkPos, Biome[]> biomeChunkCache;

  public MNIBiomeSource(World world, BiomeProvider provider) {
    this.world = world;
    this.provider = provider;
    biomeChunkCache = new NonBlockingHashMap<>();
  }

  public Biome getBiome(int x, int y, int z) {
    return Biomes.FOREST;
  }
}
