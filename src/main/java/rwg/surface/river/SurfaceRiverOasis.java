package rwg.surface.river;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.surface.SurfaceBase;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceRiverOasis extends SurfaceBase {

    public SurfaceRiverOasis() {
        super(Block.grass, Block.dirt);
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        if (river > 0.05f && river + (perlin.noise2(i / 10f, j / 10f) * 0.15f) > 0.8f) {
            Block b;
            for (int k = 255; k > -1; k--) {
                b = blocks[(y * 16 + x) * 256 + k];
                if (b == null) {
                    depth = -1;
                } else if (b != Block.waterStill) {
                    depth++;

                    if (depth == 0 && k > 61) {
                        blocks[(y * 16 + x) * 256 + k] = Block.grass;
                    } else if (depth < 4) {
                        blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                    } else if (depth > 4) {
                        return;
                    }
                }
            }
        }
    }
}
