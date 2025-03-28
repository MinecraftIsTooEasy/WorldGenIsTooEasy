package rwg.terrain;

import rwg.util.CellNoise;
import rwg.util.NoiseGenerator;

public class TerrainMesa extends TerrainBase {

    public TerrainMesa() {
    }

    @Override
    public float generateNoise(NoiseGenerator perlin, CellNoise cell, int x, int y, float ocean, float border,
                               float river) {
        float b = perlin.noise2(x / 130f, y / 130f) * 50f * river;
        b *= b / 40f;

        float hn = perlin.noise2(x / 12f, y / 12f);

        float sb = 0f;
        if (b > 2f) {
            sb = (b - 2f) / 2f;
            sb = sb < 0f ? 0f : sb > 5.5f ? 5.5f : sb;
            sb = hn * sb;
        }
        b += sb;

        b = b < 0.1f ? 0.1f : b;

        float c1 = 0f;
        if (b > 1f) {
            c1 = b > 5.5f ? 4.5f : b - 1f;
            c1 *= 3;
        }

        float c2 = 0f;
        if (b > 5.5f && border > 0.95f + hn * 0.09f) {
            c2 = b > 6f ? 0.5f : b - 5.5f;
            c2 *= 35;
        }

        float bn = 0f;
        if (b < 7f) {
            float bnh = 5f - b;
            bn += perlin.noise2(x / 70f, y / 70f) * (bnh * 0.4f);
            bn += perlin.noise2(x / 20f, y / 20f) * (bnh * 0.3f);
        }

        float w = perlin.noise2(x / 80f, y / 80f) * 25f;
        w *= w / 25f;

        b += c1 + c2 + bn - w;

        return 74f + b;
    }
}
