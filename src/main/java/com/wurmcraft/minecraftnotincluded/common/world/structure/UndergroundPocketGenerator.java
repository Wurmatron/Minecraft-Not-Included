package com.wurmcraft.minecraftnotincluded.common.world.structure;

import com.flowpowered.noise.module.modifier.ScaleBias;
import com.flowpowered.noise.module.source.Perlin;
import com.wurmcraft.minecraftnotincluded.MinecraftNotIncluded;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.api.worldgen.structure.ICubicStructureGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.ArrayList;
import java.util.Random;

public class UndergroundPocketGenerator implements ICubicStructureGenerator {

    private static final int RARITY = 25000;

    private static final int MIN_HEIGHT = 20;
    private static final int MAX_HEIGHT = 40;
    private static final int MIN_WIDTH = 300;
    private static final int MAX_WIDTH = 400;
    private static final int MIN_LENGTH = 300;
    private static final int MAX_LENGTH = 400;

    private static final int MIN_DISTANCE_BETWEEN = 32;

    private static ArrayList<int[]> boxCache = new ArrayList<>();

    @Override
    public void generate(World w, CubePrimer cube, CubePos pos) {
        Random rand = w.rand;
        if (boxCache.isEmpty()) {
            attemptToPlaceUndergroundBiome(w, rand, cube, pos);
            w.setSpawnPoint(new BlockPos((pos.getMinBlockX() << 4) + 8, pos.getMinBlockY() << 4, (pos.getMaxBlockZ() << 4) + 8));
        }
        if (rand.nextInt(RARITY) == 0) {
            attemptToPlaceUndergroundBiome(w, rand, cube, pos);
        }

        for (int[] box : boxCache) {
            attemptToCarve(w, cube, pos, box);
        }
    }

    private static void attemptToPlaceUndergroundBiome(World w, Random rand, CubePrimer cube, CubePos pos) {
        int xSize = MIN_WIDTH + rand.nextInt(MAX_WIDTH - MIN_WIDTH);
        int ySize = MIN_HEIGHT + rand.nextInt(MAX_HEIGHT - MIN_HEIGHT);
        int zSize = MIN_LENGTH + rand.nextInt(MAX_LENGTH - MIN_LENGTH);
        int[] box = new int[] {
                pos.getMinBlockX() - xSize / 2,
                pos.getMinBlockY() - ySize / 2,
                pos.getMinBlockZ() - zSize / 2,
                pos.getMinBlockZ() + xSize / 2,
                pos.getMinBlockZ() + ySize / 2,
                pos.getMinBlockZ() + zSize / 2
        };
        if (!isAnotherNearby(box)) {
            boxCache.add(box);
            MinecraftNotIncluded.logger.info("Placing Underground biome centered at [" + pos.getMinBlockX() + "," + pos.getMinBlockY() + "," + pos.getMinBlockZ() + "]" );
        }
    }

    private static boolean isAnotherNearby(int[] box) {
        for(int[] other : boxCache) {
            if(isWithin(other[0], other[1], other[2], box) || isWithin(box[3], box[4], box[5], box)) {
                return true;
            }
            if(isWithin(other[0] - MIN_DISTANCE_BETWEEN, other[1] - MIN_DISTANCE_BETWEEN, other[2] - MIN_DISTANCE_BETWEEN, box) || isWithin(box[3] + MIN_DISTANCE_BETWEEN, box[4] + MIN_DISTANCE_BETWEEN, box[5] + MIN_DISTANCE_BETWEEN, box)) {
                return true;
            }
        }
        return false;
    }

    private static void attemptToCarve(World w, CubePrimer cube, CubePos pos, int[] box) {
        for (int x = 0; x < ICube.SIZE; x++)
            for (int y = 0; y < ICube.SIZE; y++)
                for (int z = 0; z < ICube.SIZE; z++) {
                    if (isWithin((pos.getX() << 4) + x, (pos.getY() << 4) + y, (pos.getZ() << 4) + z, box))
                        cube.setBlockState(x, y, z, getBlock(pos.getX() << 4, pos.getY() << 4, pos.getZ() << 4, box));
                }
    }

    private static IBlockState getBlock(int x, int y, int z, int[] box) {
        return Blocks.AIR.getDefaultState();
    }

    private static boolean isWithin(int x, int y, int z, int[] box) {
        return isBetween(x, box[0], box[3]) && isBetween(y, box[1], box[4]) && isBetween(z, box[2], box[5]);
    }

    private static boolean isBetween(int toCheck, int from, int to) {
        return from <= toCheck && toCheck <= to;
    }
}
