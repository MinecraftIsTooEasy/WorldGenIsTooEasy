package rwg.deco.trees;

import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoDeadDesertTrees extends WorldGenerator {

    private int type;

    public DecoDeadDesertTrees(int t) {
        type = t;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        Block g = world.getBlock(x, y - 1, z);
        if (g != Block.grass && g != Block.dirt && g != Block.sand) {
            return false;
        }

        if (type == 0) {
            int i, h = 8;

            for (i = 0; i < h; i++) {
                world.setBlock(x, y + i, z, Block.wood.blockID, 0, 0);
            }

            int branches = 2 + rand.nextInt(3);
            float dir, xd, yd, zd, l, c, sk = (360f / branches);

            for (i = 0; i < branches; i++) {
                dir = i * sk + rand.nextFloat() * sk;
                xd = (float) Math.cos(dir * Math.PI / 180f);
                zd = (float) Math.sin(dir * Math.PI / 180f);
                l = 1f + rand.nextFloat() * 3f;
                c = 1f;

                while (c < l) {
                    world.setBlock(x + (int) (xd * c), y + h + (int) c, z + (int) (zd * c), Block.wood.blockID, 12, 0);
                    c += 1f;
                }
            }
        } else {
            int h = rand.nextInt(3) + 2;
            for (int i = 0; i < h; i++) {
                world.setBlock(x, y + i, z, Block.wood.blockID, 0, 0);
            }

            h--;
            world.setBlock(x + 1, y + h, z, Block.leaves.blockID, 0, 0);
            world.setBlock(x - 1, y + h, z, Block.leaves.blockID, 0, 0);
            world.setBlock(x, y + h, z + 1, Block.leaves.blockID, 0, 0);
            world.setBlock(x, y + h, z - 1, Block.leaves.blockID, 0, 0);
            world.setBlock(x, y + h + 1, z, Block.leaves.blockID, 0, 0);
        }

        return true;
    }
}
