package com.wurmcraft.minecraftnotincluded.common.block.utils;

import com.wurmcraft.minecraftnotincluded.common.tile.utils.TileEntityController;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMaster extends BlockContainer {

  public BlockMaster(Material material) {
    super(material);
    hasTileEntity = true;
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityController();
  }
}
