package rwg.biomes.savanna;

import net.minecraft.*;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RWGBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.deco.DecoCacti;
import rwg.deco.DecoFlowers;
import rwg.deco.DecoGrass;
import rwg.deco.trees.DecoSavannah;
import rwg.surface.SurfaceBase;
import rwg.surface.SurfaceGrasslandMix1;
import rwg.terrain.TerrainBase;
import rwg.terrain.TerrainGrasslandFlats;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;
import rwg.world.WorldGenBlockBlob;

import java.util.Random;

public class RealisticBiomeSavanna extends RealisticBiomeBase {

    private TerrainBase terrain;
    private SurfaceBase surface;

    public RealisticBiomeSavanna() {
        super(0, RWGBaseBiomes.baseHotPlains, RWGBiomes.coastDunes, RWGBaseBiomes.baseRiverHot);
        terrain = new TerrainGrasslandFlats();
        surface = new SurfaceGrasslandMix1(
                Block.grass,
                Block.dirt,
                Block.sand,
                Block.stone,
                Block.cobblestone,
                13f,
                0.27f);
    }

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
        for (int i23 = 0; i23 < 1; i23++) {
            int i1 = chunkX + rand.nextInt(16) + 8;
            int j1 = chunkY + rand.nextInt(16) + 8;
            int k1 = world.getHeightValue(i1, j1);
            (new WorldGenBlockBlob(Block.cobblestone, 0)).generate(world, rand, i1, k1, j1);
        }

        if (river > 0.8f) {
            for (int b33 = 0; b33 < 15f * strength; b33++) {
                int j6 = chunkX + rand.nextInt(16) + 8;
                int k10 = chunkY + rand.nextInt(16) + 8;
                int z52 = world.getHeightValue(j6, k10);

                WorldGenerator worldgenerator = rand.nextInt(3) != 0 ? new WorldGenShrub(0, 0)
                        : rand.nextInt(7) == 0 ? new DecoSavannah(1) : new DecoSavannah(2);
                worldgenerator.setScale(1.0D, 1.0D, 1.0D);
                worldgenerator.generate(world, rand, j6, z52, k10);
            }

            for (int f25 = 0; f25 < 2f * strength; f25++) {
                int i18 = chunkX + rand.nextInt(16) + 8;
                int i23 = chunkY + rand.nextInt(16) + 8;
                (new WorldGenReed()).generate(world, rand, i18, 60 + rand.nextInt(8), i23);
            }
        } else if (perlin.noise2(chunkX / 180f, chunkY / 180f) > 0.20f) {
            for (int b33 = 0; b33 < 7f * strength; b33++) {
                int j6 = chunkX + rand.nextInt(16) + 8;
                int k10 = chunkY + rand.nextInt(16) + 8;
                int z52 = world.getHeightValue(j6, k10);

                WorldGenerator worldgenerator = rand.nextInt(9) == 0 ? new WorldGenShrub(0, 0)
                        : rand.nextInt(7) == 0 ? new DecoSavannah(1) : new DecoSavannah(2);
                worldgenerator.setScale(1.0D, 1.0D, 1.0D);
                worldgenerator.generate(world, rand, j6, z52, k10);
            }
        } else {
            int a = 3 - (int) (perlin.noise2(chunkX / 100f, chunkY / 100f) * 7);
            if (a < 1 || rand.nextInt(a) == 0) {
                int j6 = chunkX + rand.nextInt(16) + 8;
                int k10 = chunkY + rand.nextInt(16) + 8;
                int z52 = world.getHeightValue(j6, k10);

                WorldGenerator worldgenerator = rand.nextBoolean() ? new WorldGenShrub(0, 0)
                        : rand.nextInt(5) == 0 ? new DecoSavannah(0) : new DecoSavannah(1);
                worldgenerator.setScale(1.0D, 1.0D, 1.0D);
                worldgenerator.generate(world, rand, j6, z52, k10);
            }
        }

        if (rand.nextInt((int) (3f / strength)) == 0) {
            int i18 = chunkX + rand.nextInt(16) + 8;
            int i23 = chunkY + rand.nextInt(16) + 8;
            (new WorldGenReed()).generate(world, rand, i18, 60 + rand.nextInt(8), i23);
        }

        if (rand.nextInt(28) == 0) {
            int j16 = chunkX + rand.nextInt(16) + 8;
            int j18 = rand.nextInt(128);
            int j21 = chunkY + rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(world, rand, j16, j18, j21);
        }

        for (int f23 = 0; f23 < 3; f23++) {
            int j15 = chunkX + rand.nextInt(16) + 8;
            int j17 = rand.nextInt(128);
            int j20 = chunkY + rand.nextInt(16) + 8;
            int flowerMeta = Block.plantRed.getRandomSubtypeForBiome(rand, baseBiome);
            (new DecoFlowers(4, 11, flowerMeta)).generate(world, rand, j15, j17, j20);
        }

        for (int k18 = 0; k18 < 12; k18++) {
            int k21 = chunkX + rand.nextInt(16) + 8;
            int j23 = rand.nextInt(160);
            int k24 = chunkY + rand.nextInt(16) + 8;
            (new DecoCacti(false)).generate(world, rand, k21, j23, k24);
        }

        for (int l14 = 0; l14 < 15; l14++) {
            int l19 = chunkX + rand.nextInt(16) + 8;
            int k22 = rand.nextInt(128);
            int j24 = chunkY + rand.nextInt(16) + 8;

            if (rand.nextInt(6) == 0) {
                (new DecoGrass(Block.plantRed, 2)).generate(world, rand, l19, k22, j24);
            } else {
                (new DecoGrass(Block.tallGrass, 1)).generate(world, rand, l19, k22, j24);
            }
        }
    }

    public float rNoise(NoiseGenerator perlin, CellNoise cell, int x, int y, float ocean, float border, float river) {
        return terrain.generateNoise(perlin, cell, x, y, ocean, border, river);
    }

    public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                         Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        surface.paintTerrain(blocks, metadata, i, j, x, y, depth, world, rand, perlin, cell, noise, river, base);
    }
}
