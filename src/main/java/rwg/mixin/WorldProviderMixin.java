package rwg.mixin;

import net.minecraft.IChunkProvider;
import net.minecraft.World;
import net.minecraft.WorldChunkManager;
import net.minecraft.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rwg.world.ChunkGeneratorRealistic;
import rwg.world.ChunkManagerRealistic;

@Mixin(WorldProvider.class)
public class WorldProviderMixin {
    @Shadow
    public World worldObj;

    @Shadow
    public WorldChunkManager worldChunkMgr;

    @Inject(method = "createChunkGenerator", at = @At("HEAD"), cancellable = true)
    private void createChunkGenerator(CallbackInfoReturnable<IChunkProvider> cir) {
        cir.setReturnValue(new ChunkGeneratorRealistic(worldObj, worldObj.getSeed()));
    }

    @Inject(method = "registerWorldChunkManager", at = @At("TAIL"))
    private void registerWorldChunkManager(CallbackInfo ci) {
        this.worldChunkMgr = new ChunkManagerRealistic(worldObj);
    }
}
