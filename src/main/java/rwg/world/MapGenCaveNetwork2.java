package rwg.world;

import net.minecraft.*;

import java.util.HashMap;
import java.util.Random;

public class MapGenCaveNetwork2 extends MapGenBase2 {
    private CaveNetworkStub stub;
    protected int a = 3;
    public final HashMap cached_stubs = new HashMap();
    public final HashMap cached_cave_network_generators = new HashMap();

    @Override
    public void generate(IChunkProvider chunk_provider, World world, int chunk_x, int chunk_z, Block[] block_ids) {
        CaveNetworkStub stub = getCaveNetworkStubAt(world, chunk_x, chunk_z);
        if (stub == null) {
            return;
        }
        generateCaveNetwork(chunk_x, chunk_z, block_ids, stub);
    }

    private void generateCaveNetwork(int chunk_x, int chunk_z, Block[] block_ids, CaveNetworkStub stub) {
        int hash = stub.getOriginChunkCoordsHash();
        CaveNetworkGenerator2 cg = (CaveNetworkGenerator2) this.cached_cave_network_generators.get(hash);
        if (cg == null || cg.getOriginChunkX() != stub.getOriginChunkX() || cg.getOriginChunkZ() != stub.getOriginChunkZ()) {
            if (cg != null) {
                Debug.setErrorMessage("generateCaveNetwork: hash collision");
            }
            HashMap hashMap = this.cached_cave_network_generators;
            CaveNetworkGenerator2 CaveNetworkGenerator2 = new CaveNetworkGenerator2(stub);
            cg = CaveNetworkGenerator2;
            hashMap.put(hash, CaveNetworkGenerator2);
        }
        cg.apply(chunk_x, chunk_z, stub.getOriginChunkX(), stub.getOriginChunkZ(), block_ids);
    }

    private CaveNetworkStub getOrCreateCaveNetworkStub(World world, int origin_chunk_x, int origin_chunk_z, double distance_from_world_origin) {
        int hash = Chunk.getChunkCoordsHash(origin_chunk_x, origin_chunk_z);
        CaveNetworkStub stub = (CaveNetworkStub) this.cached_stubs.get(hash);
        if (stub == null || stub.getOriginChunkX() != origin_chunk_x || stub.getOriginChunkZ() != origin_chunk_z) {
            if (stub != null) {
                Debug.setErrorMessage("getOrCreateCaveNetworkStub: hash collision");
            }
            //boolean has_mycelium = distance_from_world_origin >= 1500.0d && this.rand.nextInt(8) == 0;
            boolean has_mycelium = this.rand.nextInt(4) == 0;
            long seed = this.rand.nextLong();
            if (world.getSeed() == 1 && origin_chunk_x == -14 && origin_chunk_z == 29) {
                seed = 2617667064333438329L;
            }
            HashMap hashMap = this.cached_stubs;
            CaveNetworkStub caveNetworkStub = new CaveNetworkStub(origin_chunk_x, origin_chunk_z, 64, 48, 64, seed, has_mycelium, this.rand.nextInt(3) > 0, this.rand.nextInt(3) > 0);
            stub = caveNetworkStub;
            hashMap.put(hash, caveNetworkStub);
        }
        return stub;
    }

    public boolean isOriginOfCaveNetwork(World world, int chunk_x, int chunk_z) {
        if (!isGenAllowedInChunk(world, chunk_x, chunk_z)) {
            return false;
        }
        this.worldObj = world;
        this.rand.setSeed(world.getSeed());
        long seed_a = this.rand.nextLong();
        long seed_b = this.rand.nextLong();
        long seed_c = chunk_x * seed_a;
        long seed_d = chunk_z * seed_b;
        long chunk_seed = (seed_c ^ seed_d) ^ world.getSeed();
        this.rand.setSeed(chunk_seed);
        double distance_from_world_origin = world.getDistanceFromWorldOrigin(chunk_x * 16, chunk_z * 16);
        if (world.getSeed() == 1 && chunk_x == -14 && chunk_z == 29) {
            this.stub = getOrCreateCaveNetworkStub(world, chunk_x, chunk_z, distance_from_world_origin);
            return true;
        }
        if (distance_from_world_origin >= 1000.0d && this.rand.nextInt(200) == 0) {
            Random rand = this.rand;
            this.rand = new Random();
            for (int origin_chunk_x = chunk_x + 1; origin_chunk_x <= chunk_x + this.a; origin_chunk_x++) {
                for (int origin_chunk_z = chunk_z + 1; origin_chunk_z <= chunk_z + this.a; origin_chunk_z++) {
                    if (!isGenAllowedInChunk(world, origin_chunk_x, origin_chunk_z) || isOriginOfCaveNetwork(world, origin_chunk_x, origin_chunk_z)) {
                        return false;
                    }
                }
            }
            this.rand = rand;
            this.stub = getOrCreateCaveNetworkStub(world, chunk_x, chunk_z, distance_from_world_origin);
            return true;
        }
        return false;
    }

    public CaveNetworkStub getCaveNetworkStubAt(World world, int chunk_x, int chunk_z) {
        if (!isGenAllowedInChunk(world, chunk_x, chunk_z)) {
            return null;
        }
        for (int origin_chunk_x = chunk_x - this.a; origin_chunk_x <= chunk_x; origin_chunk_x++) {
            for (int origin_chunk_z = chunk_z - this.a; origin_chunk_z <= chunk_z; origin_chunk_z++) {
                if (isOriginOfCaveNetwork(world, origin_chunk_x, origin_chunk_z)) {
                    return this.stub;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isGenAllowedInBiome(BiomeGenBase biome) {
        return biome != BiomeGenBase.ocean;
    }
}