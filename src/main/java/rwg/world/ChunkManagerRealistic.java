package rwg.world;

import net.minecraft.*;
import rwg.biomes.RWGBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;
import rwg.util.NoiseSelector;
import rwg.util.WeightedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ChunkManagerRealistic extends WorldChunkManager {

    private BiomeCache biomeCache;
    private List biomesToSpawnIn;

    private NoiseGenerator perlin;
    private CellNoise cell;

    private CellNoise biomecell;
    private WeightedList<WeightedList<RealisticBiomeBase>> biomes = new WeightedList<>();
    private WeightedList<RealisticBiomeBase> biomes_small = new WeightedList<>();
    private boolean smallEnabled;

    private float[] borderNoise;
    private HashMap<ChunkCoordIntPair, RealisticBiomeBase> biomeDataMap = new HashMap<>();

    protected ChunkManagerRealistic() {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = new ArrayList();
        borderNoise = new float[256];
    }

    public ChunkManagerRealistic(World par1World) {
        this();
        long seed = par1World.getSeed();

        perlin = NoiseSelector.createNoiseGenerator(seed);
        cell = new CellNoise(seed, (short) 0);
        cell.setUseDistance(true);
        biomecell = new CellNoise(seed, (short) 0);


        WeightedList<RealisticBiomeBase> biomes_snow = new WeightedList<>();
        biomes_snow.add(RWGBiomes.polar);
        biomes_snow.add(RWGBiomes.snowHills);
        biomes_snow.add(RWGBiomes.snowRivers);
        biomes_snow.add(RWGBiomes.snowLakes);
        biomes_snow.add(RWGBiomes.redwoodSnow);

        WeightedList<RealisticBiomeBase> biomes_cold = new WeightedList<>();
        biomes_cold.add(RWGBiomes.tundraHills);
        biomes_cold.add(RWGBiomes.tundraPlains);
        biomes_cold.add(RWGBiomes.taigaHills);
        biomes_cold.add(RWGBiomes.taigaPlains);
        biomes_cold.add(RWGBiomes.redwood);
        biomes_cold.add(RWGBiomes.darkRedwood);
        biomes_cold.add(RWGBiomes.darkRedwoodPlains);


        WeightedList<RealisticBiomeBase> biomes_hot = new WeightedList<>();
        biomes_hot.add(RWGBiomes.redwoodJungle);
        biomes_hot.add(RWGBiomes.jungleHills);
        biomes_hot.add(RWGBiomes.jungleCliff);

        WeightedList<RealisticBiomeBase> biomes_dry = new WeightedList<>();
        biomes_dry.add(RWGBiomes.duneValleyForest);
        biomes_dry.add(RWGBiomes.savanna);
        biomes_dry.add(RWGBiomes.savannaForest);
        biomes_dry.add(RWGBiomes.savannaDunes);
        biomes_dry.add(RWGBiomes.stoneMountains);
        biomes_dry.add(RWGBiomes.stoneMountainsCactus);
        biomes_dry.add(RWGBiomes.hotForest);
        biomes_dry.add(RWGBiomes.hotRedwood);
        biomes_dry.add(RWGBiomes.canyonForest);
        biomes_dry.add(RWGBiomes.mesaPlains);
        biomes_dry.add(RWGBiomes.desert);
        biomes_dry.add(RWGBiomes.desertMountains);
        biomes_dry.add(RWGBiomes.duneValley);
        biomes_dry.add(RWGBiomes.oasis);
        biomes_dry.add(RWGBiomes.redDesertMountains);
        biomes_dry.add(RWGBiomes.redDesertOasis);
        biomes_dry.add(RWGBiomes.canyon);
        biomes_dry.add(RWGBiomes.mesa);

        WeightedList<RealisticBiomeBase> biomes_wet = new WeightedList<>();
        biomes_wet.add(RWGBiomes.woodhills);
        biomes_wet.add(RWGBiomes.woodmountains);

        biomes.add(biomes_snow, 5);
        biomes.add(biomes_cold, 7);
        biomes.add(biomes_wet, 10);
        biomes.add(biomes_hot, 4);
        biomes.add(biomes_dry, 8);

        smallEnabled = biomes_small.size() > 1;
    }

    public int[] getBiomesGens(int par1, int par2, int par3, int par4) {
        int[] d = new int[par3 * par4];

        for (int i = 0; i < par3; i++) {
            for (int j = 0; j < par4; j++) {
                d[j * par3 + i] = getBiomeGenAt(par1 + i, par2 + j).biomeID;
            }
        }
        return d;
    }

    public RealisticBiomeBase[] getBiomesGensData(int par1, int par2, int par3, int par4) {
        RealisticBiomeBase[] data = new RealisticBiomeBase[par3 * par4];

        for (int i = 0; i < par3; i++) {
            for (int j = 0; j < par4; j++) {
                data[j * par3 + i] = getBiomeDataAt(par1 + i, par2 + j);
            }
        }
        return data;
    }

    public float getOceanValue(int x, int y) {
        float base = 0f;
        float sample1 = perlin.noise2(x / 1200f, y / 1200f) + base;
        float sample2 = 0f, sa = 0f, highest = 0f;

        if (sample1 == 0f) {
            highest = 1f;
        }

        if (diff(sample1, sample2 = perlin.noise2((x - 100f) / 1200f, y / 1200f) + base, base)) {
            sa = sample1 * (1 / Math.abs(sample1 - sample2));
            highest = Math.max(1f - Math.abs(sa), highest);
        } else if (diff(sample1, sample2 = perlin.noise2((x + 100f) / 1200f, y / 1200f) + base, base)) {
            sa = sample1 * (1 / Math.abs(sample1 - sample2));
            highest = Math.max(1f - Math.abs(sa), highest);
        }

        if (diff(sample1, sample2 = perlin.noise2(x / 1200f, (y + 100f) / 1200f) + base, base)) {
            sa = sample1 * (1 / Math.abs(sample1 - sample2));
            highest = Math.max(1f - Math.abs(sa), highest);
        } else if (diff(sample1, sample2 = perlin.noise2(x / 1200f, (y - 100f) / 1200f) + base, base)) {
            sa = sample1 * (1 / Math.abs(sample1 - sample2));
            highest = Math.max(1f - Math.abs(sa), highest);
        }

        if (sample1 > 0f) {
            highest = 2f - highest;
        }

        return highest;
    }

    public boolean diff(float sample1, float sample2, float base) {
        return (sample1 < base && sample2 > base) || (sample1 > base && sample2 < base);
    }

    public BiomeGenBase getBiomeGenAt(int par1, int par2) {
        return getBiomeDataAt(par1, par2, getOceanValue(par1, par2)).baseBiome;
    }

    public RealisticBiomeBase getBiomeDataAt(int par1, int par2) {
        return getBiomeDataAt(par1, par2, getOceanValue(par1, par2));
    }

    public RealisticBiomeBase getBiomeDataAt(int par1, int par2, float ocean) {
        ChunkCoordIntPair coords = new ChunkCoordIntPair(par1, par2);

        if (biomeDataMap.containsKey(coords)) {
            return biomeDataMap.get(coords);
        }

        RealisticBiomeBase output;

        float s = smallEnabled ? (biomecell.noise(par1 / 140D, par2 / 140D, 1D) * 0.5f) + 0.5f : 0f;
        if (smallEnabled && s > 0.975f) {
            float h = (s - 0.975f) * 40f;
            h = MathHelper.clamp_float(h, 0, 0.9999999f);
            output = biomes_small.get(h);
        } else {
            float b = biomecell.noise((par1 + 4000f) / 1200D, par2 / 1200D, 1D) * 0.5f + 0.5f;
            b = MathHelper.clamp_float(b, 0, 0.9999999f);

            output = biomes.get(b).get(MathHelper.clamp_float(biomecell.noise(par1 / 450D, par2 / 450D, 1D) * 0.5f + 0.5f, 0, 0.9999999f));
        }

        if (biomeDataMap.size() > 4096) {
            biomeDataMap.clear();
        }

        biomeDataMap.put(coords, output);
        return output;
    }

    public float getNoiseAt(int x, int y) {
        float river = getRiverStrength(x, y) + 1f;
        if (river < 0.5f) {
            return 59f;
        }

        float ocean = getOceanValue(x, y);
        return getBiomeDataAt(x, y, ocean).rNoise(perlin, cell, x, y, ocean, 1f, river);
    }

    public float getNoiseWithRiverOceanAt(int x, int y, float river, float ocean) {
        return getBiomeDataAt(x, y, ocean).rNoise(perlin, cell, x, y, ocean, 1f, river);
    }

    public float calculateRiver(int x, int y, float st, float biomeHeight) {

        if (st < 0f && biomeHeight > 59f) {
            float pX = x + (perlin.noise1(y / 240f) * 220f);
            float pY = y + (perlin.noise1(x / 240f) * 220f);
            float r = cell.border(pX / 1250D, pY / 1250D, 50D / 1300D, 1f);
            return (biomeHeight * (r + 1f))
                    + ((59f + perlin.noise2(x / 12f, y / 12f) * 2f + perlin.noise2(x / 8f, y / 8f) * 1.5f) * (-r));
        } else {
            return biomeHeight;
        }
    }

    public float getRiverStrength(int x, int y) {
        return cell.border(
                (x + (perlin.noise1(y / 240f) * 220f)) / 1250D,
                (y + (perlin.noise1(x / 240f) * 220f)) / 1250D,
                50D / 300D,
                1f);
    }

    public boolean isBorderlessAt(int x, int y) {
        int bx, by;

        for (bx = -2; bx <= 2; bx++) {
            for (by = -2; by <= 2; by++) {
                borderNoise[getBiomeDataAt(x + bx * 16, y + by * 16).biomeID] += 0.04f;
            }
        }

        by = 0;
        for (bx = 0; bx < 256; bx++) {
            if (borderNoise[bx] > 0.98f) {
                by = 1;
            }
            borderNoise[bx] = 0;
        }

        return by == 1;
    }

    public List getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5) {
        if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5) {
            par1ArrayOfFloat = new float[par4 * par5];
        }

        int[] var6 = getBiomesGens(par2, par3, par4, par5);

        for (int var7 = 0; var7 < par4 * par5; ++var7) {
            float var8 = (float) BiomeGenBase.biomeList[var6[var7]].getIntRainfall() / 65536.0F;

            if (var8 > 1.0F) {
                var8 = 1.0F;
            }

            par1ArrayOfFloat[var7] = var8;
        }

        return par1ArrayOfFloat;
    }

    public float getTemperatureAtHeight(float par1, int par2) {
        return par1;
    }

    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4,
                                                 int par5) {
        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        int[] var7 = getBiomesGens(par2, par3, par4, par5);

        for (int var8 = 0; var8 < par4 * par5; ++var8) {
            par1ArrayOfBiomeGenBase[var8] = BiomeGenBase.biomeList[var7[var8]];
        }

        return par1ArrayOfBiomeGenBase;
    }

    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4,
                                                 int par5) {
        return this.getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, true);
    }

    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5,
                                        boolean par6) {
        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        int[] var7 = getBiomesGens(par2, par3, par4, par5);

        for (int var8 = 0; var8 < par4 * par5; ++var8) {
            par1ArrayOfBiomeGenBase[var8] = BiomeGenBase.biomeList[var7[var8]];
        }

        return par1ArrayOfBiomeGenBase;
    }

    public boolean areBiomesViable(int x, int y, int par3, List par4List) {
        float centerNoise = getNoiseAt(x, y);
        if (centerNoise < 62) {
            return false;
        }

        float lowestNoise = centerNoise;
        float highestNoise = centerNoise;
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 && j != 0) {
                    float n = getNoiseAt(x + i * 16, y + j * 16);
                    if (n < lowestNoise) {
                        lowestNoise = n;
                    }
                    if (n > highestNoise) {
                        highestNoise = n;
                    }
                }
            }
        }

        if (highestNoise - lowestNoise < 22) {
            return true;
        }

        return false;
    }

    public ChunkPosition findBiomePosition(int p_150795_1_, int p_150795_2_, int p_150795_3_, List p_150795_4_,
                                           Random p_150795_5_) {
        return null;
    }

    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }
}
