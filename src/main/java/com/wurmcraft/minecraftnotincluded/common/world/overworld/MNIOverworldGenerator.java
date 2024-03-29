package com.wurmcraft.minecraftnotincluded.common.world.overworld;

import com.wurmcraft.minecraftnotincluded.common.biome.BiomeRegistry;
import com.wurmcraft.minecraftnotincluded.common.world.generation.OreGenerator;
import com.wurmcraft.minecraftnotincluded.common.world.overworld.biome.MNIBiomeSource;
import com.wurmcraft.minecraftnotincluded.common.world.overworld.biome.ModdedBiomeProvider;
import io.github.opencubicchunks.cubicchunks.api.util.Coords;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.core.CubicChunks;
import io.github.opencubicchunks.cubicchunks.cubicgen.BasicCubeGenerator;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.BiomeBlockReplacerConfig;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.CubicBiome;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class MNIOverworldGenerator extends BasicCubeGenerator {

  private MNIBiomeSource biomeSource;
  private OverworldTerrainBuilder terrainBuilder;
  private OreGenerator oreGenerator;

  public MNIOverworldGenerator(World world) {
    this(world, world.getBiomeProvider());
  }

  public MNIOverworldGenerator(World world, BiomeProvider provider) {
    super(world);
    BiomeBlockReplacerConfig replaceConfig = new BiomeBlockReplacerConfig();
    replaceConfig.fillDefaults();
    biomeSource =
        new MNIBiomeSource(
            world,
            new ModdedBiomeProvider(
                ModdedBiomeProvider.getGeneratorSettings(
                    world, "{\n" + "  \"biomeSize\": 7,\n" + "  \"riverSize\": 0\n" + "}")));
    terrainBuilder = new OverworldTerrainBuilder(world);
    oreGenerator = new OreGenerator(world);
    // Update Build Height
    FMLCommonHandler.instance()
        .getMinecraftServerInstance()
        .setBuildLimit(CubicChunks.MAX_SUPPORTED_BLOCK_Y);
  }

  @Override
  public CubePrimer generateCube(int cubeX, int cubeY, int cubeZ) {
    CubePrimer cp = new CubePrimer();
    fillBiomes(cp, cubeX, cubeY, cubeZ);
    terrainBuilder.createPrimer(cp, cubeX, cubeY, cubeZ);
    return cp;
  }

  private void fillBiomes(CubePrimer cp, int cubeX, int cubeY, int cubeZ) {
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        for (int z = 0; z < 4; z++) {
          cp.setBiome(x, y, z, BiomeRegistry.barron);
        }
      }
    }
  }

  @Override
  public void populate(ICube cube) {
    terrainBuilder.applyBiomeFilter(cube.getWorld(), cube);
    CubicBiome cubicBiome =
        CubicBiome.getCubic(cube.getWorld().getBiome(Coords.getCubeCenter(cube)));
    cubicBiome
        .getBiome()
        .decorate(cube.getWorld(), cube.getWorld().rand, cube.getCoords().getCenterBlockPos());
    terrainBuilder.addLighting(cubicBiome, cube);
    oreGenerator.generate(world, world.rand, cube.getCoords(), cubicBiome.getBiome());
  }
}
