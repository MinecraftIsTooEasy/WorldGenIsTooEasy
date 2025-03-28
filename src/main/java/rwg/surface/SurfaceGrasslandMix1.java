package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceGrasslandMix1 extends SurfaceBase {

    private Block mixBlock;
    private Block cliffBlock1;
    private Block cliffBlock2;
    private float width;
    private float height;

    public SurfaceGrasslandMix1(Block top, Block filler, Block mix, Block cliff1, Block cliff2, float mixWidth,
                                float mixHeight) {
        super(top, filler);

        mixBlock = mix;
        cliffBlock1 = cliff1;
        cliffBlock2 = cliff2;

        width = mixWidth;
        height = mixHeight;
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float c = CliffCalculator.calc(x, y, noise);
        boolean cliff = c > 1.4f ? true : false;

        for (int k = 255; k > -1; k--) {
            Block b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (cliff) {
                    if (depth > -1 && depth < 2) {
                        blocks[(y * 16 + x) * 256 + k] = rand.nextInt(3) == 0 ? cliffBlock2 : cliffBlock1;
                    } else if (depth < 10) {
                        blocks[(y * 16 + x) * 256 + k] = cliffBlock1;
                    }
                } else {
                    if (depth == 0 && k > 61) {
                        if (perlin.noise2(i / width, j / width) > height) // > 0.27f, i / 12f
                        {
                            blocks[(y * 16 + x) * 256 + k] = mixBlock;
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = topBlock;
                        }
                    } else if (depth < 4) {
                        blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                    }
                }
            }
        }
    }
}
