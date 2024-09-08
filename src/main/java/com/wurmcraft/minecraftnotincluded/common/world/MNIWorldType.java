package com.wurmcraft.minecraftnotincluded.common.world;

import com.wurmcraft.minecraftnotincluded.common.world.generator.UndergroundBiomeGenerator;
import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;

import javax.annotation.Nullable;

public class MNIWorldType extends WorldType implements ICubicWorldType {

    public MNIWorldType() {
        super("MNI");
    }

    @Nullable
    @Override
    public ICubeGenerator createCubeGenerator(World w) {
        return new UndergroundBiomeGenerator(w);
    }

    @Override
    public IntRange calculateGenerationHeightRange(WorldServer s) {
        return new IntRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public boolean hasCubicGeneratorForWorld(World w) {
        return true;
    }
}
