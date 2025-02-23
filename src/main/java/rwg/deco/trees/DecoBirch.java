package rwg.deco.trees;

import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoBirch extends WorldGenerator {

    private int startHeight;
    private int treeSize;

    public DecoBirch(int start, int s) {
        startHeight = start;
        treeSize = s;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        Block g = world.getBlock(x, y - 1, z);
        if (g != Block.grass && g != Block.dirt) {
            return false;
        }

        int i;
        for (i = 0; i < startHeight; i++) {
            world.setBlock(x, y, z, Block.wood.blockID, 2, 0);
            y++;
        }

        int pX = 0;
        int pZ = 0;
        for (i = 0; i < treeSize; i++) {
            if (rand.nextInt(1) == 0 && i < treeSize - 2) {
                int dX = -1 + rand.nextInt(3);
                int dZ = -1 + rand.nextInt(3);

                if (dX == 0 && dZ == 0) {
                    dX = -1 + rand.nextInt(3);
                    dZ = -1 + rand.nextInt(3);
                }

                if (pX == dX && rand.nextBoolean()) {
                    dX = -dX;
                }
                if (pZ == dZ && rand.nextBoolean()) {
                    dZ = -dZ;
                }

                pX = dX;
                pZ = dZ;

                buildBranch(world, rand, x, y, z, dX, dZ, 1, i < treeSize - 2 ? 2 : 1); // i < treeSize - 4 ? 2 : 1
            }
            world.setBlock(x, y, z, Block.wood.blockID, 2, 0);

            if (i < treeSize - 2) {
                if (rand.nextBoolean()) {
                    buildLeaves(world, x, y, z + 1);
                }
                if (rand.nextBoolean()) {
                    buildLeaves(world, x, y, z - 1);
                }
                if (rand.nextBoolean()) {
                    buildLeaves(world, x + 1, y, z);
                }
                if (rand.nextBoolean()) {
                    buildLeaves(world, x - 1, y, z);
                }
            }

            y++;
        }

        buildLeaves(world, x, y - 1, z + 1);
        buildLeaves(world, x, y - 1, z - 1);
        buildLeaves(world, x + 1, y - 1, z);
        buildLeaves(world, x - 1, y - 1, z);
        buildLeaves(world, x, y, z);

        return true;
    }

    public void buildBranch(World world, Random rand, int x, int y, int z, int dX, int dZ, int logLength,
                            int leaveSize) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = 0; k < 2; k++) {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) < leaveSize + 1) {
                        buildLeaves(world, x + i + (dX * logLength), y + k, z + j + (dZ * logLength));
                    }
                }
            }
        }

        for (int m = 1; m <= logLength; m++) {
            world.setBlock(x + (dX * m), y, z + (dZ * m), Block.wood.blockID, 2, 0);
        }
    }

    public void buildLeaves(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (b == null) {
            world.setBlock(x, y, z, Block.leaves.blockID, 2, 0);
        }
    }
}
