package com.wurmcraft.minecraftnotincluded.common.world.generation.type;

import com.wurmcraft.minecraftnotincluded.common.references.json.OreConfig.OreGeneration;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.ICubicPopulator;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

public class VeinOreGenerator extends WorldGenerator implements ICubicPopulator {

  public final IBlockState oreBlock;
  private final int amount;
  private OreGeneration oreGen;

  public VeinOreGenerator(IBlockState oreBlock, int amount, OreGeneration oreGen) {
    this.oreBlock = oreBlock;
    this.amount = amount;
    this.oreGen = oreGen;
  }

  @Override
  public boolean generate(World world, Random rand, BlockPos position) {
    generate(
        world,
        rand,
        new CubePos(position.getX() / 16, position.getY() / 16, position.getZ() / 16),
        world.getBiome(position));
    return true;
  }

  @Override
  public void generate(World world, Random rand, CubePos cubePos, Biome biome) {
    BlockPos pos = cubePos.getMinBlockPos();
    int radius = 5 + rand.nextInt(4);
    int rarity = 5 / oreGen.rarity;
    BlockPos core = pos.add(8, 0, 8);
    int totalGenerated = 0;
    if (rarity > 2) {
      radius = 2 + rand.nextInt(2);
    }
    if (radius > 8) {
      radius = 8;
    }
    do {
      for (int x = 0; x < radius; x++) {
        for (int z = 0; z < radius; z++) {
          for (int y = pos.getY(); y < pos.getY() + (radius * 2); y++) {
            BlockPos[] oreLoc =
                new BlockPos[] {
                  new BlockPos(core.getX() + x, y, core.getZ() + z),
                  new BlockPos(core.getX() + x, y, core.getZ() - z),
                  new BlockPos(core.getX() - x, y, core.getZ() + z),
                  new BlockPos(core.getX() - x, y, core.getZ() - z)
                };
            for (BlockPos tempPos : oreLoc) {
              totalGenerated++;
              double disFromCore = calcDistanceFromCore(core, tempPos, radius);
              double chance;
              if (disFromCore < .3) {
                chance = .8;
              } else {
                chance = ((rarity - disFromCore) / (disFromCore >= .3 ? 4 : 8 + rand.nextInt(4)));
              }
              if (rand.nextDouble() < chance) {
                IBlockState state = world.getBlockState(tempPos);
                if (state.getBlock().isReplaceable(world, pos)) {
                  world.setBlockState(tempPos, oreBlock, 2);
                }
              }
            }
          }
        }
      }
    } while (totalGenerated <= amount);
  }

  private double calcDistanceFromCore(BlockPos center, BlockPos pos, int radius) {
    double a = center.getX() - pos.getX();
    double b = center.getY() - pos.getY();
    double c = center.getZ() - pos.getZ();
    double distance = Math.sqrt(a * a + b * b + c * c);
    return distance / radius;
  }
}
