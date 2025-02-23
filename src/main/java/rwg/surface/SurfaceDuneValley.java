package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.biomes.RWGBaseBiomes;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceDuneValley extends SurfaceBase {

    private float valley;
    private boolean dirt;
    private boolean mix;

    public SurfaceDuneValley(Block top, Block fill, float valleySize, boolean d, boolean m) {
        super(top, fill);

        valley = valleySize;
        dirt = d;
        mix = m;
    }

    @Override
    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float h = (perlin.noise2(i / valley, j / valley) + 0.25f) * 65f;
        h = h < 1f ? 1f : h;
        float m = perlin.noise2(i / 12f, j / 12f);
        boolean sand = false;

        Block b;
        for (int k = 255; k > -1; k--) {
            b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (depth == 0) {
                    if (k > 90f + perlin.noise2(i / 24f, j / 24f) * 10f - h || (m < -0.28f && mix)) {
                        blocks[(y * 16 + x) * 256 + k] = Block.sand;
                        base[x * 16 + y] = RWGBaseBiomes.baseHotDesert;
                        sand = true;
                    } else if (dirt && m < 0.22f || k < 62) {
                        blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                        metadata[(y * 16 + x) * 256 + k] = 1;
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = topBlock;
                    }
                } else if (depth < 6) {
                    if (sand) {
                        if (depth < 4) {
                            blocks[(y * 16 + x) * 256 + k] = Block.sand;
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = Block.sandStone;
                        }
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = fillerBlock;
                    }
                }
            }
        }
    }
}
