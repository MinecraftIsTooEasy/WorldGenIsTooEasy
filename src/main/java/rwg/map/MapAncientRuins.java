package rwg.map;

import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class MapAncientRuins {

    public static void build(Block[] blocks, byte[] metadata, World world, Random mapRand, int chunkX, int chunkY,
                             int baseX, int baseY, NoiseGenerator perlin, CellNoise cell, int dis) {
        int y = 120;
        for (; y > 49; y--) {
            if (blocks[coordstoArray(8, y - 1, 8)] != null) {
                if (y > 118) {
                    return;
                }
                break;
            }

            if (y < 51) {
                return;
            }
        }

        int sizeX = mapRand.nextInt(5) + 10 - (dis * 2);
        int sizeZ = mapRand.nextInt(5) + 10 - (dis * 2);
        int startX = mapRand.nextInt(2) + dis;
        int startZ = mapRand.nextInt(2) + dis;
        int i, j, height;

        for (i = startX; i < startX + sizeX; i++) {
            if (blocks[coordstoArray(startX + i, y - 2, startZ)] != null) {
                height = mapRand.nextInt(4);
                for (j = 0; j < height; j++) {
                    blocks[coordstoArray(startX + i, y + j - 1, startZ)] = Block.cobblestoneMossy;
                }
            }

            if (blocks[coordstoArray(startX + i, y - 2, startZ + sizeZ)] != null) {
                height = mapRand.nextInt(4);
                for (j = 0; j < height; j++) {
                    blocks[coordstoArray(startX + i, y + j - 1, startZ + sizeZ)] = Block.cobblestoneMossy;
                }
            }
        }

        for (i = startZ + 1; i < startZ + sizeZ - 1; i++) {
            if (blocks[coordstoArray(startX, y - 2, startZ + i)] != null) {
                height = mapRand.nextInt(4);
                for (j = 0; j < height; j++) {
                    blocks[coordstoArray(startX, y + j - 1, startZ + i)] = Block.cobblestoneMossy;
                }
            }

            if (blocks[coordstoArray(startX + sizeX, y - 2, startZ + i)] != null) {
                height = mapRand.nextInt(4);
                for (j = 0; j < height; j++) {
                    blocks[coordstoArray(startX + sizeX, y + j - 1, startZ + i)] = Block.cobblestoneMossy;
                }
            }
        }
    }

    public static int coordstoArray(int x, int y, int z) {
        return (z * 16 + x) * 256 + y;
    }
}
