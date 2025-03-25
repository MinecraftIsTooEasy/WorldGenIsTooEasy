package rwg.biomes.red;

import net.minecraft.*;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RWGBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.deco.DecoCacti;
import rwg.deco.DecoFlowers;
import rwg.deco.DecoGrass;
import rwg.deco.trees.DecoSavannah;
import rwg.surface.SurfaceBase;
import rwg.surface.SurfaceMesa;
import rwg.surface.river.SurfaceRiverOasis;
import rwg.terrain.TerrainBase;
import rwg.terrain.TerrainMesa;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;
import rwg.world.WorldGenBlockBlob;

import java.util.Random;

public class RealisticBiomeMesa extends RealisticBiomeBase {

    private TerrainBase terrain;
    private SurfaceBase surface;
    private SurfaceBase riverSurface;

    public RealisticBiomeMesa() {
        super(0, RWGBaseBiomes.baseHotDesert, RWGBiomes.coastDunes, RWGBaseBiomes.baseRiverOasis);

        terrain = new TerrainMesa();
        surface = new SurfaceMesa(Block.sand, Block.sand, (byte) 1);
        riverSurface = new SurfaceRiverOasis();
    }

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
        for (int l = 0; l < 1; ++l) {
            int i1 = chunkX + rand.nextInt(16) + 8;
            int j1 = chunkY + rand.nextInt(16) + 8;
            int k1 = world.getHeightValue(i1, j1);
            if (k1 < 83) {
                (new WorldGenBlockBlob(Block.cobblestone, 0)).generate(world, rand, i1, k1, j1);
            }
        }

        if (river > 0.7f) {
            if (river > 0.86f) {
                for (int b33 = 0; b33 < 10f * strength; b33++) {
                    int j6 = chunkX + rand.nextInt(16) + 8;
                    int k10 = chunkY + rand.nextInt(16) + 8;
                    int z52 = world.getHeightValue(j6, k10);

                    if (z52 < 100f || (z52 < 120f && rand.nextInt(10) == 0)) {
                        WorldGenerator worldgenerator = rand.nextInt(4) != 0 ? new WorldGenShrub(0, 0)
                                : new DecoSavannah(1);
                        worldgenerator.setScale(1.0D, 1.0D, 1.0D);
                        worldgenerator.generate(world, rand, j6, z52, k10);
                    }
                }
            }

            for (int k18 = 0; k18 < 12f * strength; k18++) {
                int k21 = chunkX + rand.nextInt(16) + 8;
                int j23 = rand.nextInt(160);
                int k24 = chunkY + rand.nextInt(16) + 8;
                if (j23 < 120f) {
                    (new DecoCacti(false)).generate(world, rand, k21, j23, k24);
                }
            }

            for (int f25 = 0; f25 < 2f * strength; f25++) {
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
        } else {
            for (int b1 = 0; b1 < 3; b1++) {
                int j6 = chunkX + rand.nextInt(16) + 8;
                int k10 = chunkY + rand.nextInt(16) + 8;
                int z52 = world.getHeightValue(j6, k10);

                if (z52 < 90) {
                    WorldGenerator worldgenerator;
                    worldgenerator = new WorldGenShrub(0, 0);
                    worldgenerator.setScale(1.0D, 1.0D, 1.0D);
                    worldgenerator.generate(world, rand, j6, z52, k10);
                }
            }

            for (int i15 = 0; i15 < 3; i15++) {
                int i17 = chunkX + rand.nextInt(16) + 8;
                int i20 = 60 + rand.nextInt(40);
                int l22 = chunkY + rand.nextInt(16) + 8;
                (new WorldGenDeadBush(Block.deadBush.blockID)).generate(world, rand, i17, i20, l22);
            }

            for (int k18 = 0; k18 < 18; k18++) {
                int k21 = chunkX + rand.nextInt(16) + 8;
                int j23 = 60 + rand.nextInt(40);
                int k24 = chunkY + rand.nextInt(16) + 8;
                (new WorldGenCactus()).generate(world, rand, k21, j23, k24);
            }
        }
    }

    public float rNoise(NoiseGenerator perlin, CellNoise cell, int x, int y, float ocean, float border, float river) {
        return terrain.generateNoise(perlin, cell, x, y, ocean, border, river);
    }

    public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                         Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        surface.paintTerrain(blocks, metadata, i, j, x, y, depth, world, rand, perlin, cell, noise, river, base);
        riverSurface.paintTerrain(blocks, metadata, i, j, x, y, depth, world, rand, perlin, cell, noise, river, base);
    }
}
