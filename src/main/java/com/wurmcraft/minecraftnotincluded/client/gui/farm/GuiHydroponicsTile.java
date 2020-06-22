package com.wurmcraft.minecraftnotincluded.client.gui.farm;

import com.wurmcraft.minecraftnotincluded.common.references.Global;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiHydroponicsTile extends GuiFarmTile {

  public GuiHydroponicsTile(Container container) {
    super(container);
    this.GUI_TEXTURE = new ResourceLocation(Global.MODID, "textures/gui/hydroponics-tile.png");
  }
}
