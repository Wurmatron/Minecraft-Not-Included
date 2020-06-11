package com.wurmcraft.minecraftnotincluded.common.world;

import com.wurmcraft.minecraftnotincluded.common.ConfigHandler.Wasteland;
import com.wurmcraft.minecraftnotincluded.common.world.overworld.MNIOverworldGenerator;
import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.core.CubicChunks;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;

public class MNIWorldType extends WorldType implements ICubicWorldType {

  public MNIWorldType() {
    super("MNI");
  }

  @Override
  public IntRange calculateGenerationHeightRange(WorldServer w) {
    return new IntRange(0, 1);
  }

  @Override
  public boolean hasCubicGeneratorForWorld(World w) {
    // Makes sure the world is the overworld
    //    return w.provider.getClass() == DimensionManager.getProvider(0).getClass();
    return true;
  }

  @Override
  public ICubeGenerator createCubeGenerator(World w) {
    //    if (w.provider.getClass() == DimensionManager.getProvider(0).getClass()) {
    return new MNIOverworldGenerator(w, w.getBiomeProvider());
    //    }
    //    return null;
  }

  public boolean isCustomizable() {
    return false;
  }

  @Override
  public float getCloudHeight() {
    if (!Wasteland.enabled) return CubicChunks.MAX_BLOCK_Y;
    return super.getCloudHeight();
  }
}
