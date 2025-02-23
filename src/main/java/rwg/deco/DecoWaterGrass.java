package rwg.deco;

import net.minecraft.Block;
import net.minecraft.BlockLeaves;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoWaterGrass extends WorldGenerator {

    private Block block;
    private int metadata;
    private int minHeight;

    public DecoWaterGrass(Block b, int m) {
        this(b, m, 10);
    }

    public DecoWaterGrass(Block b, int m, int mh) {
        block = b;
        metadata = m;
        minHeight = mh;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        while (y > 0) {
            if (!world.isAirBlock(x, y, z) || world.getBlock(x, y, z) instanceof BlockLeaves) {
                break;
            }

            if (y < minHeight) {
                return false;
            }
            y--;
        }

        Block b;
        if (block == Block.plantRed) {
            int i1, j1, k1;
            for (int l = 0; l < 32; ++l) {
                i1 = x + rand.nextInt(8) - rand.nextInt(8);
                j1 = y + rand.nextInt(2) - rand.nextInt(2);
                k1 = z + rand.nextInt(8) - rand.nextInt(8);

                b = world.getBlock(i1, j1 - 1, k1);
                if (((b == Block.waterStill && world.getBlock(i1, j1 - 2, k1) == Block.sand) || b == Block.sand)
                        && world.getBlock(i1, j1, k1) == null) {
                    world.setBlock(i1, j1 - 1, k1, Block.grass.blockID, 0, 0);
                }

                if (world.isAirBlock(i1, j1, k1) && j1 < 254
                        && Block.tallGrass.isLegalAt(world, i1, j1, k1, 0)) {
                    world.setBlock(i1, j1, k1, Block.tallGrass.blockID, 1, 0);
                }
            }
        } else if (block == Block.leaves) {
            for (int l = 0; l < 64; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);

                b = world.getBlock(i1, j1 - 1, k1);
                if (((b == Block.waterStill && world.getBlock(i1, j1 - 2, k1) == Block.sand) || b == Block.sand)
                        && world.getBlock(i1, j1, k1) == null) {
                    world.setBlock(i1, j1 - 1, k1, Block.grass.blockID, 0, 0);
                }

                if (world.isAirBlock(i1, j1, k1) && world.getBlock(i1, j1 - 1, k1) == Block.grass) {
                    world.setBlock(i1, j1, k1, block.blockID, metadata, 0);
                }
            }
        } else {
            for (int l = 0; l < 128; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);

                b = world.getBlock(i1, j1 - 1, k1);
                if (((b == Block.waterStill && world.getBlock(i1, j1 - 2, k1) == Block.sand) || b == Block.sand)
                        && world.getBlock(i1, j1, k1) == null) {
                    world.setBlock(i1, j1 - 1, k1, Block.grass.blockID, 0, 0);
                }

                if (world.isAirBlock(i1, j1, k1) && block.isLegalAt(world, i1, j1, k1, 0)) {
                    world.setBlock(i1, j1, k1, block.blockID, metadata, 0);
                }
            }
        }
        return true;
    }
}
