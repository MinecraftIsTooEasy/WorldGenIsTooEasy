package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;
import rwg.util.SnowheightCalculator;

import java.util.Random;

public class SurfacePolar extends SurfaceBase {

    public SurfacePolar(Block top, Block fill) {
        super(top, fill);
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        boolean water = false;
        boolean riverPaint = false;
        boolean grass = false;

        if (river > 0.05f && river + (perlin.noise2(i / 10f, j / 10f) * 0.1f) > 0.86f) {
            riverPaint = true;

            if (perlin.noise2(i / 12f, j / 12f) > 0.25f) {
                grass = true;
            }
        }

        Block b;
        for (int k = 255; k > -1; k--) {
            b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (riverPaint) {
                    if (grass && depth < 4) {
                        blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                    } else if (depth == 0) {
                        blocks[(y * 16 + x) * 256 + k] = rand.nextInt(2) == 0 ? Block.stone : Block.cobblestone;
                    }
                } else if (depth > -1 && depth < 9) {
                    blocks[(y * 16 + x) * 256 + k] = Block.blockSnow;
                    if (depth == 0 && k > 61 && k < 254) {
                        SnowheightCalculator.calc(x, y, k, blocks, metadata, noise);
                    }
                }
            } else if (!water && b == Block.waterStill) {
                blocks[(y * 16 + x) * 256 + k] = Block.ice;
                water = true;
            }
        }
    }
}
