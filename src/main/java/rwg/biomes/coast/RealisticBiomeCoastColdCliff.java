package rwg.biomes.coast;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class RealisticBiomeCoastColdCliff extends RealisticBiomeBase {

    public RealisticBiomeCoastColdCliff() {
        super(0, RWGBaseBiomes.baseOceanTemperate);
    }

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
    }

    @Override
    public float rNoise(NoiseGenerator perlin, CellNoise cell, int x, int y, float ocean, float border, float river) {
        river = river > 0.5f ? 1f : river * 2f;

        float h = ocean < 0.5f ? ocean * 14f : 7f;

        if (ocean < 1.9f) {
            float st = (1.9f - ocean) * 20f;
            st = st > 1f ? 1f : st;

            h += perlin.noise2(x / 12f, y / 12f) * 1.5f;
            h += perlin.noise2(x / 20f, y / 20f) * 3f;
        }

        return 55f + h;
    }

    @Override
    public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                         Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
    }
}
