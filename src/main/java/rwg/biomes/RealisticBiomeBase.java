package rwg.biomes;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;
import rwg.world.ChunkManagerRealistic;

import java.util.Random;

public class RealisticBiomeBase {

    private static final RealisticBiomeBase[] biomeList = new RealisticBiomeBase[256];
    private static int nextBiomeID = 0;
    public final int biomeID;
    public final int subID;
    public final BiomeGenBase baseBiome;
    public final RealisticBiomeBase beachBiome;
    public final BiomeGenBase riverBiome;

    public RealisticBiomeBase(int sub, BiomeGenBase biome) {
        this(sub, biome, RWGBiomes.coastIce, RWGBaseBiomes.baseRiverTemperate);
    }

    public RealisticBiomeBase(int sub, BiomeGenBase biome, RealisticBiomeBase coast, BiomeGenBase river) {
        biomeID = nextBiomeID;
        biomeList[biomeID] = this;
        nextBiomeID++;

        subID = sub;
        baseBiome = biome;
        beachBiome = coast;
        riverBiome = river;
    }

    public static RealisticBiomeBase getBiome(int id) {
        return biomeList[id];
    }

    // ======================================================================================================================================

    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
    }

    public void generateMapGen(Block[] blocks, byte[] metadata, Long seed, World world, ChunkManagerRealistic cmr,
                               Random mapRand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell, float noise[]) {
        int k = 5;
        mapRand.setSeed(seed);
        long l = (mapRand.nextLong() / 2L) * 2L + 1L;
        long l1 = (mapRand.nextLong() / 2L) * 2L + 1L;
        for (int baseX = chunkX - k; baseX <= chunkX + k; baseX++) {
            for (int baseY = chunkY - k; baseY <= chunkY + k; baseY++) {
                mapRand.setSeed((long) baseX * l + (long) baseY * l1 ^ seed);
                rMapGen(blocks, world, cmr, mapRand, baseX, baseY, chunkX, chunkY, perlin, cell, noise);
            }
        }
    }

    public void rMapGen(Block[] blocks, World world, ChunkManagerRealistic cmr, Random mapRand,
                        int chunkX, int chunkY, int baseX, int baseY, NoiseGenerator perlin, CellNoise cell, float[] noise) {
    }

    public float rNoise(NoiseGenerator perlin, CellNoise cell, int x, int y, float ocean, float border, float river) {
        return 63f;
    }

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
                        blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                    } else {
                        blocks[(y * 16 + x) * 256 + k] = Block.grass;
                    }
                } else if (depth < 6) {
                    blocks[(y * 16 + x) * 256 + k] = Block.dirt;
                }
            }
        }
    }

    public float r3Dnoise(float z) {
        return 0f;
    }
}
