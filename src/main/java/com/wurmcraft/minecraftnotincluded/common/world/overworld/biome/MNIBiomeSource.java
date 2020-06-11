package com.wurmcraft.minecraftnotincluded.common.world.overworld.biome;

import com.wurmcraft.minecraftnotincluded.common.ConfigHandler.Biomes;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class MNIBiomeSource {

  private World world;
  private BiomeProvider provider;
  private List<Biome> validBiomes;

  // Biome Cache
  private HashMap<Biome, Integer> BIOME_INDEX_CACHE;
  private HashMap<Integer, Biome> BIOME_LOOKUP_CACHE;

  // Chunk Cache
  private HashMap<CubePos, Biome> CUBE_BIOME_CACHE;

  public MNIBiomeSource(World world, BiomeProvider provider) {
    this.world = world;
    this.provider = provider;
    validBiomes = new ArrayList<>();
    BIOME_INDEX_CACHE = new HashMap<>();
    BIOME_LOOKUP_CACHE = new HashMap<>();
    CUBE_BIOME_CACHE = new HashMap<>();
    int currentIndex = 0;
    for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
      for (String enabledBiomes : Biomes.enabledBiomes) {
        if (biome.getBiomeName().equalsIgnoreCase(enabledBiomes)) {
          validBiomes.add(biome);
          BIOME_INDEX_CACHE.put(biome, currentIndex);
          BIOME_LOOKUP_CACHE.put(currentIndex, biome);
          currentIndex++;
        }
      }
    }
  }

  public Biome getBiome(int x, int y, int z) {
    return getBiome(new CubePos(x, y, z));
  }

  public Biome getBiome(CubePos cubePos) {
    if (CUBE_BIOME_CACHE.containsKey(cubePos)) {
      return CUBE_BIOME_CACHE.get(cubePos);
    }
    int shift = calculateShift(cubePos.getY() / 16);
    int standardBiome =
        BIOME_INDEX_CACHE.getOrDefault(
            provider.getBiome(cubePos.getCenterBlockPos()),
            world.rand.nextInt(BIOME_INDEX_CACHE.size()));
    Biome biome =
        BIOME_LOOKUP_CACHE.getOrDefault(
            repeatUntilValid(standardBiome + shift), net.minecraft.init.Biomes.VOID);
    CUBE_BIOME_CACHE.put(cubePos, biome);
    return biome;
  }

  private int calculateShift(int yCube) {
    if (yCube == 0) {
      return 0;
    }
    if (yCube > 0) { // Positive Shift
      int shift = yCube / (Biomes.biomeShiftSize / 16);
      if (world.rand.nextInt(Biomes.biomeFuzzyness) == 0) {
        shift--;
      }
      return shift;
    } else {
      int shift = Math.abs(yCube) / (Biomes.biomeShiftSize / 16);
      if (world.rand.nextInt(Biomes.biomeFuzzyness) == 0) {
        shift++;
      }
      return shift;
    }
  }

  private int repeatUntilValid(int biomeID) {
    if (biomeID < 0) {
      return world.rand.nextInt(BIOME_LOOKUP_CACHE.size() - 1);
    }
    if (biomeID < BIOME_LOOKUP_CACHE.size()) {
      return biomeID;
    }
    for (int reducedID = biomeID; reducedID < BIOME_LOOKUP_CACHE.size(); ) {
      reducedID = -BIOME_LOOKUP_CACHE.size();
      return repeatUntilValid(reducedID);
    }
    return -1;
  }
}
