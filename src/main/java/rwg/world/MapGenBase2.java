package rwg.world;

import net.minecraft.*;

import java.util.Random;

public class MapGenBase2 {

    protected int range = 8;
    protected Random rand = new Random();
    protected World worldObj;

    public void generate(IChunkProvider provider, World world, int x, int z, Block[] blocks) {
        int k = this.range;
        this.worldObj = world;
        this.rand.setSeed(world.getSeed());
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();

        for (int j1 = x - k; j1 <= x + k; ++j1) {
            for (int k1 = z - k; k1 <= z + k; ++k1) {
                if (this.isGenAllowedInChunk(world, j1, k1)) {
                    long l1 = (long) j1 * l;
                    long i2 = (long) k1 * i1;
                    this.rand.setSeed(l1 ^ i2 ^ world.getSeed());
                    this.recursiveGenerate(world, j1, k1, x, z, blocks);
                }
            }
        }
    }

    protected void recursiveGenerate(World worldIn, int dx, int dz, int cx, int cz, Block[] Blocks) {
    }

    public boolean isGenAllowedInChunk(World world, int chunk_x, int chunk_z) {
        return this.isGenAllowedInBiome(world.getBiomeGenForCoords(chunk_x * 16, chunk_z * 16));
    }

    public boolean isGenAllowedInBiome(BiomeGenBase biome) {
        return true;
    }

    public boolean canReplaceBlock(Block block, Block blockUp) {
        if (block == null || (blockUp != null && blockUp.blockMaterial == Material.water))
            return false;
        return block.blockMaterial == Material.stone
                || block == Block.stone
                || block == Block.dirt
                || block == Block.grass
                || block == Block.hardenedClay
                || block == Block.stainedClay
                || block == Block.sandStone
                || block == Block.mycelium
                || block == Block.snow
                || block == Block.blockSnow
                || block == Block.sand
                || block == Block.gravel;
    }
}
