package rwg.biomes.land;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenerator;
import rwg.biomes.RWGBaseBiomes;
import rwg.biomes.RealisticBiomeBase;
import rwg.deco.trees.DecoJungleFat;
import rwg.surface.SurfaceBase;
import rwg.surface.SurfaceGrassland;
import rwg.terrain.TerrainBase;
import rwg.terrain.TerrainHighland;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class RealisticBiomeHighRainforest extends RealisticBiomeBase {

    private TerrainBase terrain;
    private SurfaceBase surface;

    public RealisticBiomeHighRainforest() {
        super(0, RWGBaseBiomes.baseJungle);

        terrain = new TerrainHighland(0f, 140f, 68f, 200f);
        surface = new SurfaceGrassland(Block.grass, Block.dirt, Block.stone, Block.cobblestone);
    }

    public void rDecorate(World world, Random rand, int chunkX, int chunkY, NoiseGenerator perlin, CellNoise cell,
                          float strength, float river) {
        if (rand.nextInt((int) (1f / strength)) == 0) {
            int j6 = chunkX + 8; // 12 + rand.nextInt(8);
            int k10 = chunkY + 8; // 12 + rand.nextInt(8);
            int z52 = world.getHeightValue(j6, k10);

            WorldGenerator worldgenerator = new DecoJungleFat(
                    Block.wood,
                    3,
                    Block.leaves,
                    3,
                    15 + rand.nextInt(15),
                    4 + rand.nextInt(2),
                    14f,
                    3,
                    0.2f,
                    0.25f);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(world, rand, j6, z52, k10);
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
