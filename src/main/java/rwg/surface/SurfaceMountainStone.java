package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceMountainStone extends SurfaceBase {

    public byte topByte = 0;
    private boolean beach;
    private Block beachBlock;
    private float min;
    private float sCliff = 1.5f;
    private float sHeight = 60f;
    private float sStrength = 65f;
    private float cCliff = 1.5f;

    public SurfaceMountainStone(Block top, Block fill, boolean genBeach, Block genBeachBlock, float minCliff) {
        super(top, fill);
        beach = genBeach;
        beachBlock = genBeachBlock;
        min = minCliff;
    }

    public SurfaceMountainStone(Block top, Block fill, boolean genBeach, Block genBeachBlock, float minCliff,
                                float stoneCliff, float stoneHeight, float stoneStrength, float clayCliff) {
        this(top, fill, genBeach, genBeachBlock, minCliff);

        sCliff = stoneCliff;
        sHeight = stoneHeight;
        sStrength = stoneStrength;
        cCliff = clayCliff;
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float c = CliffCalculator.calc(x, y, noise);
        int cliff = 0;
        boolean gravel = false;

        Block b;
        for (int k = 255; k > -1; k--) {
            b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (depth == 0) {
                    if (k < 63) {
                        if (beach) {
                            gravel = true;
                        }
                    }

                    float p = perlin.noise3(i / 8f, j / 8f, k / 8f) * 0.5f;
                    if (c > min && c > sCliff - ((k - sHeight) / sStrength) + p) {
                        cliff = 1;
                    }
                    if (c > cCliff) {
                        cliff = 2;
                    }

                    if (cliff == 1) {
                        blocks[(y * 16 + x) * 256 + k] = rand.nextInt(3) == 0 ? Block.cobblestone : Block.stone;
                    } else if (cliff == 2) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stainedClay;
                        metadata[(y * 16 + x) * 256 + k] = 9;
                    } else if (k < 63) {
                        if (beach) {
                            blocks[(y * 16 + x) * 256 + k] = beachBlock;
                            gravel = true;
                        } else if (k < 62) {
                            blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = topBlock;
                            metadata[(y * 16 + x) * 256 + k] = topByte;
                        }
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = topBlock;
                        metadata[(y * 16 + x) * 256 + k] = topByte;
                    }
                } else if (depth < 6) {
                    if (cliff == 1) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stone;
                    } else if (cliff == 2) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stainedClay;
                        metadata[(y * 16 + x) * 256 + k] = 9;
                    } else if (gravel) {
                        blocks[(y * 16 + x) * 256 + k] = beachBlock;
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                    }
                }
            }
        }
    }
}
