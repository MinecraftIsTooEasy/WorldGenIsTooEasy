package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceGrasslandMixBig extends SurfaceBase {

    private Block mixBlockTop;
    private Block mixBlockFill;
    private Block cliffBlock1;
    private Block cliffBlock2;
    private float width;
    private float height;
    private float smallW;
    private float smallS;

    public SurfaceGrasslandMixBig(Block top, Block filler, Block mixTop, Block mixFill, Block cliff1, Block cliff2,
                                  float mixWidth, float mixHeight, float smallWidth, float smallStrength) {
        super(top, filler);

        mixBlockTop = mixTop;
        mixBlockFill = mixFill;
        cliffBlock1 = cliff1;
        cliffBlock2 = cliff2;

        width = mixWidth;
        height = mixHeight;
        smallW = smallWidth;
        smallS = smallStrength;
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float c = CliffCalculator.calc(x, y, noise);
        boolean cliff = c > 1.4f ? true : false;
        boolean mix = false;

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
                        if (perlin.noise2(i / width, j / width) + perlin.noise2(i / smallW, j / smallW) * smallS
                                > height) {
                            blocks[(y * 16 + x) * 256 + k] = mixBlockTop;
                            mix = true;
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = topBlock;
                        }
                    } else if (depth < 4) {
                        if (mix) {
                            blocks[(y * 16 + x) * 256 + k] = mixBlockFill;
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                        }
                    }
                }
            }
        }
    }
}
