package rwg.biomes.ocean;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class RealisticBiomeOceanTest extends RealisticBiomeBase {

    public RealisticBiomeOceanTest() {
        super(0, RWGBaseBiomes.baseColdPlains);
    }

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
    }

    @Override
    public float rNoise(NoiseGenerator perlin, CellNoise cell, int x, int y, float ocean, float border, float river) {
        return 45f;
    }

    @Override
    public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                         Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        Block b;
        for (int k = 255; k > -1; k--) {
            b = blocks[(y * 16 + x) * 256 + k];
            if (b == null) {
                depth = -1;
            } else if (b == Block.stone) {
                depth++;

                if (depth == 0) {
                    if (k < 62) {
                        blocks[(y * 16 + x) * 256 + k] = Block.sand;
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = Block.sand;
                    }
                } else if (depth < 6) {
                    blocks[(y * 16 + x) * 256 + k] = Block.sand;
                }
            }
        }
    }
}
