package com.wurmcraft.minecraftnotincluded.common.biome;

import java.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiomeUtils {

  public static BlockPos[] spreadFeatures(World world, BlockPos pos, int maxAmount, int distance) {
    List<BlockPos> features = new ArrayList<>();
    features.add(pos.add(world.rand.nextInt(16), 0, world.rand.nextInt(16)));
    for (int f = 0; f < maxAmount; f++) {
      BlockPos newPos =
          pos.add(
              world.rand.nextInt(4)
                  + world.rand.nextInt(4)
                  + world.rand.nextInt(4)
                  + world.rand.nextInt(4),
              0,
              world.rand.nextInt(4)
                  + world.rand.nextInt(4)
                  + world.rand.nextInt(4)
                  + world.rand.nextInt(4));
      if (!isNear(features, newPos, distance)) {
        features.add(newPos);
      }
      if (world.rand.nextInt(maxAmount) > 0) {
        break;
      }
    }
    return features.toArray(new BlockPos[0]);
  }

  public static boolean isNear(List<BlockPos> current, BlockPos pos, int distance) {
    for (BlockPos p : current) {
      if (p.getX() + distance < pos.getX() && pos.getX() - distance > pos.getX()) {
        return true;
      }
      if (p.getZ() + distance < pos.getZ() && pos.getZ() - distance > pos.getZ()) {
        return true;
      }
    }
    return false;
  }
}
