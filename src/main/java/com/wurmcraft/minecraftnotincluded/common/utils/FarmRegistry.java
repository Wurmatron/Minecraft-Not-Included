package com.wurmcraft.minecraftnotincluded.common.utils;

import com.wurmcraft.minecraftnotincluded.api.Farmable;
import com.wurmcraft.minecraftnotincluded.api.Farmable.TILE_TYPE;
import com.wurmcraft.minecraftnotincluded.client.gui.farm.SlotInput;
import com.wurmcraft.minecraftnotincluded.client.gui.farm.SlotSeed;
import java.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FarmRegistry {

  private static HashMap<String, Farmable> farmables = new HashMap<>();
  private static HashMap<TILE_TYPE, List<Farmable>> tileFarmables = new HashMap<>();

  public static void loadAndSetup() {
    register(
        new Farmable() {

          @Override
          public String getName() {
            return "wheat";
          }

          @Override
          public ItemStack getSeed() {
            return new ItemStack(Items.WHEAT_SEEDS, 1, 0);
          }

          @Override
          public Map<BlockPos, IBlockState> getCropForRendering() {
            Map<BlockPos, IBlockState> blocks = new HashMap<>();
            blocks.put(new BlockPos(0, 0, 0), Blocks.WHEAT.getStateFromMeta(7));
            return blocks;
          }

          @Override
          public int getMBPerSecond() {
            return 1;
          }

          @Override
          public Fluid getFluid() {
            return FluidRegistry.WATER;
          }

          @Override
          public ItemStack getSoil() {
            return new ItemStack(Blocks.DIRT, 1, 0);
          }

          @Override
          public double getItemPerSecond() {
            return .01;
          }

          @Override
          public int secondsToGrow() {
            return 400;
          }

          @Override
          public int powerPerTick() {
            return 0;
          }

          @Override
          public TILE_TYPE[] getFarmType() {
            return new TILE_TYPE[] {
              TILE_TYPE.FARM_TILE, TILE_TYPE.HYDROPONICS, TILE_TYPE.MULTI_BLOCK
            };
          }

          @Override
          public DropChance[] getDropChances() {
            return new DropChance[] {
              new DropChance(1, new ItemStack(Items.WHEAT, 1, 0)),
              new DropChance(.25, new ItemStack(Items.WHEAT_SEEDS))
            };
          }
        });
  }

  public static void register(Farmable farmable) {
    farmables.put(farmable.getName(), farmable);
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
}
