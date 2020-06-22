package com.wurmcraft.minecraftnotincluded.common.block;

import com.wurmcraft.minecraftnotincluded.common.block.farm.BlockFarmTile;
import com.wurmcraft.minecraftnotincluded.common.block.generation.BlockLargeVine;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystal;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystalHanging;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingDoublePlant;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingFlower;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingMushroom;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingVines;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemFarmTile;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemGlowingCrystal;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemGlowingFlower;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemGlowingMushroom;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityFarm;
import com.wurmcraft.minecraftnotincluded.common.utils.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MinecraftNotIncludedBlocks {

  // Geyser
  public static Block geyserWater;
  public static Block geyserLava;

  // Surface
  public static Block blockDust;
  public static Block blockCompressedDust;

  // Underground glowing
  public static BlockGlowingMushroom glowingMushroom;
  public static BlockGlowingDoublePlant glowingDoublePlant;
  public static BlockGlowingCrystal glowingCrystal;
  public static BlockGlowingFlower glowingFlower;
  public static BlockGlowingVines glowingVines;
  public static BlockLargeVine largeVine;
  public static BlockGlowingCrystalHanging glowingCrystalHanging;

  // Farm
  public static BlockFarmTile farmTile;

  public static void register() {
    // Geyser
    //    Registry.registerBlock(geyserWater = new GeyserBlock(Type.WATER), "geyserWater");
    //    Registry.registerBlock(geyserLava = new GeyserBlock(Type.LAVA), "geyserLava");
    // Surface
    Registry.registerBlock(blockDust = new BasicBlock(Material.SAND), "dust");
    Registry.registerBlock(blockCompressedDust = new BasicBlock(Material.SAND), "compressedDust");
    // Underground plants
    Registry.registerBlock(
        glowingMushroom = new BlockGlowingMushroom(),
        "glowingMushroom",
        new ItemGlowingMushroom(glowingMushroom));
    Registry.registerBlock(
        glowingDoublePlant = new BlockGlowingDoublePlant(), "glowingDoublePlant", true);
    Registry.registerBlock(
        glowingFlower = new BlockGlowingFlower(),
        "glowingFlower",
        new ItemGlowingFlower(glowingFlower));
    // Underground
    Registry.registerBlock(
        glowingCrystal = new BlockGlowingCrystal(),
        "glowingCrystal",
        new ItemGlowingCrystal(glowingCrystal));
    Registry.registerBlock(
        glowingCrystalHanging = new BlockGlowingCrystalHanging(), "glowingCrystalHanging", true);
    Registry.registerBlock(glowingVines = new BlockGlowingVines(), "glowingVines");
    Registry.registerBlock(largeVine = new BlockLargeVine(), "largeVine", true);
    // Farming
    Registry.registerBlock(farmTile = new BlockFarmTile(), "farmTile", new ItemFarmTile(farmTile));
    GameRegistry.registerTileEntity(
        TileEntityFarm.class, new ResourceLocation(Global.MODID, "farmTile"));
  }
}
