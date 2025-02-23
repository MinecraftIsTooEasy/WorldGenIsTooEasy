package rwg.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rwg.world.ChunkGeneratorRealistic;

@Mixin(WorldServer.class)
public abstract class WorldSeverMixin extends World {
    @Shadow
    public ChunkProviderServer theChunkProviderServer;

    @Shadow
    public abstract CaveNetworkStub getCaveNetworkStubAt(int chunk_x, int chunk_z);

    public WorldSeverMixin(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler, ILogAgent par6ILogAgent, long world_creation_time, long total_world_time) {
        super(par1ISaveHandler, par2Str, par3WorldProvider, par4WorldSettings, par5Profiler, par6ILogAgent, world_creation_time, total_world_time);
    }

    @Inject(method = "getCaveNetworkStubAt", at = @At("HEAD"), cancellable = true)
    private void inject_getCaveNetworkStubAt(int chunk_x, int chunk_z, CallbackInfoReturnable<CaveNetworkStub> cir) {
        IChunkProvider provider = theChunkProviderServer.getChunkProvider();
        if (provider instanceof ChunkGeneratorRealistic)
            cir.setReturnValue(((ChunkGeneratorRealistic) provider).cave_network_generator.getCaveNetworkStubAt(this, chunk_x, chunk_z));
    }

    @Inject(method = "isMushroomCaveAt", at = @At("HEAD"), cancellable = true)
    private void isMushroomCaveAt(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        CaveNetworkStub stub = this.getCaveNetworkStubAt(x >> 4, z >> 4);
        cir.setReturnValue(stub != null && stub.hasMycelium());
    }

    @Inject(method = "isCaveNetworkAt", at = @At("HEAD"), cancellable = true)
    private void isCaveNetworkAt(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(getCaveNetworkStubAt(x >> 4, z >> 4) != null);
        ;
    }
}
