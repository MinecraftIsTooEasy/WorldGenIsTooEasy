package rwg.deco;

import net.minecraft.Block;
import net.minecraft.MathHelper;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoIceSpike extends WorldGenerator {

    public boolean generate(World world, Random rand, int x, int y, int z) {
        while (world.isAirBlock(x, y, z) && y > 2) {
            --y;
        }

        Block block = world.getBlock(x, y, z);
        if (block != Block.blockSnow && block != Block.snow) {
            return false;
        } else {
            y += rand.nextInt(4);
            int l = rand.nextInt(4) + 7;
            int i1 = l / 4 + rand.nextInt(2);

            if (i1 > 1 && rand.nextInt(60) == 0) {
                y += 10 + rand.nextInt(30);
            }

            int j1;
            int k1;
            int l1;

            for (j1 = 0; j1 < l; ++j1) {
                float f = (1.0F - (float) j1 / (float) l) * (float) i1;
                k1 = MathHelper.ceiling_float_int(f);

                for (l1 = -k1; l1 <= k1; ++l1) {
                    float f1 = (float) MathHelper.abs_int(l1) - 0.25F;

                    for (int i2 = -k1; i2 <= k1; ++i2) {
                        float f2 = (float) MathHelper.abs_int(i2) - 0.25F;

                        if ((l1 == 0 && i2 == 0 || f1 * f1 + f2 * f2 <= f * f)
                                && (l1 != -k1 && l1 != k1 && i2 != -k1 && i2 != k1 || rand.nextFloat() <= 0.75F)) {
                            block = world.getBlock(x + l1, y + j1, z + i2);

                            if (block == null || block == Block.dirt
                                    || block == Block.blockSnow
                                    || block == Block.ice) {
                                this.setBlock(world, x + l1, y + j1, z + i2, Block.ice.blockID);
                            }

                            if (j1 != 0 && k1 > 1) {
                                block = world.getBlock(x + l1, y - j1, z + i2);

                                if (block == null || block == Block.dirt
                                        || block == Block.blockSnow
                                        || block == Block.ice) {
                                    this.setBlock(world, x + l1, y - j1, z + i2, Block.ice.blockID);
                                }
                            }
                        }
                    }
                }
            }

            j1 = i1 - 1;

            if (j1 < 0) {
                j1 = 0;
            } else if (j1 > 1) {
                j1 = 1;
            }

            for (int j2 = -j1; j2 <= j1; ++j2) {
                k1 = -j1;

                while (k1 <= j1) {
                    l1 = y - 1;
                    int k2 = 50;

                    if (Math.abs(j2) == 1 && Math.abs(k1) == 1) {
                        k2 = rand.nextInt(5);
                    }

                    while (true) {
                        if (l1 > 50) {
                            block = world.getBlock(x + j2, l1, z + k1);

                            if (block == null || block == Block.dirt
                                    || block == Block.blockSnow
                                    || block == Block.ice) {
                                this.setBlock(world, x + j2, l1, z + k1, Block.ice.blockID);
                                --l1;
                                --k2;

                                if (k2 <= 0) {
                                    l1 -= rand.nextInt(5) + 1;
                                    k2 = rand.nextInt(5);
                                }

                                continue;
                            }
                        }

                        ++k1;
                        break;
                    }
                }
            }

            return true;
        }
    }
}
