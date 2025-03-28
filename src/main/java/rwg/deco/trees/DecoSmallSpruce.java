package rwg.deco.trees;

import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoSmallSpruce extends WorldGenerator {

    private int treeSize;

    public DecoSmallSpruce(int s) {
        treeSize = s;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        Block g = world.getBlock(x, y - 1, z);
        if (g != Block.grass && g != Block.dirt) {
            return false;
        }

        int leavesMeta = rand.nextInt(2);

        int start = 1;
        int small = 2;
        int large = 0;
        if (treeSize == 1) {
            small = 2;
            large = 2;
        } else if (treeSize == 2) {
            start = 1 + rand.nextInt(2);
            small = 3;
            large = 3;
        }

        int i, j, k;
        for (i = 0; i < start; i++) {
            world.setBlock(x, y, z, Block.wood.blockID, 0, 0);
            y++;
        }

        for (i = 0; i < large; i++) {
            for (j = -2; j <= 2; j++) {
                for (k = -2; k <= 2; k++) {
                    if (Math.abs(j) + Math.abs(k) != 4
                            && ((j > -2 && k > -2 && j < 2 && k < 2) || rand.nextInt(4) != 0)) {
                        world.setBlock(x + j, y, z + k, Block.leaves.blockID, leavesMeta, 0);
                    }
                }
            }
            world.setBlock(x, y, z, Block.wood.blockID, 0, 0);
            y++;
        }

        for (i = 0; i < small; i++) {
            for (j = -1; j <= 1; j++) {
                for (k = -1; k <= 1; k++) {
                    if (Math.abs(j) + Math.abs(k) < 2 || (rand.nextInt(4) != 0)) {
                        world.setBlock(x + j, y, z + k, Block.leaves.blockID, leavesMeta, 0);
                    }
                }
            }

            if (i == 0) {
                world.setBlock(x + 1, y, z, Block.leaves.blockID, leavesMeta, 0);
                world.setBlock(x - 1, y, z, Block.leaves.blockID, leavesMeta, 0);
                world.setBlock(x, y, z + 1, Block.leaves.blockID, leavesMeta, 0);
                world.setBlock(x, y, z - 1, Block.leaves.blockID, leavesMeta, 0);
                world.setBlock(x + 2, y, z, Block.leaves.blockID, leavesMeta, 0);
                world.setBlock(x - 2, y, z, Block.leaves.blockID, leavesMeta, 0);
                world.setBlock(x, y, z + 2, Block.leaves.blockID, leavesMeta, 0);
                world.setBlock(x, y, z - 2, Block.leaves.blockID, leavesMeta, 0);
            }

            world.setBlock(x, y, z, Block.wood.blockID, 0, 0);
            y++;
        }

        world.setBlock(x, y, z, Block.wood.blockID, 0, 0);
        world.setBlock(x + 1, y, z, Block.leaves.blockID, leavesMeta, 0);
        world.setBlock(x - 1, y, z, Block.leaves.blockID, leavesMeta, 0);
        world.setBlock(x, y, z + 1, Block.leaves.blockID, leavesMeta, 0);
        world.setBlock(x, y, z - 1, Block.leaves.blockID, leavesMeta, 0);

        world.setBlock(x, y + 1, z, Block.leaves.blockID, leavesMeta, 0);
        world.setBlock(x, y + 2, z, Block.leaves.blockID, leavesMeta, 0);
        return true;
    }
}
