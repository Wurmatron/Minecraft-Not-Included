package com.wurmcraft.minecraftnotincluded.common;

import com.wurmcraft.minecraftnotincluded.common.references.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = Global.MODID)
public class ConfigHandler {

  @Config(modid = Global.MODID, category = "Biomes")
  public static class Biomes {

    @Comment("List of biomes that are able to be used in the creation of a cavern")
    public static String[] enabledBiomes =
        new String[] {"Forest", "Plains", net.minecraft.init.Biomes.DESERT.getBiomeName()};

    @Comment("How many blocks before a biome change")
    public static int biomeShiftSize = 64;

    @Comment(
        "How strict the biome change is 1 / x change to stay the same for each additional cube")
    public static int biomeFuzzyness = 2;
  }

  @Config(modid = Global.MODID, category = "Wasteland")
  public static class Wasteland {

    @Comment("Enable / Disabled the Surface Wasteland")
    public static boolean enabled = false;

    @Comment("Enable / Disabled damage from sky radiation")
    public static boolean radiationDamage = true;

    @Comment({"How much damage to deal from radiation per second"})
    public static int radiationDamagePerSecond = 1;

    @Comment({"Location were the surface wasteland begins, similar to the seaLevel"})
    public static int startY = 64;
  }
}
