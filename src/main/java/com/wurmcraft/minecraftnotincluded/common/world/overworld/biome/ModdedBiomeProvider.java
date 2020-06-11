package com.wurmcraft.minecraftnotincluded.common.world.overworld.biome;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.storage.WorldInfo;

public class ModdedBiomeProvider extends BiomeProvider {

  public static NBTTagCompound getGeneratorSettings(World world, String terrainJson) {
    NBTTagCompound worldNBT = world.getWorldInfo().cloneNBTCompound(new NBTTagCompound());
    worldNBT.setString("generatorName", WorldType.CUSTOMIZED.getName());
    worldNBT.setString("generatorOptions", terrainJson);
    return worldNBT;
  }

  public ModdedBiomeProvider(NBTTagCompound generatorSettings) {
    super(new WorldInfo(generatorSettings));
  }
}
