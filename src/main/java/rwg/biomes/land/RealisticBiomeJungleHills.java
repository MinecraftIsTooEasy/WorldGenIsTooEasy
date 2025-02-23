package rwg.biomes.land;

import net.minecraft.*;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.deco.DecoGrass;
import rwg.deco.DecoMelon;
import rwg.surface.SurfaceBase;
import rwg.surface.SurfaceMountainStone;
import rwg.terrain.TerrainBase;
import rwg.terrain.TerrainHilly;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class RealisticBiomeJungleHills extends RealisticBiomeBase {

    private TerrainBase terrain;
    private SurfaceBase surface;

    public RealisticBiomeJungleHills() {
        super(0, RWGBaseBiomes.baseJungle);
        terrain = new TerrainHilly(230f, 120f, 50f);
        surface = new SurfaceMountainStone(Block.grass, Block.dirt, false, null, 1f, 1.5f, 60f, 65f, 1.5f);
    }

    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
        for (int b33 = 0; b33 < 2; b33++) {
            int j6 = chunkX + rand.nextInt(16) + 8;
            int k10 = chunkY + rand.nextInt(16) + 8;
            int z52 = world.getHeightValue(j6, k10);

            if (z52 < 100 || rand.nextInt(3) == 0) {
                WorldGenerator worldgenerator = rand.nextInt(5) == 0 ? new WorldGenShrub(0, 0)
                        : rand.nextInt(3) == 0 ? new WorldGenHugeTrees(false, 5, 3, 3)
                        : new WorldGenTrees(false, 3 + rand.nextInt(5), 3, 3, true);
                worldgenerator.setScale(1.0D, 1.0D, 1.0D);
                worldgenerator.generate(world, rand, j6, z52, k10);
            }
        }

        if (rand.nextInt((int) (15f / strength)) == 0) {
            int j16 = chunkX + rand.nextInt(16) + 8;
            int j18 = rand.nextInt(100);
            int j21 = chunkY + rand.nextInt(16) + 8;
            (new DecoMelon()).generate(world, rand, j16, j18, j21);
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
