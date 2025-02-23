package rwg.deco;

import net.minecraft.Block;
import net.minecraft.Material;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class DecoLog extends WorldGenerator {

    private int logMeta;
    private int leavesMeta;
    private Block logBlock;
    private Block leavesBlock;
    private int logLength;

    public DecoLog(int meta, int length, boolean leaves) {
        logBlock = meta > 2 ? Block.wood : Block.wood;
        leavesBlock = meta > 2 ? Block.leaves : Block.leaves;
        meta = meta > 2 ? meta - 2 : meta;

        logMeta = meta;
        leavesMeta = leaves ? meta : -1;
        logLength = Math.max(length, 2);
    }

    public boolean generate(World world, Random rand, int x, int y, int z) {
        Block g = world.getBlock(x, y - 1, z);
        if (g == null || g.blockMaterial != Material.dirt && g.blockMaterial != Material.grass && g.blockMaterial != Material.sand && g.blockMaterial != Material.stone) {
            return false;
        }

        int dir = rand.nextInt(2);
        int dirMeta = 4 + (dir * 4) + logMeta;
        boolean leaves = leavesMeta > -1;

        int i;
        Block b;
        int air = 0;
        for (i = 0; i < logLength; i++) {
            b = world.getBlock(x - (dir == 0 ? 1 : 0), y, z - (dir == 1 ? 1 : 0));
            if (b != null && b.blockMaterial != Material.vine
                    && b.blockMaterial != Material.plants) {
                break;
            }

            x -= dir == 0 ? 1 : 0;
            z -= dir == 1 ? 1 : 0;

            if (airCheck(world, rand, x, y, z) > 0) {
                return false;
            }
        }

        for (i = 0; i < logLength * 2; i++) {
            b = world.getBlock(x + (dir == 0 ? 1 : 0), y, z + (dir == 1 ? 1 : 0));
            if (b != null && b.blockMaterial != Material.vine
                    && b.blockMaterial != Material.plants) {
                break;
            }

            air += airCheck(world, rand, x, y, z);
            if (air > 2) {
                return false;
            }

            world.setBlock(x, y, z, logBlock.blockID, dirMeta, 0);

            if (leavesMeta > -1) {
                addLeaves(world, rand, dir, x, y, z);
            }

            x += dir == 0 ? 1 : 0;
            z += dir == 1 ? 1 : 0;
        }

        return true;
    }

    private int airCheck(World world, Random rand, int x, int y, int z) {
        Block b = world.getBlock(x, y - 1, z);
        if (b == null || b.blockMaterial == Material.vine
                || b.blockMaterial == Material.water
                || b.blockMaterial == Material.plants) {
            b = world.getBlock(x, y - 2, z);
            if (b == null || b.blockMaterial == Material.vine
                    || b.blockMaterial == Material.water
                    || b.blockMaterial == Material.plants) {
                return 99;
            }
            return 1;
        }

        return 0;
    }

    private void addLeaves(World world, Random rand, int dir, int x, int y, int z) {
        Block b;
        if (dir == 0) {
            b = world.getBlock(x, y, z - 1);
            if ((b == null || b.blockMaterial == Material.vine
                    || b.blockMaterial == Material.plants) && rand.nextInt(3) == 0) {
                world.setBlock(x, y, z - 1, leavesBlock.blockID, leavesMeta, 0);
            }
            b = world.getBlock(x, y, z + 1);
            if ((b == null || b.blockMaterial == Material.vine
                    || b.blockMaterial == Material.plants) && rand.nextInt(3) == 0) {
                world.setBlock(x, y, z + 1, leavesBlock.blockID, leavesMeta, 0);
            }
        } else {
            b = world.getBlock(x - 1, y, z);
            if ((b == null || b.blockMaterial == Material.vine
                    || b.blockMaterial == Material.plants) && rand.nextInt(3) == 0) {
                world.setBlock(x - 1, y, z, leavesBlock.blockID, leavesMeta, 0);
            }
            b = world.getBlock(x + 1, y, z);
            if ((b == null || b.blockMaterial == Material.vine
                    || b.blockMaterial == Material.plants) && rand.nextInt(3) == 0) {
                world.setBlock(x + 1, y, z, leavesBlock.blockID, leavesMeta, 0);
            }
        }

        b = world.getBlock(x, y + 1, z);
        if ((b == null || b.blockMaterial == Material.vine || b.blockMaterial == Material.plants)
                && rand.nextInt(3) == 0) {
            world.setBlock(x, y + 1, z, leavesBlock.blockID, leavesMeta, 0);
        }
    }
}
