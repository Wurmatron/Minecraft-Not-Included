package com.wurmcraft.minecraftnotincluded.common.utils;

import com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded;
import com.wurmcraft.minecraftnotincluded.api.Farmable;
import com.wurmcraft.minecraftnotincluded.api.Farmable.DropChance;
import com.wurmcraft.minecraftnotincluded.api.Farmable.TILE_TYPE;
import com.wurmcraft.minecraftnotincluded.client.gui.farm.SlotInput;
import com.wurmcraft.minecraftnotincluded.client.gui.farm.SlotSeed;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

public class FarmRegistry {

  private static HashMap<String, Farmable> farmables = new HashMap<>();
  private static HashMap<TILE_TYPE, List<Farmable>> tileFarmables = new HashMap<>();

  public static final File FARM_DIR =
      new File(
          Loader.instance().getConfigDir()
              + File.separator
              + Global.NAME.replaceAll(" ", "_")
              + File.separator
              + "Crops");

  public static void loadAndSetup() {
    if (FARM_DIR.exists()) {
      for (File file : FARM_DIR.listFiles()) {
        try {
          List<String> json = Files.readAllLines(file.toPath());
          Farmable farm =
              MinecraftNotIncluded.GSON.fromJson(String.join("", json), FarmableJson.class);
          if (farm != null) {
            register(farm);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      createDefaultConfigs();
    }
  }

  public static void register(Farmable farmable) {
    farmables.put(farmable.getName(), farmable);
    MinecraftNotIncluded.logger.info(
        "Creating farm for '"
            + farmable.getSeed().getDisplayName()
            + "' with soil '"
            + farmable.getSoil().getDisplayName()
            + "'");
    for (TILE_TYPE type : farmable.getFarmType()) {
      if (tileFarmables.containsKey(type)) {
        tileFarmables.get(type).add(farmable);
        SlotInput.slotItems.add(farmable.getSoil());
        SlotInput.fluids.add(farmable.getFluid());
        SlotSeed.supportedSeeds.add(farmable.getSeed());
      } else {
        List<Farmable> temp = new ArrayList<>();
        temp.add(farmable);
        tileFarmables.put(type, temp);
        SlotInput.slotItems.add(farmable.getSoil());
        SlotInput.fluids.add(farmable.getFluid());
        SlotSeed.supportedSeeds.add(farmable.getSeed());
      }
    }
  }

  public static List<Farmable> getTypePerTile(TILE_TYPE type) {
    return tileFarmables.get(type);
  }

  public static Farmable getFarmableFromSeed(TILE_TYPE type, ItemStack seed) {
    List<Farmable> farmable = getTypePerTile(type);
    if (farmable != null) {
      for (Farmable farm : farmable) {
        if (farm.getSeed().isItemEqual(seed)) {
          return farm;
        }
      }
    }
    return null;
  }

  private static void createDefaultConfigs() {
    Farmable wheat =
        new FarmableJson(
            "wheat",
            new ItemStack(Items.WHEAT_SEEDS, 1, 0),
            Blocks.WHEAT.getStateFromMeta(7),
            2,
            FluidRegistry.WATER.getName(),
            new ItemStack(Blocks.DIRT, 1, 0),
            .01,
            1200,
            new DropChance[] {
              new DropChance(1, new ItemStack(Items.WHEAT, 1, 0)),
              new DropChance(.25, new ItemStack(Items.WHEAT_SEEDS))
            });
    saveFarmable(wheat);
    Farmable beetroot =
        new FarmableJson(
            "wheat",
            new ItemStack(Items.BEETROOT_SEEDS, 1, 0),
            Blocks.BEETROOTS.getStateFromMeta(3),
            2,
            FluidRegistry.WATER.getName(),
            new ItemStack(Blocks.DIRT, 1, 0),
            .01,
            1200,
            new DropChance[] {
              new DropChance(1, new ItemStack(Items.BEETROOT, 1, 0)),
              new DropChance(.25, new ItemStack(Items.BEETROOT_SEEDS))
            });
    saveFarmable(beetroot);
    Farmable melon =
        new FarmableJson(
            "melon",
            new ItemStack(Items.MELON_SEEDS, 1, 0),
            Blocks.MELON_BLOCK.getDefaultState(),
            4,
            FluidRegistry.WATER.getName(),
            new ItemStack(Blocks.DIRT, 1, 0),
            .01,
            1600,
            new DropChance[] {
              new DropChance(1, new ItemStack(Items.MELON, 6, 0)),
              new DropChance(.25, new ItemStack(Items.MELON, 2, 0)),
              new DropChance(.1, new ItemStack(Items.MELON_SEEDS, 2, 0))
            });
    saveFarmable(melon);
    Farmable pumpkin =
        new FarmableJson(
            "pumpkin",
            new ItemStack(Items.PUMPKIN_SEEDS, 1, 0),
            Blocks.PUMPKIN.getDefaultState(),
            2,
            FluidRegistry.WATER.getName(),
            new ItemStack(Blocks.DIRT, 1, 0),
            .01,
            1600,
            new DropChance[] {
              new DropChance(1, new ItemStack(Blocks.PUMPKIN, 1, 0)),
              new DropChance(.1, new ItemStack(Items.PUMPKIN_SEEDS)),
              new DropChance(.01, new ItemStack(Blocks.LIT_PUMPKIN))
            });
    saveFarmable(pumpkin);
    Farmable carrot =
        new FarmableJson(
            "carrot",
            new ItemStack(Items.CARROT, 1, 0),
            Blocks.CARROTS.getStateFromMeta(7),
            1,
            FluidRegistry.WATER.getName(),
            new ItemStack(Blocks.DIRT, 1, 0),
            .02,
            1200,
            new DropChance[] {
              new DropChance(1, new ItemStack(Items.CARROT, 1, 0)),
              new DropChance(.5, new ItemStack(Items.CARROT))
            });
    saveFarmable(carrot);
    Farmable potato =
        new FarmableJson(
            "potato",
            new ItemStack(Items.POTATO, 1, 0),
            Blocks.POTATOES.getStateFromMeta(7),
            1,
            FluidRegistry.WATER.getName(),
            new ItemStack(Blocks.DIRT, 1, 0),
            .02,
            1200,
            new DropChance[] {
              new DropChance(1, new ItemStack(Items.POTATO, 1, 0)),
              new DropChance(.5, new ItemStack(Items.POTATO)),
              new DropChance(.1, new ItemStack(Items.POISONOUS_POTATO))
            });
    saveFarmable(potato);
    Farmable chorusFruit =
        new FarmableJson(
            "chorusFruit",
            new ItemStack(Blocks.CHORUS_FLOWER, 1, 0),
            Blocks.CHORUS_FLOWER.getDefaultState(),
            1,
            FluidRegistry.WATER.getName(),
            new ItemStack(Blocks.END_STONE, 1, 0),
            .01,
            2000,
            new DropChance[] {
              new DropChance(1, new ItemStack(Items.CHORUS_FRUIT, 5, 0)),
              new DropChance(.5, new ItemStack(Items.CHORUS_FRUIT, 3, 0)),
              new DropChance(.05, new ItemStack(Blocks.CHORUS_FLOWER))
            });
    saveFarmable(chorusFruit);
  }

  private static void saveFarmable(Farmable farmable) {
    File farmFile = new File(FARM_DIR + File.separator + farmable.getName() + ".json");
    if (!farmFile.getParentFile().exists()) {
      farmFile.getParentFile().mkdirs();
    }
    try {
      farmFile.createNewFile();
      Files.write(farmFile.toPath(), MinecraftNotIncluded.GSON.toJson(farmable).getBytes());
      register(farmable);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
