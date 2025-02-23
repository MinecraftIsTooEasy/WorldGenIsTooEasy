package rwg.surface;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.World;
import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

import java.util.Random;

public class SurfaceBase {

    protected Block topBlock;
    protected Block fillerBlock;

    public SurfaceBase(Block top, Block fill) {
        topBlock = top;
        fillerBlock = fill;
    }

    public void paintTerrain(Block[] blocks, byte[] metadata, int i, int j, int x, int y, int depth, World world,
                             Random rand, NoiseGenerator perlin, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {
    }
}
