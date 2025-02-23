package rwg.deco.trees;

import net.minecraft.Block;
import net.minecraft.BlockLeaves;
import net.minecraft.World;
import net.minecraft.WorldGenTrees;

import java.util.Random;

public class DecoPineTree extends WorldGenTrees {

    private int height;
    private int metadata;

    public DecoPineTree(int h, int m) {
        super(false);

        height = h;
        metadata = m;
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        int l = rand.nextInt(height * 2) + height * 2;
        int i1 = height + rand.nextInt(height);
        int j1 = l - i1;
        int k1 = 2 + rand.nextInt(2);
        boolean flag = true;

        if (y >= 1 && y + l + 1 <= 256) {
            int i2;
            int l3;

            for (int l1 = y; l1 <= y + 1 + l && flag; ++l1) {
                boolean flag1 = true;

                if (l1 - y < i1) {
                    l3 = 0;
                } else {
                    l3 = k1;
                }

                for (i2 = x - l3; i2 <= x + l3 && flag; ++i2) {
                    for (int j2 = z - l3; j2 <= z + l3 && flag; ++j2) {
                        if (l1 >= 0 && l1 < 256) {
                            Block block = world.getBlock(i2, l1, j2);

                            if (block != null && !(block instanceof BlockLeaves) && block != Block.snow) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                Block block1 = world.getBlock(x, y - 1, z);

                boolean isSoil = Block.sapling.isLegalOn(0, block1, 0);

                if (isSoil && y < 256 - l - 1) {
                    world.setBlock(x, y - 1, z, Block.dirt.blockID, 0, 2);
                    l3 = rand.nextInt(2);
                    i2 = 1;
                    byte b0 = 0;
                    int k2;
                    int i4;

                    for (i4 = 0; i4 <= j1; ++i4) {
                        k2 = y + l - i4;

                        for (int l2 = x - l3; l2 <= x + l3; ++l2) {
                            int i3 = l2 - x;

                            for (int j3 = z - l3; j3 <= z + l3; ++j3) {
                                int k3 = j3 - z;
                                Block block = world.getBlock(l2, k2, j3);
                                if ((Math.abs(i3) != l3 || Math.abs(k3) != l3 || l3 <= 0) && (block == null || !(block.is_always_opaque_standard_form_cube || block.is_always_solid_standard_form_cube))) {
                                    world.setBlock(l2, k2, j3, Block.leaves.blockID, metadata, 0);
                                }
                            }
                        }

                        if (l3 >= i2) {
                            l3 = b0;
                            b0 = 1;
                            ++i2;

                            if (i2 > k1) {
                                i2 = k1;
                            }
                        } else {
                            ++l3;
                        }
                    }

                    i4 = rand.nextInt(3);

                    for (k2 = 0; k2 < l - i4; ++k2) {
                        Block block2 = world.getBlock(x, y + k2, z);

                        if (block2 == null
                                || block2 instanceof BlockLeaves
                                || block2 == Block.snow) {
                            world.setBlock(x, y + k2, z, Block.wood.blockID, metadata, 0);
                        }
                    }

                    if (height > 4) {
                        createTrunk(world, rand, x, y, z);
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private void createTrunk(World world, Random rand, int x, int y, int z) {
        int[] pos = new int[]{0, 0, 1, 0, 0, 1, -1, 0, 0, -1};
        int sh;
        for (int t = 0; t < 5; t++) {
            sh = rand.nextInt(4) + y - 2;
            while (sh > y - 1) {
                if (world.getBlock(x + pos[t * 2], sh, z + pos[t * 2 + 1]) == Block.grass) {
                    break;
                }
                world.setBlock(x + pos[t * 2], sh, z + pos[t * 2 + 1], Block.wood.blockID, metadata, 0);
                sh--;
            }
        }
    }
}
