package rwg.deco.trees;

import net.minecraft.Block;
import net.minecraft.Material;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoSmallCocoa extends WorldGenerator {

    private static int[] cocoas = new int[]{2, 0, -2, 1, 1, 1, -2, 0, 0, 0, -2, -1, 3, -1, -2, 0};

    public DecoSmallCocoa() {
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        Block b = world.getBlock(x, y - 1, z);
        if (b != Block.grass && b != Block.dirt && b != Block.sand) {
            return false;
        }

        Block m = world.getBlock(x, y, z);
        if (m != null && m.blockMaterial != Material.vine) {
            return false;
        }

        int h = y + 2 + rand.nextInt(3);
        for (; y < h; y++) {
            world.setBlock(x, y, z, Block.wood.blockID, 3, 0);
        }

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) + Math.abs(j) < 3) {
                    buildBlock(world, x + i, y - 1, z + j, Block.leaves, 3, 0);
                }
            }
        }

        world.setBlock(x, y - 1, z, Block.wood.blockID, 3, 0);
        buildBlock(world, x + 1, y, z, Block.leaves, 3, 0);
        buildBlock(world, x - 1, y, z, Block.leaves, 3, 0);
        buildBlock(world, x, y, z, Block.leaves, 3, 0);
        buildBlock(world, x, y, z + 1, Block.leaves, 3, 0);
        buildBlock(world, x, y, z - 1, Block.leaves, 3, 0);

        for (int k = 0; k < 16; k += 4) {
            if (rand.nextInt(20) == 0) {
                buildBlock(
                        world,
                        x + cocoas[k + 1],
                        y + cocoas[k + 2],
                        z + cocoas[k + 3],
                        Block.cocoaPlant,
                        cocoas[k] + 8,
                        0);
            }
        }

        return true;
    }

    private void buildBlock(World w, int x, int y, int z, Block b, int m, int u) {
        Block ma = w.getBlock(x, y, z);

        if (ma == null || ma.blockMaterial == Material.vine) {
            w.setBlock(x, y, z, b.blockID, m, u);
        }
    }
}
