package rwg.biomes.coast;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.deco.DecoWaterGrass;
import rwg.util.CellNoise;
import rwg.util.CliffCalculator;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class RealisticBiomeCoastDunes extends RealisticBiomeBase {

    public RealisticBiomeCoastDunes() {
        super(0, RWGBaseBiomes.baseOceanTemperate);
    }

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
        for (int l14 = 0; l14 < 11f * strength; l14++) {
            int l19 = chunkX + rand.nextInt(16) + 8;
            int k22 = 64 + rand.nextInt(64);
            int j24 = chunkY + rand.nextInt(16) + 8;

            if (rand.nextInt(5) == 0) {
                (new DecoWaterGrass(Block.plantRed, 2, 69)).generate(world, rand, l19, k22, j24);
            }
            if (rand.nextInt(3) == 0) {
                (new DecoWaterGrass(Block.leaves, 4, 67)).generate(world, rand, l19, k22, j24);
            } else {
                (new DecoWaterGrass(Block.tallGrass, 1, 65)).generate(world, rand, l19, k22, j24);
            }
        }
    }

    @Override
    public float rNoise(NoiseGenerator perlin, CellNoise cell, int x, int y, float ocean, float border, float river) {
        river = river > 0.5f ? 1f : river * 2f;

        float h = ocean < 0.5f ? ocean * 18f : 9f;

        if (ocean < 1.9f) {
            float st = (1.9f - ocean) * 20f;
            st = st > 1f ? 1f : st;

            h += perlin.noise2(x / 12f, y / 12f) * 1f;
            h += perlin.noise2(x / 23f, y / 23f) * 2f;
        }

        if (ocean > 1f) {
            h += perlin.noise2(x / 15f, y / 15f) * (ocean - 1f) * 8f;
            h += perlin.noise2(x / 25f, y / 25f) * (ocean - 1f) * 13f;
            h += (ocean - 1f) * 9f;
        }

        return 55f + h;
    }

    @Override
    public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                         Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        float cliff = CliffCalculator.calc(x, y, noise);
        boolean sand = false;

        for (int k = 255; k > -1; k--) {
            Block b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (cliff > 1.3f) {
                    if (depth > -1 && depth < 2) {
                        blocks[(y * 16 + x) * 256 + k] = rand.nextInt(3) == 0 ? Block.cobblestone : Block.stone;
                    } else if (depth < 10) {
                        blocks[(y * 16 + x) * 256 + k] = Block.stone;
                    }
                } else {
                    if (depth == 0) {
                        if (k > 68) {
                            blocks[(y * 16 + x) * 256 + k] = Block.grass;
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = Block.sand;
                            sand = true;
                        }
                    } else if (depth < 5) {
                        if (sand) {
                            if (depth < 4) {
                                blocks[(y * 16 + x) * 256 + k] = Block.sand;
                            } else {
                                blocks[(y * 16 + x) * 256 + k] = Block.sandStone;
                            }
                        } else {
                            blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                        }
                    }
                }
            }
        }
    }
}
