package rwg.deco;

import net.minecraft.Block;
import net.minecraft.BlockLeaves;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoGrass extends WorldGenerator {

    private Block block;
    private int metadata;

    public DecoGrass(Block b, int m) {
        block = b;
        metadata = m;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        while (y > 0) {
            if (!world.isAirBlock(x, y, z) || world.getBlock(x, y, z) instanceof BlockLeaves) {
                break;
            }
            y--;
        }

        if (block == Block.plantRed) {
            for (int l = 0; l < 48; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(i1, j1, k1) && j1 < 254
                        && Block.tallGrass.isLegalAt(world, i1, j1, k1, 0)) {
                    world.setBlock(i1, j1, k1, Block.tallGrass.blockID, 1, 0);
                }
            }
        } else if (block == Block.leaves) {
            for (int l = 0; l < 48; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(i1, j1, k1) && world.getBlock(i1, j1 - 1, k1) == Block.grass) {
                    world.setBlock(i1, j1, k1, block.blockID, metadata, 0);
                }
            }
        } else {
            for (int l = 0; l < 96; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(i1, j1, k1) && block.isLegalAt(world, i1, j1, k1, 0)) {
                    world.setBlock(i1, j1, k1, block.blockID, metadata, 0);
                }
            }
        }
        return true;
    }
}
