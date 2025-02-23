package rwg.world;

import net.minecraft.*;
import rwg.biomes.RealisticBiomeBase;
import rwg.config.ConfigRWG;
import rwg.deco.DecoClay;
import rwg.util.NoiseGenerator;
import rwg.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChunkGeneratorRealistic implements IChunkProvider {
    public final MapGenBase2 caves;
    public final MapGenCaveNetwork2 cave_network_generator = new MapGenCaveNetwork2();
    public final MapGenStronghold strongholdGenerator;
    public final MapGenMineshaft mineshaftGenerator;
    public final MapGenVillage villageGenerator;
    private final int sampleSize = 8;
    private final int sampleArraySize;
    private final int parabolicSize;
    private final int parabolicArraySize;
    private final float[] parabolicField;
    private Random rand;
    private Random mapRand;
    private World worldObj;
    private ChunkManagerRealistic cmr;
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    private NoiseGenerator perlin;
    private CellNoise cell;
    private RealisticBiomeBase[] biomesForGeneration;
    private BiomeGenBase[] baseBiomesList;
    private float parabolicFieldTotal;

    private int[] biomeData;
    private float[][] hugeRender;
    private float[][] smallRender;
    private float[] testHeight;
    private float[] mapGenBiomes;
    private float[] borderNoise;

    private long worldSeed;

    private WorldGenPlants bush_gen = new WorldGenPlants(Block.bush);
    private WorldGenMinable ore_dirt = new WorldGenMinable(Block.dirt.blockID, 32);
    private WorldGenMinable ore_gravel = new WorldGenMinable(Block.gravel.blockID, 32);
    private WorldGenMinable ore_coal = new WorldGenMinable(Block.oreCoal.blockID, 16);
    private WorldGenMinable ore_iron = new WorldGenMinable(Block.oreIron.blockID, 6);
    private WorldGenMinable ore_copper = new WorldGenMinable(Block.oreCopper.blockID, 6);
    private WorldGenMinable ore_silver = new WorldGenMinable(Block.oreSilver.blockID, 6);
    private WorldGenMinable ore_gold = new WorldGenMinable(Block.oreGold.blockID, 4);
    private WorldGenMinable ore_redstone = new WorldGenMinable(Block.oreRedstone.blockID, 5);
    private WorldGenMinable ore_diamond = new WorldGenMinable(Block.oreDiamond.blockID, 3);
    private WorldGenMinable ore_lapis = new WorldGenMinable(Block.oreLapis.blockID, 3);
    private WorldGenMinable ore_mithril = new WorldGenMinable(Block.oreMithril.blockID, 3);
    private WorldGenMinable silverfish = new WorldGenMinable(Block.silverfish.blockID, 3);

    public ChunkGeneratorRealistic(World world, long l) {
        worldObj = world;
        cmr = (ChunkManagerRealistic) worldObj.getWorldChunkManager();
        rand = new Random(l);
        perlin = NoiseSelector.createNoiseGenerator(l);
        cell = new CellNoise(l, (short) 0);
        cell.setUseDistance(true);

        mapRand = new Random(l);
        worldSeed = l;

        caves = new MapGenCaves2();
        Map m = new HashMap();
        m.put("size", "0");
        m.put("distance", "24");
        villageGenerator = new MapGenVillage(m);
        strongholdGenerator = new MapGenStronghold();
        mineshaftGenerator = new MapGenMineshaft();

        CanyonColor.init(l);

        sampleArraySize = sampleSize * 2 + 5;

        parabolicSize = sampleSize;
        parabolicArraySize = parabolicSize * 2 + 1;
        parabolicField = new float[parabolicArraySize * parabolicArraySize];
        for (int j = -parabolicSize; j <= parabolicSize; ++j) {
            for (int k = -parabolicSize; k <= parabolicSize; ++k) {
                float f = 0.445f / MathHelper.sqrt_float((float) ((j * 1) * (j * 1) + (k * 1) * (k * 1)) + 0.3F);
                parabolicField[j + parabolicSize + (k + parabolicSize) * parabolicArraySize] = f;
                parabolicFieldTotal += f;
            }
        }

        baseBiomesList = new BiomeGenBase[256];
        biomeData = new int[sampleArraySize * sampleArraySize];
        hugeRender = new float[81][256];
        smallRender = new float[625][256];
        testHeight = new float[256];
        mapGenBiomes = new float[258];
        borderNoise = new float[256];
    }

    public Chunk provideChunk(int cx, int cz) {
        if (!this.worldObj.isChunkWithinBlockDomain(cx, cz)) {
            Chunk chunk = new Chunk(this.worldObj, cx, cz);
            chunk.generateSkylightMap(true);
            return chunk;
        }

        rand.setSeed((long) cx * 0x4f9939f508L + (long) cz * 0x1ef1565bd5L);
        Block[] blocks = new Block[65536];
        byte[] metadata = new byte[65536];
        float[] noise = new float[256];
        biomesForGeneration = new RealisticBiomeBase[256];
        int k;

        generateTerrain(cmr, cx, cz, blocks, metadata, biomesForGeneration, noise);

        for (k = 0; k < 256; k++) {
            if (mapGenBiomes[k] > 0f) {
                RealisticBiomeBase.getBiome(k).generateMapGen(
                        blocks,
                        metadata,
                        worldSeed,
                        worldObj,
                        cmr,
                        mapRand,
                        cx,
                        cz,
                        perlin,
                        cell,
                        noise);
                mapGenBiomes[k] = 0f;
            }
            baseBiomesList[k] = biomesForGeneration[k].baseBiome;
        }

        replaceBlocksForBiome(cx, cz, blocks, metadata, biomesForGeneration, baseBiomesList, noise);

        if (ConfigRWG.generateCaves)
            caves.generate(this, worldObj, cx, cz, blocks);
        if (ConfigRWG.generateCaveNetwork)
            cave_network_generator.generate(this, worldObj, cx, cz, blocks);

        Chunk chunk = ChunkInit.init(worldObj, new Chunk(this.worldObj, cx, cz), blocks, metadata);

        byte[] abyte1 = chunk.getBiomeArray();
        for (k = 0; k < abyte1.length; ++k) {
            abyte1[k] = (byte) this.baseBiomesList[k].biomeID;
        }

        chunk.generateSkylightMap(true);
        if (this.worldObj.pending_sand_falls != null) {
            chunk.pending_sand_falls = this.worldObj.pending_sand_falls;
            this.worldObj.pending_sand_falls = null;
        }

        return chunk;
    }

    public void generateTerrain(ChunkManagerRealistic cmr, int cx, int cy, Block[] blocks, byte[] metadata,
                                RealisticBiomeBase[] biomes, float[] n) {
        int p, h;
        float[] noise = getNewNoise(cmr, cx * 16, cy * 16, biomes);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                h = (int) noise[j * 16 + i];

                for (int k = 0; k < 256; k++) {
                    p = (j * 16 + i) * 256 + k;
                    if (k > h) {
                        if (k < 63) {
                            blocks[p] = Block.waterStill;
                        } else {
                            blocks[p] = null;
                        }
                    } else {
                        blocks[p] = Block.stone;
                    }
                }
                n[j * 16 + i] = noise[j * 16 + i];
            }
        }
    }

    public float[] getNewNoise(ChunkManagerRealistic cmr, int x, int y, RealisticBiomeBase[] biomes) {
        int i, j, k, l, m, n, p;

        for (i = -sampleSize; i < sampleSize + 5; i++) {
            for (j = -sampleSize; j < sampleSize + 5; j++) {
                biomeData[(i + sampleSize) * sampleArraySize + (j + sampleSize)] = cmr
                        .getBiomeDataAt(x + ((i * 8) - 8), y + ((j * 8) - 8)).biomeID;
            }
        }

        for (i = -1; i < 4; i++) {
            for (j = -1; j < 4; j++) {
                hugeRender[(i * 2 + 2) * 9 + (j * 2 + 2)] = new float[256];
                for (k = -parabolicSize; k <= parabolicSize; k++) {
                    for (l = -parabolicSize; l <= parabolicSize; l++) {
                        hugeRender[(i * 2 + 2) * 9 + (j * 2 + 2)][biomeData[(i + k + sampleSize + 1) * sampleArraySize
                                + (j + l + sampleSize + 1)]] += parabolicField[k + parabolicSize
                                + (l + parabolicSize) * parabolicArraySize] / parabolicFieldTotal;
                    }
                }
            }
        }

        // MAIN BIOME CHECK
        RealisticBiomeBase b = null;
        for (i = 0; i < 256; i++) {
            if (hugeRender[4 * 9 + 4][i] > 0.95f) {
                b = RealisticBiomeBase.getBiome(i);
            }
        }

        // RENDER HUGE 1
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                hugeRender[(i * 2 + 1) * 9 + (j * 2 + 1)] = mix4(
                        new float[][]{hugeRender[(i * 2) * 9 + (j * 2)], hugeRender[(i * 2 + 2) * 9 + (j * 2)],
                                hugeRender[(i * 2) * 9 + (j * 2 + 2)], hugeRender[(i * 2 + 2) * 9 + (j * 2 + 2)]});
            }
        }

        // RENDER HUGE 2
        for (i = 0; i < 7; i++) {
            for (j = 0; j < 7; j++) {
                if (!(i % 2 == 0 && j % 2 == 0) && !(i % 2 != 0 && j % 2 != 0)) {
                    smallRender[(i * 4) * 25 + (j * 4)] = mix4(
                            new float[][]{hugeRender[(i) * 9 + (j + 1)], hugeRender[(i + 1) * 9 + (j)],
                                    hugeRender[(i + 1) * 9 + (j + 2)], hugeRender[(i + 2) * 9 + (j + 1)]});
                } else {
                    smallRender[(i * 4) * 25 + (j * 4)] = hugeRender[(i + 1) * 9 + (j + 1)];
                }
            }
        }

        // RENDER SMALL 1
        for (i = 0; i < 6; i++) {
            for (j = 0; j < 6; j++) {
                smallRender[(i * 4 + 2) * 25 + (j * 4 + 2)] = mix4(
                        new float[][]{smallRender[(i * 4) * 25 + (j * 4)], smallRender[(i * 4 + 4) * 25 + (j * 4)],
                                smallRender[(i * 4) * 25 + (j * 4 + 4)], smallRender[(i * 4 + 4) * 25 + (j * 4 + 4)]});
            }
        }

        // RENDER SMALL 2
        for (i = 0; i < 11; i++) {
            for (j = 0; j < 11; j++) {
                if (!(i % 2 == 0 && j % 2 == 0) && !(i % 2 != 0 && j % 2 != 0)) {
                    smallRender[(i * 2 + 2) * 25 + (j * 2 + 2)] = mix4(
                            new float[][]{smallRender[(i * 2) * 25 + (j * 2 + 2)],
                                    smallRender[(i * 2 + 2) * 25 + (j * 2)],
                                    smallRender[(i * 2 + 2) * 25 + (j * 2 + 4)],
                                    smallRender[(i * 2 + 4) * 25 + (j * 2 + 2)]});
                }
            }
        }

        // RENDER SMALL 3
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                smallRender[(i * 2 + 3) * 25 + (j * 2 + 3)] = mix4(
                        new float[][]{smallRender[(i * 2 + 2) * 25 + (j * 2 + 2)],
                                smallRender[(i * 2 + 4) * 25 + (j * 2 + 2)],
                                smallRender[(i * 2 + 2) * 25 + (j * 2 + 4)],
                                smallRender[(i * 2 + 4) * 25 + (j * 2 + 4)]});
            }
        }

        // RENDER SMALL 4
        for (i = 0; i < 16; i++) {
            for (j = 0; j < 16; j++) {
                if (!(i % 2 == 0 && j % 2 == 0) && !(i % 2 != 0 && j % 2 != 0)) {
                    smallRender[(i + 4) * 25 + (j + 4)] = mix4(
                            new float[][]{smallRender[(i + 3) * 25 + (j + 4)], smallRender[(i + 4) * 25 + (j + 3)],
                                    smallRender[(i + 4) * 25 + (j + 5)], smallRender[(i + 5) * 25 + (j + 4)]});
                }
            }
        }

        // CREATE BIOMES ARRAY
        boolean randBiome = true;
        float bCount = 0f, bRand = 0f;
        if (b != null) {
            randBiome = false;
            for (i = 0; i < 256; i++) {
                biomes[i] = b;
            }
        }

        float river, ocean;
        for (i = 0; i < 16; i++) {
            for (j = 0; j < 16; j++) {
                if (randBiome) {
                    bCount = 0f;
                    bRand = 0.5f + perlin.noise2((float) (x + i) / 15f, (float) (y + j) / 15f);
                    bRand = bRand < 0f ? 0f : Math.min(bRand, 0.99999f);
                }

                ocean = cmr.getOceanValue(x + i, y + j);
                l = ((int) (i + 4) * 25 + (j + 4));

                testHeight[i * 16 + j] = 0f;

                river = cmr.getRiverStrength(x + i, y + j);

                if (l == 312) {
                    mapGenBiomes[256] = ocean;
                    mapGenBiomes[257] = river;
                }

                for (k = 0; k < 256; k++) {
                    if (smallRender[l][k] > 0f) {
                        if (randBiome && bCount <= 1f) // 3f)
                        {
                            bCount += smallRender[l][k]; // * 3f;
                            if (bCount > bRand) {
                                biomes[j * 16 + i] = RealisticBiomeBase.getBiome(k);
                                bCount = 2f; // 20f;
                            }
                        }

                        if (l == 312) {
                            mapGenBiomes[k] = smallRender[312][k];
                        }

                        testHeight[i * 16 + j] += cmr.calculateRiver(
                                x + i,
                                y + j,
                                river,
                                RealisticBiomeBase.getBiome(k)
                                        .rNoise(perlin, cell, x + i, y + j, ocean, smallRender[l][k], river + 1f))
                                * smallRender[l][k];
                    }
                }
            }
        }

        return testHeight;
    }

    public float[] mix4(float[][] ingredients) {
        float[] result = new float[256];
        int i, j;
        for (i = 0; i < 256; i++) {
            for (j = 0; j < 4; j++) {
                if (ingredients[j][i] > 0f) {
                    result[i] += ingredients[j][i] / 4f;
                }
            }
        }

        return result;
    }

    public void replaceBlocksForBiome(int cx, int cy, Block[] blocks, byte[] metadata, RealisticBiomeBase[] biomes,
                                      BiomeGenBase[] base, float[] n) {
        int i, j, depth;
        float river;
        for (i = 0; i < 16; i++) {
            for (j = 0; j < 16; j++) {
                RealisticBiomeBase biome = biomes[i * 16 + j];

                river = -cmr.getRiverStrength(cx * 16 + j, cy * 16 + i);
                if (river > 0.05f && river + (perlin.noise2((cx * 16 + j) / 10f, (cy * 16 + i) / 10f) * 0.15f) > 0.8f) {
                    base[i * 16 + j] = biome.riverBiome;
                }

                depth = -1;

                biome.rReplace(
                        blocks,
                        metadata,
                        cx * 16 + j,
                        cy * 16 + i,
                        i,
                        j,
                        depth,
                        worldObj,
                        rand,
                        perlin,
                        cell,
                        n,
                        river,
                        base);

                blocks[(j * 16 + i) * 256] = Block.bedrock;
                blocks[(j * 16 + i) * 256 + rand.nextInt(2)] = Block.bedrock;
                blocks[(j * 16 + i) * 256 + rand.nextInt(3)] = Block.bedrock;
                blocks[(j * 16 + i) * 256 + rand.nextInt(4)] = Block.bedrock;
                blocks[(j * 16 + i) * 256 + rand.nextInt(5)] = Block.bedrock;
            }
        }
    }

    protected void genMinable(int cx, int cz, int frequency, WorldGenMinable world_gen_minable) {
        this.genMinable(cx, cz, frequency, world_gen_minable, false);
    }

    protected void genMinable(int cx, int cz, int frequency, WorldGenMinable world_gen_minable, boolean vein_size_increases_with_depth) {
        while (frequency-- > 0) {
            if (this.rand.nextInt(8) == 0) {
                int x = cx + this.rand.nextInt(16);
                int y = world_gen_minable.getRandomVeinHeight(this.worldObj, this.rand);
                int z = cz + this.rand.nextInt(16);
                if (y >= 0) {
                    world_gen_minable.generate(this.worldObj, this.rand, x, y, z, vein_size_increases_with_depth);
                }
            }
        }
    }

    public Chunk loadChunk(int cx, int cz) {
        return provideChunk(cx, cz);
    }

    public boolean chunkExists(int cx, int cz) {
        return true;
    }

    @Override
    public Chunk getChunkIfItExists(int i, int i1) {
        return null;
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j) {
        BlockFalling.fallInstantly = true;
        int x = i * 16;
        int y = j * 16;
        RealisticBiomeBase biome = cmr.getBiomeDataAt(x + 16, y + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) i * i1 + (long) j * j1 ^ this.worldObj.getSeed());

        if (ConfigRWG.generateMineshafts) {
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, i, j);
        }
        strongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
        if (ConfigRWG.generateVillages) {
            villageGenerator.generateStructuresInChunk(worldObj, rand, i, j);
        }

        if (ConfigRWG.generateUndergroundLakes) {
            if (rand.nextInt(10) == 0) {
                int i2 = x + rand.nextInt(16) + 8;
                int l4 = rand.nextInt(50);
                int i8 = y + rand.nextInt(16) + 8;
                (new WorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, i2, l4, i8);
            }
        }

        if (ConfigRWG.generateUndergroundLavaLakes) {
            if (rand.nextInt(18) == 0) {
                int j2 = x + rand.nextInt(16) + 8;
                int i5 = rand.nextInt(rand.nextInt(45) + 8);
                int j8 = y + rand.nextInt(16) + 8;
                if (rand.nextInt(10) == 0) {
                    (new WorldGenLakes(Block.lavaStill.blockID)).generate(worldObj, rand, j2, i5, j8);
                }
            }
        }

        for (int k1 = 0; k1 < 8; k1++) {
            int j5 = x + rand.nextInt(16) + 8;
            int k8 = rand.nextInt(128);
            int j11 = y + rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(worldObj, rand, j5, k8, j11);
        }

        float river = -cmr.getRiverStrength(x + 16, y + 16);
        if (river > 0.85f) {
            for (int j2 = 0; j2 < 3; j2++) {
                int l5 = x + rand.nextInt(16);
                int i9 = 53 + rand.nextInt(15);
                int l11 = y + rand.nextInt(16);
                (new DecoClay(Block.blockClay, 0, 20)).generate(worldObj, rand, l5, i9, l11);
            }
        }

        this.genMinable(x, y, 200, this.ore_dirt);
        this.genMinable(x, y, 200, this.ore_gravel);
        this.genMinable(x, y, 50, this.ore_coal);
        this.genMinable(x, y, 40, this.ore_copper, true);
        this.genMinable(x, y, 10, this.ore_silver, true);
        this.genMinable(x, y, 20, this.ore_gold, true);
        this.genMinable(x, y, 60, this.ore_iron, true);
        this.genMinable(x, y, 10, this.ore_mithril, true);
        this.genMinable(x, y, 5, this.silverfish, true);
        this.genMinable(x, y, 10, this.ore_redstone);
        //this.genMinable(x, y, 5, this.ore_diamond);
        this.genMinable(x, y, 5, this.ore_lapis);

        if (ConfigRWG.generateEmeralds) {
            for (int g12 = 0; g12 < 2; ++g12) {
                int n1 = x + rand.nextInt(16);
                int m1 = rand.nextInt(28) + 4;
                int p1 = y + rand.nextInt(16);

                if (worldObj.getBlock(n1, m1, p1) == Block.stone) {
                    worldObj.setBlock(n1, m1, p1, Block.oreEmerald.blockID, 0, 2);
                }
            }
        }

        if (rand.nextInt(5) == 0) {
            int k15 = x + rand.nextInt(16) + 8;
            int k17 = rand.nextInt(64);
            int k20 = y + rand.nextInt(16) + 8;

            if (rand.nextBoolean()) {
                (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, k15, k17, k20);
            } else {
                (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, k15, k17, k20);
            }
        }

        for (int bx = -4; bx <= 4; bx++) {
            for (int by = -4; by <= 4; by++) {
                borderNoise[cmr.getBiomeDataAt(x + 24 + bx * 16, y + 24 + by * 16).biomeID] += 0.01234569f;
            }
        }

        RealisticBiomeBase b;
        float snow = 0f;
        for (int bn = 0; bn < 256; bn++) {
            if (borderNoise[bn] > 0f) {
                if (borderNoise[bn] >= 1f) {
                    borderNoise[bn] = 1f;
                }
                b = RealisticBiomeBase.getBiome(bn);
                b.rDecorate(this.worldObj, this.rand, x, y, perlin, cell, borderNoise[bn], river);

                if (b.baseBiome.temperature < 0.15f) {
                    snow -= 0.6f * borderNoise[bn];
                } else {
                    snow += 0.6f * borderNoise[bn];
                }
                borderNoise[bn] = 0f;
            }
        }

        for (int l18 = 0; l18 < 50; l18++) {
            int l21 = x + rand.nextInt(16) + 8;
            int k23 = rand.nextInt(rand.nextInt(120) + 8);
            int l24 = y + rand.nextInt(16) + 8;
            WorldGenLiquids.generate(worldObj, rand, Block.waterMoving.blockID, l21, k23, l24);
        }

        for (int i19 = 0; i19 < 20; i19++) {
            int i22 = x + rand.nextInt(16) + 8;
            int l23 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int i25 = y + rand.nextInt(16) + 8;
            WorldGenLiquids.generate(worldObj, rand, Block.lavaMoving.blockID, i22, l23, i25);
        }

        if (snow < 0.59f) {
            x += 8;
            y += 8;
            float s;
            Block b1, b2;

            for (int sn1 = 0; sn1 < 16; ++sn1) {
                for (int sn2 = 0; sn2 < 16; ++sn2) {
                    if (snow < -0.59f) {
                        s = -1f;
                    } else {
                        s = perlin.noise2((sn1 + x) / 3f, (sn2 + y) / 3f) + snow;
                    }

                    if (s < 0f) {
                        int sn3 = worldObj.getPrecipitationHeight(x + sn1, y + sn2);
                        b1 = worldObj.getBlock(sn1 + x, sn3, sn2 + y);
                        b2 = worldObj.getBlock(sn1 + x, sn3 - 1, sn2 + y);

                        if (b2 == Block.waterStill || b2 == Block.waterMoving) {
                            worldObj.setBlock(sn1 + x, sn3 - 1, sn2 + y, Block.ice.blockID, 0, 2);
                        } else if (Block.snow.canBePlacedAt(worldObj, sn1 + x, sn3, sn2 + y, 0) && b2 != Block.ice && sn3 > 62) {
                            if (b1 != Block.snow) {
                                worldObj.setBlock(sn1 + x, sn3, sn2 + y, Block.snow.blockID, 0, 2);
                            }
                        }
                    }
                }
            }
        }

        SpawnerAnimals.performWorldGenSpawning(this.worldObj, biome.baseBiome, EnumCreatureType.animal, x + 8, y + 8, 16, 16, this.rand);
        SpawnerAnimals.performWorldGenSpawning(this.worldObj, biome.baseBiome, EnumCreatureType.aquatic, x + 8, y + 8, 16, 16, this.rand);

        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
        return true;
    }

    @Override
    public void saveExtraData() {
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "RandomLevelSource";
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
        BiomeGenBase var5 = this.worldObj.getBiomeGenForCoords(par2, par4);
        if (var5 == null) {
            return null;
        }
        return (par1EnumCreatureType == EnumCreatureType.monster && this.scatteredFeatureGenerator.func_143030_a(par2, par3, par4)) ? this.scatteredFeatureGenerator.getScatteredFeatureSpawnList() : var5.getSpawnableList(par1EnumCreatureType);
    }

    @Override
    public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5) {
        if (!"Stronghold".equals(par2Str) || this.strongholdGenerator == null) {
            return null;
        }
        return this.strongholdGenerator.getNearestInstance(par1World, par3, par4, par5);
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(int par1, int par2) {
        this.mineshaftGenerator.generate(this, this.worldObj, par1, par2, null);
        this.villageGenerator.generate(this, this.worldObj, par1, par2, null);
        this.strongholdGenerator.generate(this, this.worldObj, par1, par2, null);
        this.scatteredFeatureGenerator.generate(this, this.worldObj, par1, par2, null);
    }
}
