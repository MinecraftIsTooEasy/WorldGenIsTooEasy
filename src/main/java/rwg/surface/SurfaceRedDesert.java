package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceRedDesert extends SurfaceBase {

    private Block cliffBlock1;
    private Block cliffBlock2;
    private Block bottomBlock;

    public SurfaceRedDesert() {
        super(Block.gravel, Block.gravel);

        bottomBlock = Block.sandStone;
        cliffBlock1 = Block.stainedClay;
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
                    if (depth < 6) {
                        blocks[(y * 16 + x) * 256 + k] = cliffBlock1;
                        metadata[(y * 16 + x) * 256 + k] = (byte) 14;
                    }
                } else if (depth < 6) {
                    if (depth == 0 && k > 61) {
                        blocks[(y * 16 + x) * 256 + k] = topBlock;
                        metadata[(y * 16 + x) * 256 + k] = (byte) 2;
                    } else if (depth < 4) {
                        blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                        metadata[(y * 16 + x) * 256 + k] = (byte) 2;
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = bottomBlock;
                    }
                }
            }
        }
    }
}
