package rwg.deco;

import net.minecraft.Block;
import net.minecraft.EnumFace;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoCacti extends WorldGenerator {

    private boolean sand;
    private int eHeight;

    public DecoCacti(boolean sandOnly) {
        this(sandOnly, 0);
    }

    public DecoCacti(boolean sandOnly, int extraHeight) {
        sand = sandOnly;
        eHeight = extraHeight;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        Block b;
        for (int l = 0; l < 10; ++l) {
            int i1 = x + rand.nextInt(8) - rand.nextInt(8);
            int j1 = y + rand.nextInt(4) - rand.nextInt(4);
            int k1 = z + rand.nextInt(8) - rand.nextInt(8);

            if (world.isAirBlock(i1, j1, k1) && !world.getNeighborBlockMaterial(i1, j1, k1, EnumFace.WEST).isSolid() && !world.getNeighborBlockMaterial(i1, j1, k1, EnumFace.EAST).isSolid() && !world.getNeighborBlockMaterial(i1, j1, k1, EnumFace.NORTH).isSolid() && !world.getNeighborBlockMaterial(i1, j1, k1, EnumFace.SOUTH).isSolid()) {
                b = world.getBlock(i1, j1 - 1, k1);
                if (b == Block.sand || b == Block.gravel || (!sand && (b == Block.grass || b == Block.dirt))) {
                    int l1 = 1 + rand.nextInt(rand.nextInt(3) + 1);
                    if (b == Block.grass || b == Block.dirt || b == Block.gravel) {
                        world.setBlock(i1, j1 - 1, k1, Block.sand.blockID, 0, 2);
                    }

                    for (int i2 = 0; i2 < l1 + eHeight; ++i2) {
                        if (Block.cactus.isLegalAt(world, i1, j1 + i2, k1, 0)) {
                            world.setBlock(i1, j1 + i2, k1, Block.cactus.blockID, 0, 2);
                        }
                    }
                }
            }
        }

        return true;
    }
}
