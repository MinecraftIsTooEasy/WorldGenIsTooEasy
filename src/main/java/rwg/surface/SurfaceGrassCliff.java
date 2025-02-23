package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceGrassCliff extends SurfaceBase {
    public SurfaceGrassCliff(Block top, Block fill) {
        super(top, fill);
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float c = CliffCalculator.calc(x, y, noise);
        boolean cliff = c > 1.3f;

        for (int k = 255; k > -1; k--) {
            Block b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (depth > -1 && depth < 12) {
                    if (cliff) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stone;
                    } else {
                        if (depth > 4) {
                            blocks[(y * 16 + x) * 256 + k] = Block.stone;
                        } else {
                            if (depth == 0) {
                                blocks[(y * 16 + x) * 256 + k] = topBlock;
                            } else {
                                blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                            }
                        }
                    }
                } else if (k > 63) {
                    blocks[(y * 16 + x) * 256 + k] = Block.stone;
                }
            }
        }
    }
}
