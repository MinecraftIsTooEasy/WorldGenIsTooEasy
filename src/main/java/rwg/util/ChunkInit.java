package rwg.util;

import net.minecraft.Block;
import net.minecraft.Chunk;
import net.minecraft.ExtendedBlockStorage;
import net.minecraft.World;

public class ChunkInit {
    public static Chunk init(World world, Chunk chunk, Block[] blocks, byte[] metadata) {
        int k = blocks.length / 256;
        boolean flag = !world.provider.hasNoSky;

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < k; ++y) {
                    int index = x << 12 | z << 8 | y;
                    Block block = blocks[index];

                    if (block != null) {
                        int k1 = y >> 4;

                        if (chunk.storageArrays[k1] == null) {
                            chunk.storageArrays[k1] = new ExtendedBlockStorage(k1 << 4, flag);
                        }

                        int id = block.blockID;
                        chunk.storageArrays[k1].setExtBlockID(x, y & 15, z, id);
                        chunk.storageArrays[k1].setExtBlockMetadata(x, y & 15, z, metadata[index]);
                    }
                }
            }
        }

        return chunk;
    }
}
