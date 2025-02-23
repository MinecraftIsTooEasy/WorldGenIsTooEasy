package rwg.deco;

import net.minecraft.*;

import java.util.Random;

public class DecoWildWheat extends WorldGenerator {
    private static final Block[] farmtypes = new Block[]{
            Block.potato,
            Block.carrot,
            Block.crops,
            Block.onions
    };

    private Block farmtype;

    public DecoWildWheat(int type) {
        farmtype = farmtypes[type];
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        Block b;
        while (y > 0) {
            b = world.getBlock(x, y, z);
            if (!(b == null || b instanceof BlockLeaves)) {
                break;
            }
            y--;
        }

        b = world.getBlock(x, y, z);
        if (b != Block.grass && b != Block.dirt) {
            return false;
        }

        for (int j = 0; j < 4; j++) {
            b = world.getBlock(j == 0 ? x - 1 : j == 1 ? x + 1 : x, y, j == 2 ? z - 1 : j == 3 ? z + 1 : z);
            if (b == null || b.blockMaterial != Material.dirt && b.blockMaterial != Material.grass) {
                return false;
            }
        }

        int rx, ry, rz;
        for (int i = 0; i < 10; i++) {
            rx = rand.nextInt(5) - 2;
            //ry = rand.nextInt(2) - 1;
            ry = 0;
            rz = rand.nextInt(5) - 2;
            b = world.getBlock(x + rx, y + ry, z + rz);

            if ((b == Block.grass || b == Block.dirt) && world.isAirBlock(x + rx, y + ry + 1, z + rz)) {
                world.setBlock(x + rx, y + ry, z + rz, Block.tilledField.blockID, rand.nextInt(4) + 4, 0);
                world.setBlock(x + rx, y + ry + 1, z + rz, farmtype.blockID, rand.nextInt(4) + 4, 0);
            }
        }

        world.setBlock(x, y, z, Block.waterStill.blockID);
        return true;
    }
}
