package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceDesertOasis extends SurfaceBase {

    private Block cliffBlock1;
    private Block cliffBlock2;
    private byte sandMetadata;
    private int cliffType;

    public SurfaceDesertOasis(Block top, Block filler, Block cliff1, Block cliff2, byte metadata, int cliff) {
        super(top, filler);

        cliffBlock1 = cliff1;
        cliffBlock2 = cliff2;
        sandMetadata = metadata;
        cliffType = cliff;
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float c = CliffCalculator.calc(x, y, noise);
        boolean cliff = c > 1.3f ? true : false;
        boolean dirt = false;

        for (int k = 255; k > -1; k--) {
            Block b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (cliff) {
                    if (cliffType == 1) {
                        if (depth < 6) {
                            blocks[(y * 16 + x) * 256 + k] = cliffBlock1;
                            metadata[(y * 16 + x) * 256 + k] = 14;
                        }
                    } else {
                        if (depth > -1 && depth < 2) {
                            blocks[(y * 16 + x) * 256 + k] = rand.nextInt(3) == 0 ? cliffBlock2 : cliffBlock1;
                        } else if (depth < 10) {
                            blocks[(y * 16 + x) * 256 + k] = cliffBlock1;
                        }
                    }
                } else if (depth < 6) {
                    if (depth == 0 && k > 61) {
                        if (perlin.noise2(i / 12f, j / 12f) > -0.3f + ((k - 61f) / 15f)) {
                            dirt = true;
                            blocks[(y * 16 + x) * 256 + k] = topBlock;
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = Block.sand;
                            metadata[(y * 16 + x) * 256 + k] = sandMetadata;
                        }
                    } else if (depth < 4) {
                        if (dirt) {
                            blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = Block.sand;
                            metadata[(y * 16 + x) * 256 + k] = sandMetadata;
                        }
                    } else if (!dirt) {
                        blocks[(y * 16 + x) * 256 + k] = Block.sandStone;
                    }
                }
            }
        }
    }
}
