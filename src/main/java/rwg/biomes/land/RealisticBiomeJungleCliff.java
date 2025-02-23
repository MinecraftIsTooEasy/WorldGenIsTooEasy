package rwg.biomes.land;

import rwg.world.WorldGenBlockBlob;
import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenerator;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.deco.DecoGrass;
import rwg.deco.DecoMelon;
import rwg.deco.trees.DecoJungleSmall;
import rwg.surface.SurfaceBase;
import rwg.surface.SurfaceGrassCliff;
import rwg.terrain.TerrainBase;
import rwg.terrain.TerrainCanyon;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class RealisticBiomeJungleCliff extends RealisticBiomeBase {

    private TerrainBase terrain;
    private SurfaceBase surface;

    public RealisticBiomeJungleCliff() {
        super(0, RWGBaseBiomes.baseHotForest);

        terrain = new TerrainCanyon(true, 35f, 160f, 60f, 40f, 69f);
        surface = new SurfaceGrassCliff(Block.grass, Block.dirt);
    }

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
        for (int l = 0; l < 1f * strength; ++l) {
            int i1 = chunkX + rand.nextInt(16) + 8;
            int j1 = chunkY + rand.nextInt(16) + 8;
            int k1 = world.getHeightValue(i1, j1);
            if (k1 < 70) {
                (new WorldGenBlockBlob(Block.cobblestoneMossy, 0)).generate(world, rand, i1, k1, j1);
            }
        }

        for (int a = 0; a < 5f * strength; a++) {
            int j6 = chunkX + rand.nextInt(16) + 8;
            int k10 = chunkY + rand.nextInt(16) + 8;
            int z52 = world.getHeightValue(j6, k10);

            WorldGenerator worldgenerator = new DecoJungleSmall(
                    Block.wood,
                    3,
                    Block.leaves,
                    3,
                    1 + rand.nextInt(4),
                    0,
                    5f,
                    2,
                    0.32f,
                    0.14f);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(world, rand, j6, z52, k10);
        }

        if (rand.nextInt((int) (15f / strength)) == 0) {
            int j16 = chunkX + rand.nextInt(16) + 8;
            int j18 = rand.nextInt(100);
            int j21 = chunkY + rand.nextInt(16) + 8;
            (new DecoMelon()).generate(world, rand, j16, j18, j21);
        }

        for (int l14 = 0; l14 < 8; l14++) {
            int l19 = chunkX + rand.nextInt(16) + 8;
            int k22 = rand.nextInt(128);
            int j24 = chunkY + rand.nextInt(16) + 8;

            (new DecoGrass(Block.tallGrass, 1)).generate(world, rand, l19, k22, j24);
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
