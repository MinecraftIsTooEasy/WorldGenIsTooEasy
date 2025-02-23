package rwg.deco;

import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoFlowers extends WorldGenerator {

    private final int yellowWeight;
    private final int otherWeight;
    private final int otherMeta;

    public DecoFlowers(int yellowWeight, int otherWeight, int otherMeta) {
        this.yellowWeight = yellowWeight;
        this.otherWeight = otherWeight;
        this.otherMeta = otherMeta;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        int randomFlower = rand.nextInt(yellowWeight + otherWeight);

        if (randomFlower < yellowWeight) {
            for (int l = 0; l < 48; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(i1, j1, k1) && (!world.provider.hasNoSky || j1 < 255)
                        && Block.plantYellow.isLegalAt(world, i1, j1, k1, 0)) {
                    world.setBlock(i1, j1, k1, Block.plantYellow.blockID, 0, 0);
                }
            }
        } else {
            for (int l = 0; l < 48; ++l) {
                int i1 = x + rand.nextInt(8) - rand.nextInt(8);
                int j1 = y + rand.nextInt(4) - rand.nextInt(4);
                int k1 = z + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(i1, j1, k1) && (!world.provider.hasNoSky || j1 < 255)
                        && Block.plantRed.isLegalAt(world, i1, j1, k1, 0)) {
                    world.setBlock(i1, j1, k1, Block.plantRed.blockID, otherMeta, 0);
                }
            }
        }

        return true;
    }
}
