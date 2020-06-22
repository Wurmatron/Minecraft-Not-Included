package com.wurmcraft.minecraftnotincluded.common.network.packets;

import com.wurmcraft.minecraftnotincluded.common.network.utils.CustomMessage;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityFarm;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

public class EmptyFarmPacket extends CustomMessage.CustomtServerMessage<EmptyFarmPacket> {

  private boolean emptySoil;
  private boolean emptyFluid;
  private boolean emptyFertilizer;

  private BlockPos loc;

  public EmptyFarmPacket() {}

  public EmptyFarmPacket(
      boolean emptySoil, boolean emptyFluid, boolean emptyFertilizer, BlockPos loc) {
    this.emptySoil = emptySoil;
    this.emptyFluid = emptyFluid;
    this.emptyFertilizer = emptyFertilizer;
    this.loc = loc;
  }

  @Override
  protected void read(PacketBuffer buff) throws IOException {
    int[] empty = buff.readVarIntArray();
    emptySoil = empty[0] == 1;
    emptyFluid = empty[1] == 1;
    emptyFertilizer = empty[2] == 1;
    loc = buff.readBlockPos();
  }

  @Override
  protected void write(PacketBuffer buff) throws IOException {
    buff.writeVarIntArray(
        new int[] {emptySoil ? 1 : 0, emptyFluid ? 1 : 0, emptyFertilizer ? 1 : 0});
    buff.writeBlockPos(loc);
  }

  @Override
  public void process(EntityPlayer player, Side side) {
    TileEntityFarm farm = (TileEntityFarm) player.world.getTileEntity(loc);
    if (farm != null) {
      if (emptySoil) {
        farm.emptySoil();
      }
      if (emptyFluid) {
        farm.emptyFluid();
      }
      if (emptyFertilizer) {
        farm.emptyFertilizer();
      }
    }
  }
}
