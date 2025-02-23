package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceTundra extends SurfaceBase {

    public SurfaceTundra(Block top, Block fill) {
        super(top, fill);
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float p = perlin.noise2(i / 8f, j / 8f) * 0.5f;
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
                        gravel = true;
                    }

                    if (c > 0.45f && c > 1.5f - ((k - 60f) / 65f) + p) {
                        cliff = 1;
                    }
                    if (c > 1.5f) {
                        cliff = 2;
                    }
                    if (k > 110 + (p * 4) && c < 0.3f + ((k - 100f) / 50f) + p) {
                        cliff = 3;
                    }

                    if (cliff == 1) {
                        blocks[(y * 16 + x) * 256 + k] = rand.nextInt(3) == 0 ? Block.cobblestone : Block.stone;
                    } else if (cliff == 2) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stainedClay;
                        metadata[(y * 16 + x) * 256 + k] = 9;
                    } else if (cliff == 3) {
                        blocks[(y * 16 + x) * 256 + k] = Block.blockSnow;
                    } else if (perlin.noise2(i / 50f, j / 50f) + p * 0.6f > 0.24f) {
                        blocks[(y * 16 + x) * 256 + k] = Block.grass;
                        //blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                        //metadata[(y * 16 + x) * 256 + k] = 2;
                    } else if (k < 63) {
                        blocks[(y * 16 + x) * 256 + k] = Block.gravel;
                        gravel = true;
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = Block.grass;
                    }
                } else if (depth < 6) {
                    if (cliff == 1) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stone;
                    } else if (cliff == 2) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stainedClay;
                        metadata[(y * 16 + x) * 256 + k] = 9;
                    } else if (cliff == 3) {
                        blocks[(y * 16 + x) * 256 + k] = Block.blockSnow;
                    } else if (gravel) {
                        blocks[(y * 16 + x) * 256 + k] = Block.gravel;
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                    }
                }
            }
        }
    }
}
