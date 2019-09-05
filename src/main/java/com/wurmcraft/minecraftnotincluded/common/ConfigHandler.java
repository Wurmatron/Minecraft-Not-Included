package com.wurmcraft.minecraftnotincluded.common;

import com.wurmcraft.minecraftnotincluded.common.references.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = Global.MODID)
public class ConfigHandler {

  @Comment("Enable / Disabled damage from sky radiation")
  public static boolean radiationDamage = true;

  @Comment("How much damage to deal from radiation per second")
  public static int radiationDamagePerSecond = 1;
}
