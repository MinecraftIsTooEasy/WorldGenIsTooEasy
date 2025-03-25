package rwg.world;


import net.minecraft.*;
import rwg.util.FastNoise;
import rwg.util.WorleyUtil;

import java.util.Random;

public class WorleyCavesGenerator extends MapGenBase2 {
    private MapGenBase2 surfaceCaves = new MapGenSurfaceCaves();
    private WorleyUtil worleyF1divF3;
    private FastNoise displacementNoisePerlin;
    private static Block lava;
    private static int maxCaveHeight;
    private static int minCaveHeight;
    private static float noiseCutoff;
    private static float warpAmplifier;
    private static float easeInDepth;
    private static float yCompression;
    private static float xzCompression;
    private static float surfaceCutoff;
    private static int lavaDepth;
    private static int HAS_CAVES_FLAG = 129;

    public WorleyCavesGenerator(long seed) {
        int seed2 = new Random(seed).nextInt();
        worleyF1divF3 = new WorleyUtil(seed2);
        worleyF1divF3.SetFrequency(0.016f);

        displacementNoisePerlin = new FastNoise(seed2);
        displacementNoisePerlin.SetNoiseType(FastNoise.NoiseType.Perlin);
        displacementNoisePerlin.SetFrequency(0.05f);

        maxCaveHeight = 128;
        minCaveHeight = 1;
        noiseCutoff = -0.14f;
        warpAmplifier = 8.0f;
        easeInDepth = 15;
        yCompression = 2.0f;
        xzCompression = 1.0f;
        surfaceCutoff = -0.081f;
        lavaDepth = 6;

        lava = Block.lavaStill;
    }

    @Override
    public void generate(IChunkProvider provider, World worldIn, int x, int z, Block[] Block) {
        ChunkPrimer primer = new ChunkPrimer(Block);
        int currentDim = worldIn.provider.dimensionId;
        this.worldObj = worldIn;

        if (currentDim != 0) {
            return;
        }

        this.generateWorleyCaves(worldIn, x, z, primer);
        this.surfaceCaves.generate(provider, worldIn, x, z, Block);
    }

    protected void generateWorleyCaves(World worldIn, int chunkX, int chunkZ, ChunkPrimer chunkPrimerIn) {
        int chunkMaxHeight = getMaxSurfaceHeight(chunkPrimerIn);
        int seaLevel = 63;
        float[][][] samples = sampleNoise(chunkX, chunkZ, chunkMaxHeight + 1);
        float oneQuarter = 0.25F;
        float oneHalf = 0.5F;
        BiomeGenBase currentBiome;

        for (int x = 0; x < 4; x++) {
            //each chunk divided into 4 subchunks along Z axis
            for (int z = 0; z < 4; z++) {
                int depth = 0;

                //don't bother checking all the other logic if there's nothing to dig in this column
                if (samples[x][HAS_CAVES_FLAG][z] == 0 && samples[x + 1][HAS_CAVES_FLAG][z] == 0 && samples[x][HAS_CAVES_FLAG][z + 1] == 0 && samples[x + 1][HAS_CAVES_FLAG][z + 1] == 0)
                    continue;

                //each chunk divided into 128 subchunks along Y axis. Need lots of y sample points to not break things
                for (int y = (maxCaveHeight / 2) - 1; y >= 0; y--) {
                    //grab the 8 sample points needed from the noise values
                    float x0y0z0 = samples[x][y][z];
                    float x0y0z1 = samples[x][y][z + 1];
                    float x1y0z0 = samples[x + 1][y][z];
                    float x1y0z1 = samples[x + 1][y][z + 1];
                    float x0y1z0 = samples[x][y + 1][z];
                    float x0y1z1 = samples[x][y + 1][z + 1];
                    float x1y1z0 = samples[x + 1][y + 1][z];
                    float x1y1z1 = samples[x + 1][y + 1][z + 1];

                    //how much to increment noise along y value
                    //linear interpolation from start y and end y
                    float noiseStepY00 = (x0y1z0 - x0y0z0) * -oneHalf;
                    float noiseStepY01 = (x0y1z1 - x0y0z1) * -oneHalf;
                    float noiseStepY10 = (x1y1z0 - x1y0z0) * -oneHalf;
                    float noiseStepY11 = (x1y1z1 - x1y0z1) * -oneHalf;

                    //noise values of 4 corners at y=0
                    float noiseStartX0 = x0y0z0;
                    float noiseStartX1 = x0y0z1;
                    float noiseEndX0 = x1y0z0;
                    float noiseEndX1 = x1y0z1;

                    // loop through 2 Block of the Y subchunk
                    for (int suby = 1; suby >= 0; suby--) {
                        int localY = suby + y * 2;
                        float noiseStartZ = noiseStartX0;
                        float noiseEndZ = noiseStartX1;

                        //how much to increment X values, linear interpolation
                        float noiseStepX0 = (noiseEndX0 - noiseStartX0) * oneQuarter;
                        float noiseStepX1 = (noiseEndX1 - noiseStartX1) * oneQuarter;

                        // loop through 4 Block of the X subchunk
                        for (int subx = 0; subx < 4; subx++) {
                            int localX = subx + x * 4;
                            int realX = localX + chunkX * 16;

                            //how much to increment Z values, linear interpolation
                            float noiseStepZ = (noiseEndZ - noiseStartZ) * oneQuarter;

                            //Y and X already interpolated, just need to interpolate final 4 Z block to get final noise value
                            float noiseVal = noiseStartZ;

                            // loop through 4 Block of the Z subchunk
                            for (int subz = 0; subz < 4; subz++) {
                                int localZ = subz + z * 4;
                                int realZ = localZ + chunkZ * 16;
                                currentBiome = null;

                                if (depth == 0) {
                                    //only checks depth once per 4x4 subchunk
                                    if (subx == 0 && subz == 0) {
                                        Block currentBlock = chunkPrimerIn.getBlock(localX, localY, localZ);

                                        currentBiome = worldObj.getBiomeGenForCoords(realX, realX);//world.getBiome(realPos);

                                        //use isDigable to skip leaves/wood getting counted as surface
                                        if (canReplaceBlock(currentBlock, null) || isBiomeBlock(chunkPrimerIn, realX, realZ, currentBlock, currentBiome)) {
                                            depth++;
                                        }
                                    } else {
                                        continue;
                                    }
                                } else if (subx == 0 && subz == 0) {
                                    //already hit surface, simply increment depth counter
                                    depth++;
                                }

                                float adjustedNoiseCutoff = noiseCutoff;// + cutoffAdjuster;
                                if (depth < easeInDepth) {
                                    //higher threshold at surface, normal threshold below easeInDepth
                                    adjustedNoiseCutoff = 1; //(float) clampedLerp(noiseCutoff, surfaceCutoff, (easeInDepth - (float) depth) / easeInDepth);
                                }

                                //increase cutoff as we get closer to the minCaveHeight so it's not all flat floors
                                if (localY < (minCaveHeight + 5)) {
                                    adjustedNoiseCutoff += ((minCaveHeight + 5) - localY) * 0.05;
                                }

                                if (noiseVal > adjustedNoiseCutoff) {
                                    Block aboveBlock = chunkPrimerIn.getBlock(localX, localY + 1, localZ);
                                    if (!isFluidBlock(aboveBlock) || localY <= lavaDepth) {
                                        //if we are in the easeInDepth range or near sea level or subH2O is installed, do some extra checks for water before digging
                                        if ((depth < easeInDepth || localY > (seaLevel - 8)) && localY > lavaDepth) {
                                            if (localX < 15)
                                                if (isFluidBlock(chunkPrimerIn.getBlock(localX + 1, localY, localZ)))
                                                    continue;
                                            if (localX > 0)
                                                if (isFluidBlock(chunkPrimerIn.getBlock(localX - 1, localY, localZ)))
                                                    continue;
                                            if (localZ < 15)
                                                if (isFluidBlock(chunkPrimerIn.getBlock(localX, localY, localZ + 1)))
                                                    continue;
                                            if (localZ > 0)
                                                if (isFluidBlock(chunkPrimerIn.getBlock(localX, localY, localZ - 1)))
                                                    continue;
                                        }
                                        Block currentBlock = chunkPrimerIn.getBlock(localX, localY, localZ);
                                        if (currentBiome == null)
                                            currentBiome = worldObj.getBiomeGenForCoords(realX, realZ);//world.getBiome(realPos);

                                        boolean foundTopBlock = isTopBlock(currentBlock, currentBiome);
                                        digBlock(chunkPrimerIn, localX, localY, localZ, chunkX, chunkZ, foundTopBlock, currentBlock, aboveBlock, currentBiome);
                                    }
                                }

                                noiseVal += noiseStepZ;
                            }

                            noiseStartZ += noiseStepX0;
                            noiseEndZ += noiseStepX1;
                        }

                        noiseStartX0 += noiseStepY00;
                        noiseStartX1 += noiseStepY01;
                        noiseEndX0 += noiseStepY10;
                        noiseEndX1 += noiseStepY11;
                    }
                }
            }
        }
    }

    public float[][][] sampleNoise(int chunkX, int chunkZ, int maxSurfaceHeight) {
        int originalMaxHeight = 128;
        float[][][] noiseSamples = new float[5][130][5];
        float noise;
        for (int x = 0; x < 5; x++) {
            int realX = x * 4 + chunkX * 16;
            for (int z = 0; z < 5; z++) {
                int realZ = z * 4 + chunkZ * 16;
                boolean columnHasCaveFlag = false;

                // loop from top down for y values so we can adjust noise above current y later on
                for (int y = 128; y >= 0; y--) {
                    float realY = y * 2;
                    if (realY > maxSurfaceHeight || realY > maxCaveHeight || realY < minCaveHeight) {
                        // if outside of valid cave range set noise value below normal minimum of -1.0
                        noiseSamples[x][y][z] = -1.1F;
                    } else {
                        // Experiment making the cave system more chaotic the more you descend
                        float dispAmp = (float) (warpAmplifier * ((originalMaxHeight - y) / (originalMaxHeight * 0.85)));

                        float xDisp = 0f;
                        float yDisp = 0f;
                        float zDisp = 0f;

                        xDisp = displacementNoisePerlin.GetNoise(realX, realZ) * dispAmp;
                        yDisp = displacementNoisePerlin.GetNoise(realX, realZ + 67.0f) * dispAmp;
                        zDisp = displacementNoisePerlin.GetNoise(realX, realZ + 149.0f) * dispAmp;

                        // doubling the y frequency to get some more caves
                        noise = worleyF1divF3.SingleCellular3Edge(realX * xzCompression + xDisp, realY * yCompression + yDisp, realZ * xzCompression + zDisp);
                        noiseSamples[x][y][z] = noise;

                        if (noise > noiseCutoff) {
                            columnHasCaveFlag = true;
                            // if noise is below cutoff, adjust values of neighbors helps prevent caves fracturing during interpolation
                            if (x > 0)
                                noiseSamples[x - 1][y][z] = (noise * 0.2f) + (noiseSamples[x - 1][y][z] * 0.8f);
                            if (z > 0)
                                noiseSamples[x][y][z - 1] = (noise * 0.2f) + (noiseSamples[x][y][z - 1] * 0.8f);

                            // more heavily adjust y above 'air block' noise values to give players more head room
                            if (y < 128) {
                                float noiseAbove = noiseSamples[x][y + 1][z];
                                if (noise > noiseAbove)
                                    noiseSamples[x][y + 1][z] = (noise * 0.8F) + (noiseAbove * 0.2F);
                                if (y < 127) {
                                    float noiseTwoAbove = noiseSamples[x][y + 2][z];
                                    if (noise > noiseTwoAbove)
                                        noiseSamples[x][y + 2][z] = (noise * 0.35F) + (noiseTwoAbove * 0.65F);
                                }
                            }

                        }
                    }
                }
                noiseSamples[x][HAS_CAVES_FLAG][z] = columnHasCaveFlag ? 1 : 0;
            }
        }
        return noiseSamples;
    }

    private int recursiveBinarySurfaceSearch(ChunkPrimer chunkPrimer, int localX, int localZ, int searchTop, int searchBottom) {
        int top = searchTop;
        if (searchTop > searchBottom) {
            int searchMid = (searchBottom + searchTop) / 2;
            if (canReplaceBlock(chunkPrimer.getBlock(localX, searchMid, localZ), null)) {
                top = recursiveBinarySurfaceSearch(chunkPrimer, localX, localZ, searchTop, searchMid + 1);
            } else {
                top = recursiveBinarySurfaceSearch(chunkPrimer, localX, localZ, searchMid, searchBottom);
            }
        }
        return top;
    }

    private int getMaxSurfaceHeight(ChunkPrimer primer) {
        int y = 0;
        int[] cords = {2, 6, 3, 11, 7, 2, 9, 13, 12, 4, 13, 9};

        for (int i = 0; i < cords.length; i += 2) {
            int test = recursiveBinarySurfaceSearch(primer, cords[i], cords[i + 1], 255, 0);
            if (test > y) {
                y = test;
                if (y > maxCaveHeight)
                    return y;
            }
        }
        return y;
    }

    private boolean isBiomeBlock(ChunkPrimer primer, int realX, int realZ, Block block, BiomeGenBase biome) {
        return block != null && (block == Block.blocksList[biome.topBlock] || block == Block.blocksList[biome.fillerBlock]);
    }

    private boolean isFluidBlock(Block blocky) {
        return blocky instanceof BlockFluid;
    }

    private boolean isTopBlock(Block block, BiomeGenBase biome) {
        return block == Block.blocksList[biome.topBlock];
    }

    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, Block block, Block up, BiomeGenBase biome) {
        Block top = Block.blocksList[biome.topBlock];
        Block filler = Block.blocksList[biome.fillerBlock];
        if (!(up instanceof BlockFluid) && (this.canReplaceBlock(block, up) || block == top || block == filler)) {
            if (y <= lavaDepth) {
                data.setBlock(x, y, z, lava);
            } else {
                data.setBlock(x, y, z, null);

                /*for (int k = y + 1; k < 128; k++) {
                    Block b = data.getBlock(x, k, z);
                    if (b == Block.gravel) {
                        data.setBlock(x, k, z, Block.stone);
                        break;
                    } else if (b == Block.sand) {
                        data.setBlock(x, k, z, Block.sandStone);
                        break;
                    }
                    if (b != null)
                        break;
                }*/

                if (foundTop && data.getBlock(x, y - 1, z) == filler) {
                    data.setBlock(x, y - 1, z, top);
                }
            }
        }
    }
}
