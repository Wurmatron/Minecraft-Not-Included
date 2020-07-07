package com.wurmcraft.minecraftnotincluded.common.block.farm.t1;

import com.wurmcraft.minecraftnotincluded.common.block.utils.BlockMaster;
import com.wurmcraft.minecraftnotincluded.common.tile.utils.TileEntityController;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockT1Controller extends BlockMaster {

  public BlockT1Controller() {
    super(Material.CIRCUITS);
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityController();
  }
}
