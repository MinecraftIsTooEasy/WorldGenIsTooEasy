package rwg.map;

import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;
import rwg.util.TerrainMath;

import java.util.Random;

public class MapVolcano {

    public static void build(Block[] blocks, World world, Random mapRand, int baseX, int baseY,
                             int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell, float[] noise) {
        int i, j;
        float distance, height, obsidian;
        Block b;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                i = (chunkX * 16) + x;
                j = (chunkY * 16) + z;

                distance = (float) TerrainMath.dis2(i, j, baseX * 16, baseY * 16);
                obsidian = 140f + distance + perlin.noise2(i / 16f, j / 16f) * 15f;

                if (distance < 10 + perlin.noise2(i / 3f, j / 3f) * 1.5f) {
                    height = perlin.noise2(i / 5f, j / 5f) * 2f;
                    for (int y = 255; y > -1; y--) {
                        if (y > 165) {
                            if (blocks[cta(x, y, z)] != null) {
                                blocks[cta(x, y, z)] = null;
                            }
                        } else if (y > obsidian && y < 156 + height) {
                            blocks[cta(x, y, z)] = Block.obsidian;
                        } else if (y < 166) {
                            blocks[cta(x, y, z)] = Block.lavaMoving;
                        } else if (y < obsidian + 1) {
                            if (blocks[cta(x, y, z)] == null) {
                                blocks[cta(x, y, z)] = Block.stone;
                            } else {
                                break;
                            }
                        }
                    }
                } else {
                    height = 190f - (distance + perlin.noise2(i / 12f, j / 12f) * 5f) * 1.7f;
                    if (height > noise[x * 16 + z]) {
                        noise[x * 16 + z] = height;
                    }

                    for (int y = 255; y > -1; y--) {
                        if (y <= height) {
                            b = blocks[cta(x, y, z)];
                            if (b == null) {
                                if (y > obsidian) {
                                    b = Block.obsidian;
                                } else {
                                    b = Block.stone;
                                }
                            } else {
                                break;
                            }
                            blocks[cta(x, y, z)] = b;
                        }
                    }
                }
            }
        }
    }

    public static int cta(int x, int y, int z) {
        return (x * 16 + z) * 256 + y;
    }
}
