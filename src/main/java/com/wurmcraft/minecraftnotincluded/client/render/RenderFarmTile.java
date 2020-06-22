package com.wurmcraft.minecraftnotincluded.client.render;

import com.wurmcraft.minecraftnotincluded.common.tile.TileEntityFarm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFarmTile extends TileEntitySpecialRenderer<TileEntityFarm> {

  @Override
  public void render(
      TileEntityFarm te,
      double x,
      double y,
      double z,
      float partialTicks,
      int destroyStage,
      float alpha) {
    super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    if (te.getFarmable() != null) {
      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
      GlStateManager.pushMatrix();
      double scale = .75 * te.getGrowthPercentage();
      GlStateManager.scale(scale, scale, scale);
      double reverseScale = 1 / scale;
      double xCenter = (.5 * (1 - scale));
      double zCenter = 1 - (.5 * (1 - scale));
      GlStateManager.translate(
          ((x + xCenter) * reverseScale), ((y + 1) * reverseScale), ((z + zCenter) * reverseScale));
      for (BlockPos pos : te.getFarmable().getCropForRendering().keySet()) {
        GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
        renderer.renderBlockBrightness(
            te.getFarmable().getCropForRendering().get(pos),
            te.getWorld().getCombinedLight(new BlockPos(x, y, z), 15));
      }
      GlStateManager.popMatrix();
    }
  }
}
