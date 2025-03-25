package rwg.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSand.class)
public abstract class BlockSandMixin extends BlockFalling {
    @Unique
    private BlockSubtypes subtypes;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(int id, CallbackInfo ci) {
        this.subtypes = new BlockSubtypes(new String[]{"sand", "redsand"});
    }

    public BlockSandMixin(int par1, Material material, BlockConstants constants) {
        super(par1, material, constants);
    }

    public boolean isValidMetadata(int metadata) {
        return metadata >= 0 && metadata < 2;
    }

    public int getBlockSubtypeUnchecked(int metadata) {
        return metadata;
    }

    public void registerIcons(IconRegister icon) {
        this.subtypes.setIcons(this.registerIcons(icon, this.subtypes.getTextures()));
    }

    public Icon getIcon(int side, int metadata) {
        return this.subtypes.getIcon(this.getBlockSubtype(metadata));
    }
}
