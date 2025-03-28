package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;
import rwg.util.NoiseSelector;

import java.util.Random;

public class SurfaceCanyon extends SurfaceBase {

    private int[] claycolor = new int[100];
    private byte blockByte;
    private int grassRaise;

    public SurfaceCanyon(Block top, Block fill, byte b, int grassHeight) {
        super(top, fill);
        blockByte = b;
        grassRaise = grassHeight;

        int[] c = new int[]{1, 8, 0};
        NoiseGenerator perlin = NoiseSelector.createNoiseGenerator(2L);

        float n;
        for (int i = 0; i < 100; i++) {
            n = perlin.noise1(i / 3f) * 3f + perlin.noise1(i / 1f) * 0.3f + 1.5f;
            n = n >= 3f ? 2.9f : n < 0f ? 0f : n;
            claycolor[i] = c[(int) n];
        }
    }

    public byte getClayColorForHeight(int k) {
        k -= 60;
        k = k < 0 ? 0 : k > 99 ? 99 : k;
        return (byte) claycolor[k];
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float c = CliffCalculator.calc(x, y, noise);
        boolean cliff = c > 1.3f ? true : false;

        for (int k = 255; k > -1; k--) {
            Block b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (depth > -1 && depth < 12) {
                    if (cliff) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stainedClay;
                        metadata[(y * 16 + x) * 256 + k] = getClayColorForHeight(k);
                    } else {
                        if (depth > 4) {
                            blocks[(y * 16 + x) * 256 + k] = Block.stainedClay;
                            metadata[(y * 16 + x) * 256 + k] = getClayColorForHeight(k);
                        } else if (k > 74 + grassRaise) {
                            if (rand.nextInt(5) == 0) {
                                blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                            } else {
                                if (depth == 0) {
                                    blocks[(y * 16 + x) * 256 + k] = topBlock;
                                    metadata[(y * 16 + x) * 256 + k] = blockByte;
                                } else {
                                    blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                                    metadata[(y * 16 + x) * 256 + k] = blockByte;
                                }
                            }
                        } else if (k < 62) {
                            blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                        } else if (k < 62 + grassRaise) {
                            if (depth == 0) {
                                blocks[(y * 16 + x) * 256 + k] = Block.grass;
                            } else {
                                blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                            }
                        } else if (k < 75 + grassRaise) {
                            if (depth == 0) {
                                int r = (int) ((k - (62 + grassRaise)) / 2f);
                                if (rand.nextInt(r + 1) == 0) {
                                    blocks[(y * 16 + x) * 256 + k] = Block.grass;
                                } else if (rand.nextInt((int) (r / 2f) + 1) == 0) {
                                    blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                                } else {
                                    blocks[(y * 16 + x) * 256 + k] = topBlock;
                                    metadata[(y * 16 + x) * 256 + k] = blockByte;
                                }
                            } else {
                                blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                                metadata[(y * 16 + x) * 256 + k] = blockByte;
                            }
                        } else {
                            if (depth == 0) {
                                blocks[(y * 16 + x) * 256 + k] = topBlock;
                                metadata[(y * 16 + x) * 256 + k] = blockByte;
                            } else {
                                blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                                metadata[(y * 16 + x) * 256 + k] = blockByte;
                            }
                        }
                    }
                } else if (k > 63) {
                    blocks[(y * 16 + x) * 256 + k] = Block.stainedClay;
                    metadata[(y * 16 + x) * 256 + k] = getClayColorForHeight(k);
                }
            }
        }
    }
}
