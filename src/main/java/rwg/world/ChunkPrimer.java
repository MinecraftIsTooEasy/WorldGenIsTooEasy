package rwg.world;


import net.minecraft.Block;

public class ChunkPrimer {
    public final Block[] data;
    public byte[] metadata;

    public ChunkPrimer(Block[] data) {
        this.data = data;
    }

    public ChunkPrimer(Block[] data, byte[] metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    public Block getBlock(int x, int y, int z) {
        return this.data[getBlockIndex(x, y, z)];
    }

    public void setBlock(int x, int y, int z, Block block) {
        this.data[getBlockIndex(x, y, z)] = block;
    }

    public byte getBlockMetadata(int x, int y, int z) {
        return this.metadata[getBlockIndex(x, y, z)];
    }

    public void setBlockMetadata(int x, int y, int z, byte metadata) {
        this.metadata[getBlockIndex(x, y, z)] = metadata;
    }

    public static int getBlockIndex(int x, int y, int z) {
        return x << 12 | z << 8 | y;
    }

    public int findGroundBlockIdx(int x, int z) {
        int i = (x << 12 | z << 8) + 256 - 1;

        for (int j = 255; j >= 0; --j) {
            Block block = this.data[i + j];

            if (block != null) {
                return j;
            }
        }

        return 0;
    }
}
