package com.wurmcraft.minecraftnotincluded.common.world.generation;

import static com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded.GSON;
import static com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded.oreConfig;

import com.wurmcraft.minecraftnotincluded.common.references.json.OreConfig;
import com.wurmcraft.minecraftnotincluded.common.references.json.OreConfig.OreGeneration;
import com.wurmcraft.minecraftnotincluded.common.utils.BlockUtils;
import com.wurmcraft.minecraftnotincluded.common.world.generation.type.VeinOreGenerator;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.ICubicPopulator;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class OreGenerator implements ICubicPopulator {

  public static OreConfig config;
  private static HashMap<String, WorldGenerator> oreGenerators = new HashMap<>();
  private static HashMap<Integer, List<String>> oreGeneratorRarityCache = new HashMap<>();
  private static HashMap<String, OreGeneration> nameCache = new HashMap<>();
  private static int highestRarity = 0;

  public OreGenerator(World world) {
    if (!oreConfig.exists()) {
      OreGenerator.config = OreGenerator.createDefaultConfig();
      try {
        oreConfig.getParentFile().mkdirs();
        oreConfig.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      OreGenerator.config = OreGenerator.loadOreConfig(world, oreConfig);
    }
    createGenerators(world);
  }

  private static void createGenerators(World world) {
    if (config != null) {
      for (OreGeneration gen : config.ores) {
        nameCache.put(gen.block, gen);
        oreGenerators.put(gen.block, createGenerator(world, gen, config.generationType));
        if (oreGeneratorRarityCache.containsKey(gen.rarity)) {
          oreGeneratorRarityCache.get(gen.rarity).add(gen.block);
        } else {
          List<String> temp = new ArrayList<>();
          temp.add(gen.block);
          oreGeneratorRarityCache.put(gen.rarity, temp);
        }
      }
      for (OreGeneration gen : config.ores) {
        if (highestRarity < gen.rarity) {
          highestRarity = gen.rarity;
        }
      }
    } else {
      config = createDefaultConfig();
    }
  }

  @Override
  public void generate(World world, Random rand, CubePos cubePos, Biome biome) {
    if (world.rand.nextInt(3) == 0) {
      OreGeneration gen = getRandomOreGenerator(world);
      if (gen.minHeight != -1 && gen.minHeight < (cubePos.getY() * 16)) {
        return;
      }
      if (gen.maxHeight != -1 && gen.maxHeight > (cubePos.getY() * 16)) {
        return;
      }
      if (gen.biomes.length > 1
          || gen.biomes.length == 1 && !gen.biomes[0].equalsIgnoreCase("ALL")) {
        return;
      }
      WorldGenerator worldGen = oreGenerators.get(gen.block);
      if (worldGen instanceof ICubicPopulator) {
        ((ICubicPopulator) worldGen).generate(world, rand, cubePos, biome);
      } else {
        worldGen.generate(
            world,
            rand,
            cubePos
                .getMinBlockPos()
                .add(world.rand.nextInt(8), world.rand.nextInt(8), world.rand.nextInt(8)));
      }
    }
  }

  private static OreGeneration getRandomOreGenerator(World world) {
    if (highestRarity > 0) {
      int currentRarity = 0;
      double chanceToMoveUp = Math.random();
      if (chanceToMoveUp > .5) {
        currentRarity = world.rand.nextInt(highestRarity / 2);
        chanceToMoveUp = Math.random();
        if (chanceToMoveUp > .75) {
          currentRarity = 1 + world.rand.nextInt((highestRarity - 1));
        }
      } else {
        currentRarity = world.rand.nextInt(1);
      }
      return nameCache.get(
          oreGeneratorRarityCache
              .get(currentRarity)
              .get(world.rand.nextInt(oreGeneratorRarityCache.get(currentRarity).size())));
    } else {
      return nameCache.get(
          oreGeneratorRarityCache
              .get(0)
              .get(world.rand.nextInt(oreGeneratorRarityCache.get(0).size())));
    }
  }

  public static OreConfig loadOreConfig(World world, File file) {
    try {
      OreConfig config = GSON.fromJson(new FileReader(file), OreConfig.class);
      for (OreGeneration ore : config.ores) {
        if (highestRarity < ore.rarity) {
          highestRarity = ore.rarity;
        }
        System.out.println("Ore Config: " + ore.block);
        oreGenerators.put(ore.block, createGenerator(world, ore, config.generationType));
      }
      return config;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static WorldGenerator createGenerator(
      World world, OreGeneration ore, GenerationType type) {
    if (type == GenerationType.STANDARD || type == GenerationType.SINGLE) {
      int oreCount = 1;
      if (type == GenerationType.STANDARD) {
        oreCount = (10 + world.rand.nextInt(8)) - (ore.rarity / 3);
        if (oreCount <= 0) {
          oreCount = 2 + world.rand.nextInt(8);
        }
      }
      IBlockState block = getBlockFromString(ore.block);
      return new WorldGenMinable(block, oreCount);
    } else if (type == GenerationType.LARGE_VEIN) {
      int oreCount = (50 + world.rand.nextInt(25)) - (ore.rarity * 5);
      if (oreCount < 10) {
        oreCount = 10;
      }
      return new VeinOreGenerator(getBlockFromString(ore.block), oreCount, ore);
    }
    return null;
  }

  private static IBlockState getBlockFromString(String name) {
    String modid = BlockUtils.getModID(name);
    String block = BlockUtils.getName(name);
    int meta;
    if (block.contains(":")) {
      meta = BlockUtils.getMeta(block);
      block = block.substring(0, block.indexOf(":"));
    } else {
      meta = BlockUtils.getMeta(name);
    }
    if (modid.equalsIgnoreCase("minecraft")) {
      modid = "";
    }
    ResourceLocation loc = new ResourceLocation(modid, block);
    for (ResourceLocation l : ForgeRegistries.BLOCKS.getKeys()) {
      if (l.getResourceDomain().equalsIgnoreCase(modid) && l.getResourcePath().equalsIgnoreCase(block)) {
        loc = l;
      }
    }
    Block blockReg = ForgeRegistries.BLOCKS.getValue(loc);
    if (blockReg != null) {
      return blockReg.getStateFromMeta(meta);
    } else {
      return Blocks.AIR.getDefaultState();
    }
  }

  public static OreConfig createDefaultConfig() {
    List<OreGeneration> defaultOres = new ArrayList<>();
    defaultOres.add(new OreGeneration(-1, -1, new String[] {"ALL"}, "minecraft:coal_ore", 0));
    defaultOres.add(new OreGeneration(-1, -1, new String[] {"ALL"}, "minecraft:iron_ore", 1));
    defaultOres.add(new OreGeneration(-1, -1, new String[] {"ALL"}, "minecraft:redstone_ore", 3));
    defaultOres.add(new OreGeneration(-1, -1, new String[] {"ALL"}, "minecraft:lapis_ore", 3));
    defaultOres.add(new OreGeneration(-1, -1, new String[] {"ALL"}, "minecraft:gold_ore", 2));
    defaultOres.add(new OreGeneration(-1, -1, new String[] {"ALL"}, "minecraft:diamond_ore", 4));
    defaultOres.add(new OreGeneration(-1, -1, new String[] {"ALL"}, "minecraft:emerald_ore", 5));
    return new OreConfig(GenerationType.STANDARD, defaultOres.toArray(new OreGeneration[0]));
  }

  public enum GenerationType {
    STANDARD,
    LARGE_VEIN,
    SINGLE
  }
}
