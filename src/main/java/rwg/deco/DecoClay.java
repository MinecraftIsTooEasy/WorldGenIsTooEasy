package rwg.deco;

import net.minecraft.Block;
import net.minecraft.MathHelper;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoClay extends WorldGenerator {

    private final Block block;
    private final int numberOfBlocks;
    private final int mineableBlockMeta;

    public DecoClay(Block block, int metadata, int amount) {
        this.block = block;
        mineableBlockMeta = metadata;
        numberOfBlocks = amount;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = (x + 8) + MathHelper.sin(f) * this.numberOfBlocks / 8.0D;
        double d1 = (x + 8) - MathHelper.sin(f) * this.numberOfBlocks / 8.0D;
        double d2 = (z + 8) + MathHelper.cos(f) * this.numberOfBlocks / 8.0D;
        double d3 = (z + 8) - MathHelper.cos(f) * this.numberOfBlocks / 8.0D;
        double d4 = y + rand.nextInt(3) - 2;
        double d5 = y + rand.nextInt(3) - 2;
        Block b;

        for (int l = 0; l <= this.numberOfBlocks; ++l) {
            double d6 = d0 + (d1 - d0) * (double) l / (double) this.numberOfBlocks;
            double d7 = d4 + (d5 - d4) * (double) l / (double) this.numberOfBlocks;
            double d8 = d2 + (d3 - d2) * (double) l / (double) this.numberOfBlocks;
            double d9 = rand.nextDouble() * (double) this.numberOfBlocks / 16.0D;
            double d10 = (double) (MathHelper.sin((float) l * (float) Math.PI / (float) this.numberOfBlocks) + 1.0F)
                    * d9 + 1.0D;
            double d11 = (double) (MathHelper.sin((float) l * (float) Math.PI / (float) this.numberOfBlocks) + 1.0F)
                    * d9 + 1.0D;
            int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
            int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
            int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
            int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
            int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
            int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

            for (int k2 = i1; k2 <= l1; ++k2) {
                double d12 = ((double) k2 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D) {
                    for (int l2 = j1; l2 <= i2; ++l2) {
                        double d13 = ((double) l2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D) {
                            for (int i3 = k1; i3 <= j2; ++i3) {
                                double d14 = ((double) i3 + 0.5D - d8) / (d10 / 2.0D);
                                b = world.getBlock(k2, l2, i3);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && (b == Block.dirt
                                        || b == Block.sand
                                        || b == Block.gravel)) {
                                    world.setBlock(k2, l2, i3, this.block.blockID, mineableBlockMeta, 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
