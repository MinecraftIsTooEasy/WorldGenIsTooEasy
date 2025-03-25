package rwg.world;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.biomes.RWGBiomes;
import rwg.biomes.RealisticBiomeBase;

public class MapGenSurfaceCaves extends MapGenCaves2 {
    protected void recursiveGenerate(World worldIn, int dx, int dz, int cx, int cz, Block[] blocks) {
        RealisticBiomeBase biome = ((ChunkManagerRealistic) worldIn.getWorldChunkManager()).getBiomeDataAt(dx << 4, dz << 4);
        int topY = 128, bottomY = 40;
        int numAttempts = 0;
        if (biome != RWGBiomes.polar && this.rand.nextInt(100) < 7) {
            numAttempts = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);
        }

        if (RWGBiomes.mountainsBiomes.contains(biome)) {
            numAttempts = numAttempts * 3 / 4;
        }

        for (int i = 0; i < numAttempts; ++i) {
            double caveStartX = (dx << 4) + this.rand.nextInt(16);
            double caveStartY = this.rand.nextInt(topY - bottomY) + bottomY;
            double caveStartZ = (dz << 4) + this.rand.nextInt(16);

            int numAddTunnelCalls = 1;


            for (int j = 0; j < numAddTunnelCalls; ++j) {
                float yaw = this.rand.nextFloat() * ((float) Math.PI * 2F);
                float pitch = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float width = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                this.addTunnel(this.rand.nextLong(), cx, cz, blocks, caveStartX, caveStartY, caveStartZ, width, yaw, pitch, 0, 0, 1.0D);
            }
        }
    }

    protected void digBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop) {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + (chunkX << 4), z + (chunkZ << 4));
        Block top = (isExceptionBiome(biome) ? Block.grass : Block.blocksList[biome.topBlock]);
        Block filler = (isExceptionBiome(biome) ? Block.dirt : Block.blocksList[biome.fillerBlock]);
        Block block = data[index];

        if (this.canReplaceBlock(block, null) || block == filler || block == top) {
            if (y < 6) {
                data[index] = Block.lavaStill;
            } else {
                data[index] = null;

                if (foundTop && data[index - 1] == filler) {
                    data[index - 1] = top;
                }
            }
        }
    }
}
