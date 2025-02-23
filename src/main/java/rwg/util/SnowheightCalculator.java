package rwg.util;

import net.minecraft.Block;

public class SnowheightCalculator {

    public static void calc(int x, int y, int k, Block[] blocks, byte[] metadata, float[] noise) {
        if (k < 254) {
            byte h = (byte) ((noise[y * 16 + x] - ((int) noise[y * 16 + x])) * 8);

            int i = (y * 16 + x) * 256 + k;
            if (h > 7) {
                blocks[i + 2] = Block.snow;
                blocks[i + 1] = Block.snow;
                metadata[i + 1] = 7;
            } else {
                blocks[i + 1] = Block.snow;
                metadata[i + 1] = h;
            }
        }
    }
}
