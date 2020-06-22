package com.wurmcraft.minecraftnotincluded.client.gui.farm;

import com.wurmcraft.minecraftnotincluded.common.network.NetworkHandler;
import com.wurmcraft.minecraftnotincluded.common.network.packets.EmptyFarmPacket;
import com.wurmcraft.minecraftnotincluded.common.references.Global;
import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityFarm;
import java.awt.Color;
import java.io.IOException;
import java.util.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;

public class GuiFarmTile extends GuiContainer {

  private TileEntityFarm te;
  protected ResourceLocation GUI_TEXTURE =
      new ResourceLocation(Global.MODID, "textures/gui/farm-tile.png");

  // Button Status
  private boolean soilEmptyToggle;
  private boolean fluidEmptyToggle;
  private boolean fertilizerEmptyToggle;

  public GuiFarmTile(Container container) {
    super(container);
    this.te = ((ContainerFarmTile) container).te;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawDefaultBackground();
    int startX = (width - 176) / 2;
    int startY = (height - 174) / 2;
    mc.getTextureManager().bindTexture(GUI_TEXTURE);
    drawTexturedModalRect(startX, startY, 0, 0, 176, 174);
    drawEmptyButtons(startX, startY, mouseX, mouseY);
    drawStatusBars(startX, startY, mouseX, mouseY);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    // Render Hover tooltip
    Slot hoverSlot = getSlotUnderMouse();
    if (mc.player.inventory.getItemStack().isEmpty()
        && hoverSlot != null
        && hoverSlot.getHasStack()) {
      this.renderToolTip(hoverSlot.getStack(), mouseX, mouseY);
    }
  }

  private void drawStatusBars(int startX, int startY, int mouseX, int mouseY) {
    // Hydration
    int hydrationPerc = (int) (46 * te.getFluidPercentage());
    int hydrationStartX = startX + 8;
    int hydrationStartY = (startY + 12) + (46 - hydrationPerc);
    drawTexturedModalRect(hydrationStartX, hydrationStartY, 185, 0, 4, hydrationPerc);
    // Fertilizer
    int fertlizationPerc = (int) (46 * te.getFertilizerPercentage());
    int fertStartX = startX + 14;
    int fertStartY = startY + 12 + (46 - fertlizationPerc);
    drawTexturedModalRect(fertStartX, fertStartY, 190, 0, 4, fertlizationPerc);
    // Soil
    int soilPerc = (int) (46 * te.getSoilPercentage());
    int soilStartX = startX + 20;
    int soilStartY = startY + 12 + (46 - soilPerc);
    drawTexturedModalRect(soilStartX, soilStartY, 195, 0, 4, soilPerc);
    // Growth
    int growthPerc = (int) (26 * te.getGrowthPercentage());
    int growthStartX = startX + 83;
    int growthStartY = startY + 13 + (26 - growthPerc);
    drawTexturedModalRect(growthStartX, growthStartY, 200, 26 - growthPerc, 16, growthPerc);
    // Hovering Text
    GlStateManager.pushMatrix();
    hydrationStartY = startY + 12;
    soilStartY = startY + 12;
    fertStartY = startY + 12;
    if (isWithin(fertStartX, fertStartX + 4, fertStartY - 4, fertStartY + 46, mouseX, mouseY)) {
      List<String> str = new ArrayList<>();
      str.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal("stat.fertilization.name"));
      str.add(
          TextFormatting.RED
              + ""
              + TextFormatting.ITALIC
              + te.getFertilizerAmount()
              + " / "
              + TextFormatting.DARK_RED
              + te.getMaxFertilizerStorage());
      str.add(TextFormatting.GOLD + " " + te.getFertilizerEfficiency());
      drawHoveringText(str, startX, mouseY + 4);
    }
    if (isWithin(soilStartX, soilStartX + 4, soilStartY - 4, soilStartY + 46, mouseX, mouseY)) {
      List<String> str = new ArrayList<>();
      str.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal("stat.soil.name"));
      str.add(
          TextFormatting.RED
              + ""
              + TextFormatting.ITALIC
              + te.getSoilAmount()
              + " / "
              + TextFormatting.DARK_RED
              + te.getMaxSoilStorage());
      drawHoveringText(str, startX, mouseY + 4);
    }
    if (isWithin(
        hydrationStartX,
        hydrationStartX + 4,
        hydrationStartY - 4,
        hydrationStartY + 46,
        mouseX,
        mouseY)) {
      List<String> str = new ArrayList<>();
      str.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal("stat.hydration.name"));
      str.add(
          TextFormatting.RED
              + ""
              + TextFormatting.ITALIC
              + te.getFluidAmount()
              + "mb / "
              + TextFormatting.DARK_RED
              + te.getMaxFluidStorage()
              + "mb");
      drawHoveringText(str, startX, mouseY + 4);
    }
    if (isWithin(growthStartX, growthStartX + 16, startY + 13, startY + 39, mouseX, mouseY)) {
      drawHoveringText(
          TextFormatting.GREEN + "" + (te.getGrowthPercentage() * 100) + "%",
          growthStartX,
          mouseY + 4);
    }
    GlStateManager.popMatrix();
  }

  private void drawEmptyButtons(int startX, int startY, int mouseX, int mouseY) {
    int emptyY = startY + 6;
    drawTexturedModalRect(startX + 8, emptyY, soilEmptyToggle ? 222 : 217, 0, 4, 4);
    drawTexturedModalRect(startX + 14, emptyY, fertilizerEmptyToggle ? 222 : 217, 0, 4, 4);
    drawTexturedModalRect(startX + 20, emptyY, fluidEmptyToggle ? 222 : 217, 0, 4, 4);
    GlStateManager.pushMatrix();
    GlStateManager.scale(.5, .5, .5);
    fontRenderer.drawString("Empty", (startX + 29) * 2, (startY + 35) * 2, Color.WHITE.getRGB());
    GlStateManager.popMatrix();
    mc.getTextureManager().bindTexture(GUI_TEXTURE);
  }

  private static boolean isWithin(int minX, int maxX, int minY, int maxY, int mouseX, int mouseY) {
    return mouseX > minX && mouseY > minY && maxX > mouseX && maxY > mouseY;
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    int startX = (width - 176) / 2;
    int startY = (height - 174) / 2;
    if (isWithin(startX + 8, startX + 12, startY + 6, startY + 10, mouseX, mouseY)) {
      soilEmptyToggle = !soilEmptyToggle;
    } else if (isWithin(startX + 14, startX + 18, startY + 6, startY + 10, mouseX, mouseY)) {
      fertilizerEmptyToggle = !fertilizerEmptyToggle;
    } else if (isWithin(startX + 20, startX + 24, startY + 6, startY + 10, mouseX, mouseY)) {
      fluidEmptyToggle = !fluidEmptyToggle;
    } else if (isWithin(startX + 27, startX + 45, startY + 34, startY + 44, mouseX, mouseY)) {
      drawTexturedModalRect(startX + 27, startY + 34, 227, 0, 18, 6);
      if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
        fluidEmptyToggle = true;
        soilEmptyToggle = true;
        fertilizerEmptyToggle = true;
      }
      handleEmpty();
      fluidEmptyToggle = false;
      soilEmptyToggle = false;
      fertilizerEmptyToggle = false;
    }
  }

  private void handleEmpty() {
    NetworkHandler.sendToServer(
        new EmptyFarmPacket(fluidEmptyToggle, soilEmptyToggle, fertilizerEmptyToggle, te.getPos()));
  }
}
