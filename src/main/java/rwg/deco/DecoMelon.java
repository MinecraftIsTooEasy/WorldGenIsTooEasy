//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package rwg.deco;

import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoMelon extends WorldGenerator {
    public boolean generate(World world, Random random, int x, int y, int z) {
        for (int a = 0; a < 32; ++a) {
            int i = x + random.nextInt(8) - random.nextInt(8);
            int j = y + random.nextInt(4) - random.nextInt(4);
            int k = z + random.nextInt(8) - random.nextInt(8);
            int metadata = random.nextInt(4);
            if (world.isAirBlock(i, j, k) && world.getBlockId(i, j - 1, k) == Block.grass.blockID && Block.melon.canOccurAt(world, i, j, k, metadata)) {
                world.setBlock(i, j, k, Block.melon.blockID, metadata, 2);
            }
        }
        return true;
    }
}