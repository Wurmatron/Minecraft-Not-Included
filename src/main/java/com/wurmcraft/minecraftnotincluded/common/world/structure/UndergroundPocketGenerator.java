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

import java.util.ArrayList;
import java.util.Random;

public class UndergroundPocketGenerator implements ICubicStructureGenerator {

    private static final int RARITY = 1000;

    private static final int MIN_HEIGHT = 30;
    private static final int MAX_HEIGHT = 65;
    private static final int MIN_WIDTH = 300;
    private static final int MAX_WIDTH = 500;
    private static final int MIN_LENGTH = 300;
    private static final int MAX_LENGTH = 500;

    private static ArrayList<int[]> boxCache = new ArrayList<>();

    @Override
    public void generate(World w, CubePrimer cube, CubePos pos) {
        Random rand = w.rand;
        if (boxCache.isEmpty()) {
            attemptToPlaceUndergroundBiome(w, rand, cube, pos);
            w.setSpawnPoint(new BlockPos(pos.getX() << 4, pos.getY() << 4, pos.getZ() << 4));
        }
//        if(rand.nextInt(RARITY) == 0) { TODO Fix Spawns way to close
//            attemptToPlaceUndergroundBiome(w, rand, cube, pos);
//        }

        for (int[] box : boxCache) {
                attemptToCarve(w, cube, pos, box);
        }
    }

    private static void attemptToPlaceUndergroundBiome(World w, Random rand, CubePrimer cube, CubePos pos) {
        int height = MIN_HEIGHT + rand.nextInt(MAX_HEIGHT - MIN_HEIGHT);
        int width = MIN_WIDTH + rand.nextInt(MAX_WIDTH - MIN_WIDTH);
        int length = MIN_LENGTH + rand.nextInt(MAX_LENGTH - MIN_LENGTH);
        int[] box = new int[]{(pos.getX() << 4) - (width / 2),(pos.getY() << 4) - (height / 2), (pos.getZ() << 4) - (length / 2), (pos.getX() << 4) + (width / 2) + 1, (pos.getY() << 4) + (height / 2) + 1, (pos.getZ() >> 4) + (length / 2) + 1};
        boxCache.add(box);
        MinecraftNotIncluded.logger.info("Placing Underground Biome between [" + box[0] + ", " + box[1] + ", " + box[2] + "] and [" + box[3] + ", " + box[4] + ", " + box[5] + "] centered at [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]");
        // TODO Save? For Compass lookup
        attemptToCarve(w, cube, pos, box);
    }

    private static void attemptToCarve(World w, CubePrimer cube, CubePos pos, int[] box) {
        for (int x = 0; x < ICube.SIZE; x++)
            for (int y = 0; y < ICube.SIZE; y++)
                for (int z = 0; z < ICube.SIZE; z++) {
                    if (isWithin((pos.getX() << 4) + x, (pos.getY() << 4) + y, (pos.getZ() << 4) + z, box))
                        cube.setBlockState(x,y,z, getBlock(pos.getX()<<4,pos.getY()<<4,pos.getZ()<<4, box));
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
