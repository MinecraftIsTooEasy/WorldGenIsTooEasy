package rwg.biomes;

import rwg.biomes.coast.*;
import rwg.biomes.desert.RealisticBiomeDesert;
import rwg.biomes.desert.RealisticBiomeDesertMountains;
import rwg.biomes.desert.RealisticBiomeDuneValley;
import rwg.biomes.desert.RealisticBiomeOasis;
import rwg.biomes.forest.RealisticBiomeDarkRedwood;
import rwg.biomes.forest.RealisticBiomeDarkRedwoodPlains;
import rwg.biomes.forest.RealisticBiomeWoodHills;
import rwg.biomes.forest.RealisticBiomeWoodMountains;
import rwg.biomes.land.*;
import rwg.biomes.ocean.RealisticBiomeIslandTropical;
import rwg.biomes.ocean.RealisticBiomeOceanTest;
import rwg.biomes.red.RealisticBiomeCanyon;
import rwg.biomes.red.RealisticBiomeMesa;
import rwg.biomes.red.RealisticBiomeRedDesertMountains;
import rwg.biomes.red.RealisticBiomeRedOasis;
import rwg.biomes.savanna.*;

import java.util.HashSet;
import java.util.Set;

public class RWGBiomes {
    public static RealisticBiomeBase test = new RealisticBiomeTest();
    public static RealisticBiomeBase river = new RealisticBiomeTestRiver();
    public static RealisticBiomeBase ocean = new RealisticBiomeOceanTest();
    public static RealisticBiomeBase coast = new RealisticBiomeCoastTest();
    // POLAR =========================================================================================
    public static RealisticBiomeBase polar = new RealisticBiomePolar();
    // SNOW ==========================================================================================
    public static RealisticBiomeBase snowHills = new RealisticBiomeSnowHills();
    public static RealisticBiomeBase snowRivers = new RealisticBiomeSnowRivers();
    public static RealisticBiomeBase snowLakes = new RealisticBiomeSnowLakes();
    public static RealisticBiomeBase redwoodSnow = new RealisticBiomeRedwoodSnow();
    // PINE ===========================================================================================
    public static RealisticBiomeBase tundraHills = new RealisticBiomeTundraHills();
    public static RealisticBiomeBase tundraPlains = new RealisticBiomeTundraPlains();
    public static RealisticBiomeBase taigaHills = new RealisticBiomeTaigaHills();
    public static RealisticBiomeBase taigaPlains = new RealisticBiomeTaigaPlains();
    // FOREST =========================================================================================
    public static RealisticBiomeBase redwood = new RealisticBiomeRedwood();
    public static RealisticBiomeBase darkRedwood = new RealisticBiomeDarkRedwood();
    public static RealisticBiomeBase darkRedwoodPlains = new RealisticBiomeDarkRedwoodPlains();
    public static RealisticBiomeBase woodhills = new RealisticBiomeWoodHills();
    public static RealisticBiomeBase woodmountains = new RealisticBiomeWoodMountains();
    // SAVANNA ========================================================================================
    public static RealisticBiomeBase duneValleyForest = new RealisticBiomeDuneValleyForest();
    public static RealisticBiomeBase savanna = new RealisticBiomeSavanna();
    public static RealisticBiomeBase savannaForest = new RealisticBiomeSavannaForest();
    public static RealisticBiomeBase savannaDunes = new RealisticBiomeSavannaDunes();
    public static RealisticBiomeBase stoneMountains = new RealisticBiomeStoneMountains();
    public static RealisticBiomeBase stoneMountainsCactus = new RealisticBiomeStoneMountainsCactus();
    public static RealisticBiomeBase hotForest = new RealisticBiomeHotForest();
    public static RealisticBiomeBase hotRedwood = new RealisticBiomeHotRedwood();
    public static RealisticBiomeBase canyonForest = new RealisticBiomeCanyonForest();
    public static RealisticBiomeBase mesaPlains = new RealisticBiomeMesaPlains();
    // DESERT =========================================================================================
    public static RealisticBiomeBase desert = new RealisticBiomeDesert();
    public static RealisticBiomeBase desertMountains = new RealisticBiomeDesertMountains();
    public static RealisticBiomeBase duneValley = new RealisticBiomeDuneValley();
    public static RealisticBiomeBase oasis = new RealisticBiomeOasis();
    // RED ============================================================================================
    public static RealisticBiomeBase redDesertMountains = new RealisticBiomeRedDesertMountains();
    public static RealisticBiomeBase redDesertOasis = new RealisticBiomeRedOasis();
    public static RealisticBiomeBase canyon = new RealisticBiomeCanyon();
    public static RealisticBiomeBase mesa = new RealisticBiomeMesa();
    // JUNGLE =========================================================================================
    public static RealisticBiomeBase rainForestHigh = new RealisticBiomeHighRainforest();
    // TROPICAL =======================================================================================
    public static RealisticBiomeBase jungleHills = new RealisticBiomeJungleHills();
    public static RealisticBiomeBase jungleCliff = new RealisticBiomeJungleCliff();
    public static RealisticBiomeBase redwoodJungle = new RealisticBiomeRedwoodJungle();
    // COAST =========================================================================================
    public static RealisticBiomeBase coastIce = new RealisticBiomeCoastIce();
    public static RealisticBiomeBase coastColdSlope = new RealisticBiomeCoastColdSlope();
    public static RealisticBiomeBase coastColdCliff = new RealisticBiomeCoastColdCliff();
    public static RealisticBiomeBase coastDunes = new RealisticBiomeCoastDunes();
    public static RealisticBiomeBase coastMangrove = new RealisticBiomeCoastMangrove();
    public static RealisticBiomeBase coastOasis = new RealisticBiomeCoastOasis();
    // OCEAN =========================================================================================
    public static RealisticBiomeBase islandTropical = new RealisticBiomeIslandTropical();

    // SWAMP ==========================================================================================

    public static final Set<RealisticBiomeBase> desertBiomes = new HashSet<>();
    public static final Set<RealisticBiomeBase> forestBiomes = new HashSet<>();
    public static final Set<RealisticBiomeBase> savannaBiomes = new HashSet<>();
    public static final Set<RealisticBiomeBase> jungleBiomes = new HashSet<>();
    public static final Set<RealisticBiomeBase> snowBiomes = new HashSet<>();
    public static final Set<RealisticBiomeBase> mountainsBiomes = new HashSet<>();
    public static final Set<RealisticBiomeBase> plainsBiomes = new HashSet<>();
    public static final Set<RealisticBiomeBase> riverBiomes = new HashSet<>();
    public static final Set<RealisticBiomeBase> oceanBiomes = new HashSet<>();

    static {
        mountainsBiomes.add(snowHills);
        mountainsBiomes.add(taigaHills);
        mountainsBiomes.add(jungleHills);
        mountainsBiomes.add(tundraHills);
        mountainsBiomes.add(woodhills);
        mountainsBiomes.add(desertMountains);
        mountainsBiomes.add(stoneMountains);
        mountainsBiomes.add(redDesertMountains);
        mountainsBiomes.add(stoneMountainsCactus);
        mountainsBiomes.add(woodmountains);
    }
}
