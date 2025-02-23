package rwg.deco;

import net.minecraft.*;

import java.util.Random;

public class DecoJungleCane extends WorldGenerator {

    private int height;

    public DecoJungleCane(int h) {
        height = h;
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        Block b;
        while (y > 0) {
            b = world.getBlock(x, y, z);
            if (b == null || b instanceof BlockLeaves) {
                break;
            }
            y--;
        }

        b = world.getBlock(x, y, z);
        if (b != Block.grass && b != Block.dirt) {
            return false;
        }

        int j, sx, sz, ra;
        for (j = 0; j < 4; j++) {
            b = world.getBlock(j == 0 ? x - 1 : j == 1 ? x + 1 : x, y, j == 2 ? z - 1 : j == 3 ? z + 1 : z);
            if (b.blockMaterial != Material.dirt && b.blockMaterial != Material.grass) {
                return false;
            }
        }

        for (j = 0; j < 4; j++) {
            sx = j == 0 ? x - 1 : j == 1 ? x + 1 : x;
            sz = j == 2 ? z - 1 : j == 3 ? z + 1 : z;
            ra = rand.nextInt(height * 2 + 1) + height;

            b = world.getBlock(sx, y + 1, sz);
            if (b == null || b.blockMaterial == Material.vine) {
                for (int k = 0; k < ra; k++) {
                    b = world.getBlock(sx, y + 1 + k, sz);
                    if (b == null || b.blockMaterial == Material.vine) {
                        world.setBlock(sx, y + 1 + k, sz, Block.reed.blockID, 0, 2);
                    } else {
                        break;
                    }
                }
            }
        }

        world.setBlock(x, y, z, Block.waterStill.blockID);

        return true;
    }
}
