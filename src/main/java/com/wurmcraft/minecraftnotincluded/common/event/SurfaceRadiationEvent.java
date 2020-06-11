package com.wurmcraft.minecraftnotincluded.common.event;

import com.wurmcraft.minecraftnotincluded.common.ConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SurfaceRadiationEvent {

  @SubscribeEvent
  public void onLivingUpdate(LivingUpdateEvent e) {
    if (e.getEntityLiving().world.getWorldTime() % 20 == 0
        && e.getEntityLiving() instanceof EntityPlayer
        && !e.getEntityLiving().world.isRemote) {
      EntityPlayer player = (EntityPlayer) e.getEntityLiving();
      if (!player.capabilities.isCreativeMode && !player.capabilities.disableDamage) {
        if (player.world.canSeeSky(player.getPosition())) {
          // Check for radiation protection items
          player.setHealth(player.getHealth() - ConfigHandler.Wasteland.radiationDamagePerSecond);
        }
      }
    }
  }
}
