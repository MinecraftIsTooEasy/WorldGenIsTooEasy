package rwg.util;

public class NoiseSelector {

    public static NoiseGenerator createNoiseGenerator(long seed) {

        NoiseImplementation noiseImplementation = NoiseImplementation.UNKNOWN;

        switch (noiseImplementation) {
            case UNKNOWN:
            case DYNAMICPERLIN:
                NoiseGeneratorWrapper.useOpenSimplex = false;
                return new NoiseGeneratorWrapper(seed);
            case PERLIN:
                NoiseGeneratorWrapper.useOpenSimplex = false;
                return new PerlinNoise(seed);
            case OPENSIMPLEX:
                NoiseGeneratorWrapper.useOpenSimplex = true;
                return new OpenSimplexNoise(seed);
            case DYNAMICOPENSIMPLEX:
                NoiseGeneratorWrapper.useOpenSimplex = true;
                return new NoiseGeneratorWrapper(seed);
        }

        return new NoiseGeneratorWrapper(seed);
    }
}
