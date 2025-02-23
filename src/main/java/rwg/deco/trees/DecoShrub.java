package rwg.deco.trees;

import net.minecraft.Block;
import net.minecraft.Material;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoShrub extends WorldGenerator {

    private int size;
    private Block logBlock;
    private int logMeta;
    private Block leaveBlock;
    private int leaveMeta;
    private boolean sand;

    public DecoShrub(int s, int log, int leav) {
        this(s, log, leav, false);
    }

    public DecoShrub(int s, int log, int leav, boolean sa) {
        size = s;
        sand = sa;

        logBlock = log < 3 ? Block.wood : Block.wood;
        logMeta = log > 2 ? log - 3 : log;

        leaveBlock = leav < 4 ? Block.leaves : Block.leaves;
        leaveMeta = leav > 3 ? leav - 4 : leav;
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        int width = Math.min(size, 6);
        int height = size > 3 ? 2 : 1;

        for (int i = 0; i < size; i++) {
            int rX = rand.nextInt(width * 2) - width;
            int rY = rand.nextInt(height);
            int rZ = rand.nextInt(width * 2) - width;

            if (i == 0 && size > 4) {
                buildLeaves(world, x + rX, y, z + rZ, 3);
            } else if (i == 1 && size > 2) {
                buildLeaves(world, x + rX, y, z + rZ, 2);
            } else {
                buildLeaves(world, x + rX, y + rY, z + rZ, 1);
            }
        }
        return true;
    }

    public void buildLeaves(World world, int x, int y, int z, int size) {
        Block b = world.getBlock(x, y - 2, z);
        if (b != null && (b.blockMaterial == Material.grass || b.blockMaterial == Material.dirt
                || (sand && b.blockMaterial == Material.sand))) {
            if (world.getBlock(x, y - 1, z) != Block.waterStill) {
                for (int i = -size; i <= size; i++) {
                    for (int j = -1; j <= 1; j++) {
                        for (int k = -size; k <= size; k++) {
                            if (Math.abs(i) + Math.abs(j) + Math.abs(k) <= size) {
                                buildBlock(world, x + i, y + j, z + k, leaveBlock, leaveMeta);
                            }
                        }
                    }
                }
                world.setBlock(x, y - 1, z, logBlock.blockID, logMeta, 0);
            }
        }
    }

    public void buildBlock(World world, int x, int y, int z, Block block, int meta) {
        Block b = world.getBlock(x, y, z);
        if (b == null || b.blockMaterial == Material.vine
                || b.blockMaterial == Material.plants
                || b == Block.snow) {
            world.setBlock(x, y, z, block.blockID, meta, 0);
        }
    }
}
