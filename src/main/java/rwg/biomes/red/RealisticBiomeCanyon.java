package rwg.biomes.red;

import net.minecraft.*;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RWGBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.deco.DecoCacti;
import rwg.surface.SurfaceBase;
import rwg.surface.SurfaceCanyon;
import rwg.terrain.TerrainBase;
import rwg.terrain.TerrainCanyon;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;
import rwg.world.WorldGenBlockBlob;

import java.util.Random;

public class RealisticBiomeCanyon extends RealisticBiomeBase {

    private TerrainBase terrain;
    private SurfaceBase surface;

    public RealisticBiomeCanyon() {
        super(0, RWGBaseBiomes.baseHotPlains, RWGBiomes.coastDunes, RWGBaseBiomes.baseRiverOasis);

        terrain = new TerrainCanyon(true, 35f, 160f, 60f, 40f, 69f);
        surface = new SurfaceCanyon(Block.sand, Block.sand, (byte) 1, 0);
    }

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
        for (int l = 0; l < 1; ++l) {
            int i1 = chunkX + rand.nextInt(16) + 8;
            int j1 = chunkY + rand.nextInt(16) + 8;
            int k1 = world.getHeightValue(i1, j1);
            if (k1 < 70) {
                (new WorldGenBlockBlob(Block.cobblestoneMossy, 0)).generate(world, rand, i1, k1, j1);
            }
        }

        if (river > 0.8f) {
            for (int b1 = 0; b1 < 10; b1++) {
                int j6 = chunkX + rand.nextInt(16) + 8;
                int k10 = chunkY + rand.nextInt(16) + 8;
                int z52 = world.getHeightValue(j6, k10);

                WorldGenerator worldgenerator;
                worldgenerator = new WorldGenShrub(0, 0);
                worldgenerator.setScale(1.0D, 1.0D, 1.0D);
                worldgenerator.generate(world, rand, j6, z52, k10);
            }
        } else {
            for (int b1 = 0; b1 < 5; b1++) {
                int j6 = chunkX + rand.nextInt(16) + 8;
                int k10 = chunkY + rand.nextInt(16) + 8;
                int z52 = world.getHeightValue(j6, k10);

                WorldGenerator worldgenerator;
                worldgenerator = new WorldGenShrub(0, 0);
                worldgenerator.setScale(1.0D, 1.0D, 1.0D);
                worldgenerator.generate(world, rand, j6, z52, k10);
            }

            if (rand.nextInt(3) == 0) {
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

            for (int i15 = 0; i15 < 5; i15++) {
                int i17 = chunkX + rand.nextInt(16) + 8;
                int i20 = rand.nextInt(160);
                int l22 = chunkY + rand.nextInt(16) + 8;
                (new WorldGenDeadBush(Block.deadBush.blockID)).generate(world, rand, i17, i20, l22);
            }

            for (int k18 = 0; k18 < 14f; k18++) {
                int k21 = chunkX + rand.nextInt(16) + 8;
                int j23 = rand.nextInt(160);
                int k24 = chunkY + rand.nextInt(16) + 8;
                (new DecoCacti(true)).generate(world, rand, k21, j23, k24);
            }
        }
    }

    @Override
    public float rNoise(NoiseGenerator perlin, CellNoise cell, int x, int y, float ocean, float border, float river) {
        return terrain.generateNoise(perlin, cell, x, y, ocean, border, river);
    }

    @Override
    public void rReplace(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                         Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
        surface.paintTerrain(blocks, metadata, i, j, x, y, depth, world, rand, perlin, cell, noise, river, base);
    }
}
