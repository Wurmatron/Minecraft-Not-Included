package com.wurmcraft.minecraftnotincluded.common.tile;

import com.wurmcraft.minecraftnotincluded.common.block.farm.t1.BlockCasing;
import com.wurmcraft.minecraftnotincluded.common.block.farm.t1.BlockController;
import com.wurmcraft.minecraftnotincluded.common.utils.MultiBlockUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;

public class TileControllerT1 extends TileEntityCasing implements ITickable {

  @Override
  public void update() {
    verifyAndUpdateStructure();
  }

  protected int isValidStructure() {
    int[][] structureSize = MultiBlockUtils.getAdjustedMultiblockDimensions(world, pos);
    boolean hasController = false;
    for (int x = structureSize[0][0]; x < structureSize[0][1]; x++) {
      for (int y = structureSize[1][0]; y < structureSize[1][0]; y++) {
        for (int z = structureSize[2][0]; z < structureSize[2][0]; z++) {
          IBlockState state = world.getBlockState(pos.add(x, y, z));
          if (!(state.getBlock() instanceof BlockCasing)) {
            return 0;
          }
          if (state.getBlock() instanceof BlockController) {
            if (hasController) {
              return 2; // Has Multiple Controllers
            }
            hasController = true;
          }
        }
      }
    }
    return 1;
  }

  public boolean verifyAndUpdateStructure() {
    return false;
  }
}
