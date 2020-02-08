package com.wurmcraft.minecraftnotincluded.common.gen;

import com.wurmcraft.minecraftnotincluded.common.biome.BiomeRegistry;
import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

// Modified version of https://github.com/OpenCubicChunks/CubicChunks/blob/MC_1.12/src/main/java/io/github/opencubicchunks/cubicchunks/core/worldgen/generator/vanilla/VanillaCompatibilityGenerator.java
public class MNIWorldType extends WorldType implements ICubicWorldType {

  private IChunkGenerator vanillaGenerator;

  public MNIWorldType() {
    super("MNI");
  }

  @Override
  public IntRange calculateGenerationHeightRange(WorldServer world) {
    return new IntRange(0, 0);
  }

  @Override
  public boolean hasCubicGeneratorForWorld(World w) {
    return true;
  }

  public BiomeProvider getBiomeProvider(World world) {
    return new BiomeProviderSingle(BiomeRegistry.wasteland);
  }

  @Override
  public ICubeGenerator createCubeGenerator(World world) {
    return new MNIGenerator(world);
  }

  public boolean isCustomizable() {
    return false;
  }
}
