package com.wurmcraft.minecraftnotincluded.common.world.overworld.cavern;

import static io.github.opencubicchunks.cubicchunks.api.util.Coords.cubeToCenterBlock;
import static io.github.opencubicchunks.cubicchunks.api.util.Coords.cubeToMinBlock;
import static io.github.opencubicchunks.cubicchunks.api.util.Coords.localToBlock;
import static io.github.opencubicchunks.cubicchunks.cubicgen.StructureGenUtil.normalizedDistance;
import static java.lang.Math.max;
import static net.minecraft.util.math.MathHelper.cos;
import static net.minecraft.util.math.MathHelper.floor;
import static net.minecraft.util.math.MathHelper.sin;

import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.api.worldgen.structure.ICubicStructureGenerator;
import io.github.opencubicchunks.cubicchunks.cubicgen.StructureGenUtil;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

/**
 * A modified version of CubicCaveGenerator by Bartek2x from
 * https://github.com/OpenCubicChunks/CubicWorldGen/blob/MC_1.12/src/main/java/io/github/opencubicchunks/cubicchunks/cubicgen/customcubic/structure/CubicCaveGenerator.java
 * Author: https://github.com/Barteks2x
 */
public class CavernGenerator implements ICubicStructureGenerator {

  private static final int CAVE_RARITY = 45 * 7 / (2 * 2 * 2);
  private static final int MAX_INIT_NODES = 48;
  private static final int LARGE_NODE_RARITY = 4;
  private static final int LARGE_NODE_MAX_BRANCHES = 6;
  private static final int BIG_CAVE_RARITY = 6;
  private static final double CAVE_SIZE_ADD = 10D;
  private static final int STEEP_STEP_RARITY = 10;
  private static final float FLATTEN_FACTOR = 0.3f;
  private static final float STEEPER_FLATTEN_FACTOR = 0.92f;
  private static final float DIRECTION_CHANGE_FACTOR = 0.1f;
  private static final float PREV_HORIZ_DIRECTION_CHANGE_WEIGHT = 0.75f;
  private static final float PREV_VERT_DIRECTION_CHANGE_WEIGHT = 0.9f;
  private static final float MAX_ADD_DIRECTION_CHANGE_HORIZ = 4.0f;
  private static final float MAX_ADD_DIRECTION_CHANGE_VERT = 2.0f;
  private static final int CARVE_STEP_RARITY = 4;
  private static final double CAVE_FLOOR_DEPTH = -0.7;

  private static final int RANGE = 8;

  private boolean spawnSet;

  @Override
  public void generate(World world, CubePrimer cube, CubePos cubePos) {
    this.generate(world, cube, cubePos, this::generate, RANGE, RANGE, 1, 1);
  }

  private static final Predicate<IBlockState> isBlockReplaceable =
      (state ->
          state.getBlock() == Blocks.STONE
              || state.getBlock() == Blocks.DIRT
              || state.getBlock() == Blocks.GRASS);

  public void generate(
      World world,
      Random rand,
      CubePrimer cube,
      int cubeXOrigin,
      int cubeYOrigin,
      int cubeZOrigin,
      CubePos generatedCubePos) {
    if (rand.nextInt(CAVE_RARITY) != 0) {
      return;
    }
    if (!spawnSet) {
      BlockPos pos =
          new BlockPos(
              cubeToCenterBlock(cubeXOrigin),
              cubeToCenterBlock(cubeYOrigin),
              cubeToCenterBlock(cubeZOrigin));
      world.setSpawnPoint(pos);
      spawnSet = true;
    }
    // very low probability of generating high number
    int nodes = rand.nextInt(rand.nextInt(rand.nextInt(MAX_INIT_NODES + 1) + 1) + 1);

    for (int node = 0; node < nodes; ++node) {
      double branchStartX = localToBlock(cubeXOrigin, rand.nextInt(ICube.SIZE));
      double branchStartY = localToBlock(cubeYOrigin, rand.nextInt(ICube.SIZE));
      double branchStartZ = localToBlock(cubeZOrigin, rand.nextInt(ICube.SIZE));
      int subBranches = 1;

      if (rand.nextInt(LARGE_NODE_RARITY) == 0) {
        this.generateLargeNode(
            cube,
            rand,
            rand.nextLong(),
            generatedCubePos,
            branchStartX,
            branchStartY,
            branchStartZ);
        subBranches += rand.nextInt(LARGE_NODE_MAX_BRANCHES);
      }

      for (int branch = 0; branch < subBranches; ++branch) {
        float horizDirAngle = rand.nextFloat() * (float) Math.PI * 2.0F;
        float vertDirAngle = (rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
        float baseHorizSize = rand.nextFloat() * 2.0F + rand.nextFloat();

        if (rand.nextInt(BIG_CAVE_RARITY) == 0) {
          baseHorizSize *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
        }

        int startWalkedDistance = 0;
        int maxWalkedDistance = 0;
        double vertCaveSizeMod = 1.0;

        this.generateNode(
            cube,
            rand.nextLong(),
            generatedCubePos,
            branchStartX,
            branchStartY,
            branchStartZ,
            baseHorizSize,
            horizDirAngle,
            vertDirAngle,
            startWalkedDistance,
            maxWalkedDistance,
            vertCaveSizeMod);
      }
    }
  }

  /** Generates a flattened cave "room", usually more caves split off it */
  private void generateLargeNode(
      CubePrimer cube,
      Random rand,
      long seed,
      CubePos generatedCubePos,
      double x,
      double y,
      double z) {
    float baseHorizSize = 1.0F + rand.nextFloat() * 6.0F;
    float horizDirAngle = 0;
    float vertDirAngle = 0;

    int startWalkedDistance = -1;
    int maxWalkedDistance = -1;
    double vertCaveSizeMod = 0.5;
    this.generateNode(
        cube,
        seed,
        generatedCubePos,
        x,
        y,
        z,
        baseHorizSize,
        horizDirAngle,
        vertDirAngle,
        startWalkedDistance,
        maxWalkedDistance,
        vertCaveSizeMod);
  }

  /**
   * Recursively generates a node in the current cave system tree.
   *
   * @param cube block buffer to modify
   * @param seed random seed to use
   * @param generatedCubePos position of the cube to modify
   * @param caveX starting x coordinate of the cave
   * @param caveY starting Y coordinate of the cave
   * @param caveZ starting Z coordinate of the cave
   * @param baseCaveSize initial value for cave size, size decreases as cave goes further
   * @param horizDirAngle horizontal direction angle
   * @param vertCaveSizeMod vertical direction angle
   * @param startWalkedDistance the amount of steps the cave already went forwards, used in
   *     recursive step. -1 means that there will be only one step
   * @param maxWalkedDistance maximum distance the cave can go forwards, <= 0 to use default
   * @param vertDirAngle changes vertical size of the cave, values < 1 result in flattened caves, >
   *     1 result in vertically stretched caves
   */
  private void generateNode(
      CubePrimer cube,
      long seed,
      CubePos generatedCubePos,
      double caveX,
      double caveY,
      double caveZ,
      float baseCaveSize,
      float horizDirAngle,
      float vertDirAngle,
      int startWalkedDistance,
      int maxWalkedDistance,
      double vertCaveSizeMod) {
    Random rand = new Random(seed);

    // store by how much the horizontal and vertical direction angles will change each step
    float horizDirChange = 0.0F;
    float vertDirChange = 0.0F;

    if (maxWalkedDistance <= 0) {
      int maxBlockRadius = cubeToMinBlock(RANGE - 1);
      maxWalkedDistance = maxBlockRadius - rand.nextInt(maxBlockRadius / 4);
    }

    // if true - this branch won't generate new sub-branches
    boolean finalStep = false;

    int walkedDistance;
    if (startWalkedDistance == -1) {
      // generate a cave "room"
      // start at half distance towards the end = max cave size
      walkedDistance = maxWalkedDistance / 2;
      finalStep = true;
    } else {
      walkedDistance = startWalkedDistance;
    }

    int splitPoint = rand.nextInt(maxWalkedDistance / 2) + maxWalkedDistance / 4;

    for (; walkedDistance < maxWalkedDistance; ++walkedDistance) {
      float fractionWalked = walkedDistance / (float) maxWalkedDistance;
      // horizontal and vertical size of the cave
      // size starts small and increases, then decreases as cave goes further
      double caveSizeHoriz = CAVE_SIZE_ADD + sin(fractionWalked * (float) Math.PI) * baseCaveSize;
      double caveSizeVert = caveSizeHoriz * vertCaveSizeMod;

      // Walk forward a single step:

      // from sin(alpha)=y/r and cos(alpha)=x/r ==> x = r*cos(alpha) and y = r*sin(alpha)
      // always moves by one block in some direction

      // here x is xzDirectionFactor, y is yDirectionFactor
      float xzDirectionFactor = cos(vertDirAngle);
      float yDirectionFactor = sin(vertDirAngle);

      // here y is directionZ and x is directionX
      caveX += cos(horizDirAngle) * xzDirectionFactor;
      caveY += yDirectionFactor;
      caveZ += sin(horizDirAngle) * xzDirectionFactor;

      if (rand.nextInt(STEEP_STEP_RARITY) == 0) {
        vertDirAngle *= STEEPER_FLATTEN_FACTOR;
      } else {
        vertDirAngle *= FLATTEN_FACTOR;
      }

      // change the direction
      vertDirAngle += vertDirChange * DIRECTION_CHANGE_FACTOR;
      horizDirAngle += horizDirChange * DIRECTION_CHANGE_FACTOR;
      // update direction change angles
      vertDirChange *= PREV_VERT_DIRECTION_CHANGE_WEIGHT;
      horizDirChange *= PREV_HORIZ_DIRECTION_CHANGE_WEIGHT;
      vertDirChange +=
          (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * MAX_ADD_DIRECTION_CHANGE_VERT;
      horizDirChange +=
          (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * MAX_ADD_DIRECTION_CHANGE_HORIZ;

      // if we reached split point - try to split
      // can split only if it's not final branch and the cave is still big enough (>1 block radius)
      if (!finalStep && walkedDistance == splitPoint && baseCaveSize > 1.0F) {
        this.generateNode(
            cube,
            rand.nextLong(),
            generatedCubePos,
            caveX,
            caveY,
            caveZ,
            rand.nextFloat() * 0.5F + 0.5F, // base cave size
            horizDirAngle - ((float) Math.PI / 2F), // horiz. angle - subtract 90 degrees
            vertDirAngle / 3.0F,
            walkedDistance,
            maxWalkedDistance,
            1.0D);
        this.generateNode(
            cube,
            rand.nextLong(),
            generatedCubePos,
            caveX,
            caveY,
            caveZ,
            rand.nextFloat() * 0.5F + 0.5F, // base cave size
            horizDirAngle + ((float) Math.PI / 2F), // horiz. angle - add 90 degrees
            vertDirAngle / 3.0F,
            walkedDistance,
            maxWalkedDistance,
            1.0D);
        return;
      }

      // carve blocks only on some percentage of steps, unless this is the final branch
      if (rand.nextInt(CARVE_STEP_RARITY) == 0 && !finalStep) {
        continue;
      }

      double xDist = caveX - generatedCubePos.getXCenter();
      double yDist = caveY - generatedCubePos.getYCenter();
      double zDist = caveZ - generatedCubePos.getZCenter();
      double maxStepsDist = maxWalkedDistance - walkedDistance;
      // CHANGE: multiply max(1, vertCaveSizeMod)
      double maxDistToCube = baseCaveSize * max(1, vertCaveSizeMod) + CAVE_SIZE_ADD + ICube.SIZE;

      if (xDist * xDist + yDist * yDist + zDist * zDist - maxStepsDist * maxStepsDist
          > maxDistToCube * maxDistToCube) {
        return;
      }

      tryCarveBlocks(cube, generatedCubePos, caveX, caveY, caveZ, caveSizeHoriz, caveSizeVert);
      if (finalStep) {
        return;
      }
    }
  }

  // returns true if cave generation should be continued
  private void tryCarveBlocks(
      CubePrimer cube,
      CubePos generatedCubePos,
      double caveX,
      double caveY,
      double caveZ,
      double caveSizeHoriz,
      double caveSizeVert) {
    double genCubeCenterX = generatedCubePos.getXCenter();
    double genCubeCenterY = generatedCubePos.getYCenter();
    double genCubeCenterZ = generatedCubePos.getZCenter();

    if (caveX < genCubeCenterX - ICube.SIZE - caveSizeHoriz * 2.0D
        || caveY < genCubeCenterY - ICube.SIZE - caveSizeVert * 2.0D
        || caveZ < genCubeCenterZ - ICube.SIZE - caveSizeHoriz * 2.0D
        || caveX > genCubeCenterX + ICube.SIZE + caveSizeHoriz * 2.0D
        || caveY > genCubeCenterY + ICube.SIZE + caveSizeVert * 2.0D
        || caveZ > genCubeCenterZ + ICube.SIZE + caveSizeHoriz * 2.0D) {
      return;
    }
    int minLocalX = floor(caveX - caveSizeHoriz) - generatedCubePos.getMinBlockX() - 1;
    int maxLocalX = floor(caveX + caveSizeHoriz) - generatedCubePos.getMinBlockX() + 1;
    int minLocalY = floor(caveY - caveSizeVert) - generatedCubePos.getMinBlockY() - 1;
    int maxLocalY = floor(caveY + caveSizeVert) - generatedCubePos.getMinBlockY() + 1;
    int minLocalZ = floor(caveZ - caveSizeHoriz) - generatedCubePos.getMinBlockZ() - 1;
    int maxLocalZ = floor(caveZ + caveSizeHoriz) - generatedCubePos.getMinBlockZ() + 1;

    // skip is if everything is outside of that cube
    if (maxLocalX <= 0
        || minLocalX >= ICube.SIZE
        || maxLocalY <= 0
        || minLocalY >= ICube.SIZE
        || maxLocalZ <= 0
        || minLocalZ >= ICube.SIZE) {
      return;
    }
    StructureBoundingBox boundingBox =
        new StructureBoundingBox(minLocalX, minLocalY, minLocalZ, maxLocalX, maxLocalY, maxLocalZ);

    StructureGenUtil.clampBoundingBoxToLocalCube(boundingBox);

    boolean hitLiquid =
        StructureGenUtil.scanWallsForBlock(
            cube,
            boundingBox,
            (b) -> b.getBlock() == Blocks.LAVA || b.getBlock() == Blocks.FLOWING_LAVA);

    if (!hitLiquid) {
      carveBlocks(
          cube, generatedCubePos, caveX, caveY, caveZ, caveSizeHoriz, caveSizeVert, boundingBox);
    }
  }

  private void carveBlocks(
      CubePrimer cube,
      CubePos generatedCubePos,
      double caveX,
      double caveY,
      double caveZ,
      double caveSizeHoriz,
      double caveSizeVert,
      StructureBoundingBox boundingBox) {

    int generatedCubeX = generatedCubePos.getX();
    int generatedCubeY = generatedCubePos.getY();
    int generatedCubeZ = generatedCubePos.getZ();

    int minX = boundingBox.minX;
    int maxX = boundingBox.maxX;
    int minY = boundingBox.minY;
    int maxY = boundingBox.maxY;
    int minZ = boundingBox.minZ;
    int maxZ = boundingBox.maxZ;

    for (int localX = minX; localX < maxX; ++localX) {
      double distX = normalizedDistance(generatedCubeX, localX, caveX, caveSizeHoriz);

      for (int localZ = minZ; localZ < maxZ; ++localZ) {
        double distZ = normalizedDistance(generatedCubeZ, localZ, caveZ, caveSizeHoriz);

        if (distX * distX + distZ * distZ >= 1.0D) {
          continue;
        }
        for (int localY = minY; localY < maxY; ++localY) {
          double distY = normalizedDistance(generatedCubeY, localY, caveY, caveSizeVert);

          IBlockState state = cube.getBlockState(localX, localY, localZ);

          if (!isBlockReplaceable.test(state)) {
            continue;
          }

          if (shouldCarveBlock(distX, distY, distZ)) {
            // No lava generation, infinite depth. Lava will be generated differently (or not
            // generated)
            cube.setBlockState(localX, localY, localZ, Blocks.AIR.getDefaultState());
          } else if (state.getBlock() == Blocks.DIRT) {
            // vanilla dirt-grass replacement works by scanning top-down and moving the block
            // cubic chunks needs to be a bit more hacky about it
            // instead of keeping track of the encountered grass block
            // cubic chunks replaces any dirt block (it's before population, no ore-like dirt
            // formations yet)
            // with grass, if the block above would be deleted by this cave generator step
            double distYAbove = normalizedDistance(generatedCubeY, localY + 1, caveY, caveSizeVert);
            if (shouldCarveBlock(distX, distYAbove, distZ)) {
              cube.setBlockState(localX, localY, localZ, Blocks.GRASS.getDefaultState());
            }
          }
        }
      }
    }
  }

  private static boolean shouldCarveBlock(double distX, double distY, double distZ) {
    // distY > CAVE_FLOOR_DEPTH --> flattened floor
    return distY > CAVE_FLOOR_DEPTH && distX * distX + distY * distY + distZ * distZ < 1.0D;
  }
}
