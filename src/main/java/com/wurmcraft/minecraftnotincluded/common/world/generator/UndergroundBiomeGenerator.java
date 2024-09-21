package com.wurmcraft.minecraftnotincluded.common.world.generator;

import com.wurmcraft.minecraftnotincluded.common.world.structure.UndergroundPocketGenerator;
import io.github.opencubicchunks.cubicchunks.api.util.Box;
import io.github.opencubicchunks.cubicchunks.api.util.Coords;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.structure.ICubicStructureGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.structure.event.InitCubicStructureGeneratorEvent;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.InitMapGenEvent;

import javax.annotation.Nullable;
import java.util.List;

public class UndergroundBiomeGenerator implements ICubeGenerator {

    private World world;
    private Biome[] biomes;
    private ICubicStructureGenerator caveGenerator;

    public UndergroundBiomeGenerator(World world) {
        this.world = world;
        caveGenerator = new InitCubicStructureGeneratorEvent(InitMapGenEvent.EventType.CAVE, new UndergroundPocketGenerator(), world).getNewGen();
        this.world.setSpawnPoint(new BlockPos(0,0,0));
        world.setSpawnPoint(new BlockPos(0,0,0));
    }

    @Override
    public CubePrimer generateCube(int cubeX, int cubeY, int cubeZ) {
        return generateCube(cubeX, cubeY, cubeZ, CubePrimer.createFilled(Blocks.STONE.getDefaultState()));
    }

    @Override
    public CubePrimer generateCube(int cubeX, int cubeY, int cubeZ, CubePrimer primer) {
        if(primer.getBlockState(0,0,0) != Blocks.STONE) {
            primer = CubePrimer.createFilled(Blocks.STONE.getDefaultState());
        }
        caveGenerator.generate(world, primer, new CubePos(cubeX, cubeY, cubeZ));
        return primer;
    }

    // From Cubic Chunks World Gen
    @Override
    public void generateColumn(Chunk chunk) {
        this.biomes = this.world.getBiomeProvider()
                .getBiomes(this.biomes,
                        Coords.cubeToMinBlock(chunk.x),
                        Coords.cubeToMinBlock(chunk.z),
                        ICube.SIZE, ICube.SIZE);

        // Copy ids to column internal biome array
        byte[] columnBiomeArray = chunk.getBiomeArray();
        for (int i = 0; i < columnBiomeArray.length; ++i) {
            columnBiomeArray[i] = (byte) Biome.getIdForBiome(this.biomes[i]);
        }
    }

    @Override
    public void populate(ICube iCube) {

    }

    @Override
    public Box getFullPopulationRequirements(ICube iCube) {
        return RECOMMENDED_FULL_POPULATOR_REQUIREMENT;
    }

    @Override
    public Box getPopulationPregenerationRequirements(ICube iCube) {
        return RECOMMENDED_GENERATE_POPULATOR_REQUIREMENT;
    }

    @Override
    public void recreateStructures(ICube iCube) {

    }

    @Override
    public void recreateStructures(Chunk chunk) {

    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        return world.getBiome(pos).getSpawnableList(type);
    }

    @Nullable
    @Override
    public BlockPos getClosestStructure(String s, BlockPos blockPos, boolean b) {
        return null;
    }
}
