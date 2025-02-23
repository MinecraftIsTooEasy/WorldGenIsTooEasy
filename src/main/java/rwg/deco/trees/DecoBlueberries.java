package rwg.deco.trees;

import net.minecraft.Block;
import net.minecraft.BlockBush;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoBlueberries extends WorldGenerator {
    public boolean generate(World world, Random rand, int x, int y, int z) {
        for (int l = 0; l < 48; ++l) {
            int i1 = x + rand.nextInt(8) - rand.nextInt(8);
            int j1 = y + rand.nextInt(4) - rand.nextInt(4);
            int k1 = z + rand.nextInt(8) - rand.nextInt(8);

            if (world.isAirBlock(i1, j1, k1) && (!world.provider.hasNoSky || j1 < 255)
                    && Block.bush.isLegalAt(world, i1, j1, k1, 0)) {
                world.setBlock(i1, j1, k1, Block.bush.blockID, BlockBush.getMetadataForBushWithBerries(0), 0);
            }
        }
        return true;
    }
}
