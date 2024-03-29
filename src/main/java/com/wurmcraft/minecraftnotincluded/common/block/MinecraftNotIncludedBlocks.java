package com.wurmcraft.minecraftnotincluded.common.block;

import com.wurmcraft.minecraftnotincluded.api.GeyserData;
import com.wurmcraft.minecraftnotincluded.common.block.farm.BlockFarmTile;
import com.wurmcraft.minecraftnotincluded.common.block.farm.BlockHydroponicsTile;
import com.wurmcraft.minecraftnotincluded.common.block.farm.t1.BlockCasing;
import com.wurmcraft.minecraftnotincluded.common.block.farm.t1.BlockController;
import com.wurmcraft.minecraftnotincluded.common.block.generation.BlockLargeVine;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystal;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingCrystalHanging;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingDoublePlant;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingFlower;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingMushroom;
import com.wurmcraft.minecraftnotincluded.common.block.light.BlockGlowingVines;
import com.wurmcraft.minecraftnotincluded.common.block.utils.BasicBlock;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemBlockGeyser;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemFarmTile;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemGlowingCrystal;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemGlowingFlower;
import com.wurmcraft.minecraftnotincluded.common.item.block.ItemGlowingMushroom;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import com.wurmcraft.minecraftnotincluded.common.tile.TileControllerT1;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityCasing;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityFarm;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityHydroponics;
import com.wurmcraft.minecraftnotincluded.common.utils.GeyserRegistry;
import com.wurmcraft.minecraftnotincluded.common.utils.Registry;
import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MinecraftNotIncludedBlocks {

  // Geyser
  public static List<Block> geysers = new ArrayList<>();

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
  public static BlockHydroponicsTile hydroponicsTile;
  public static BlockController controllerT1;
  public static BlockCasing casingT1;

  public static void register() {
    // Geyser
    int currentGeysers = GeyserRegistry.getCount();
    if (currentGeysers < 16) {
      GeyserBlock geyserBlock = new GeyserBlock(0);
      Registry.registerBlock(geyserBlock, "geyser_0", new ItemBlockGeyser(geyserBlock, 0));
      geysers.add(geyserBlock);
    } else {
      for (int blocks = 0; blocks > (currentGeysers / 16); blocks++) {
        GeyserBlock geyserBlock = new GeyserBlock(blocks);
        geysers.add(geyserBlock);
        Registry.registerBlock(
            geyserBlock, "geyser_" + blocks, new ItemBlockGeyser(geyserBlock, blocks));
      }
    }
    // Surface
    Registry.registerBlock(
        blockDust = new BasicBlock(Material.SAND).setUnlocalizedName("dust"), "dust");
    Registry.registerBlock(
        blockCompressedDust = new BasicBlock(Material.SAND).setUnlocalizedName("compressedDust"),
        "compressedDust");
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
    Registry.registerBlock(
        glowingVines =
            (BlockGlowingVines) new BlockGlowingVines().setUnlocalizedName("glowingVines"),
        "glowingVines");
    Registry.registerBlock(largeVine = new BlockLargeVine(), "largeVine", true);
    // Farming
    Registry.registerBlock(
        farmTile = (BlockFarmTile) new BlockFarmTile().setUnlocalizedName("farmTile"),
        "farmTile",
        new ItemFarmTile(farmTile));
    GameRegistry.registerTileEntity(
        TileEntityFarm.class, new ResourceLocation(Global.MODID, "farmTile"));
    Registry.registerBlock(
        hydroponicsTile =
            (BlockHydroponicsTile) new BlockHydroponicsTile().setUnlocalizedName("hydroponicsTile"),
        "hydroponicsTile");
    GameRegistry.registerTileEntity(
        TileEntityHydroponics.class, new ResourceLocation(Global.MODID, "hydroponicsTile"));

    Registry.registerBlock(
        controllerT1 =
            (BlockController) new BlockController().setUnlocalizedName("farmControllerT1"),
        "farmControllerT1");
    GameRegistry.registerTileEntity(TileControllerT1.class, "controller");
    Registry.registerBlock(
        casingT1 = (BlockCasing) new BlockCasing().setUnlocalizedName("casingT1"), "casingT1");
    GameRegistry.registerTileEntity(TileEntityCasing.class, "casing");
  }

  public static IBlockState getGeyser(GeyserData data) {
    int dataIndex = GeyserRegistry.getIDFromData(data);
    return geysers.get(dataIndex / 16).getStateFromMeta(dataIndex % 16);
  }
}
