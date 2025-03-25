package rwg.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiCreateWorld.class})
public abstract class GuiCreateWorldMixin extends GuiScreen {
    @Shadow
    private GuiButton buttonWorldType;

    @Shadow
    private int worldTypeId;

    @Shadow
    public String generatorOptionsToUse;

    @Shadow
    private boolean moreOptions;

    @Shadow
    private GuiButton buttonBonusItems;

    @Shadow
    private boolean bonusItems;

    @Shadow
    private boolean commandsToggled;

    @Shadow
    private boolean commandsAllowed;

    @Shadow
    protected abstract void updateButtonText();

    @Shadow
    protected abstract void func_82288_a(boolean z);

    @Inject(method = "<init>", at = {@At("TAIL")})
    private void init(CallbackInfo ci) {
        this.worldTypeId = 3;
    }

    @Inject(method = {"initGui"}, at = {@At("TAIL")})
    private void initGui(CallbackInfo ci) {
        this.buttonWorldType.enabled = true;
        this.buttonBonusItems.enabled = true;
        this.worldTypeId = 3;
    }

    @ModifyArg(method = {"initGui"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiButton;<init>(IIIIILjava/lang/String;)V"), index = 1)
    private int modifyPosX(int par1) {
        if (par1 == (this.width / 2) + 5) {
            par1 = (this.width / 2) + 2;
        }
        return par1;
    }

    @ModifyArg(method = {"initGui"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiButton;<init>(IIIIILjava/lang/String;)V"), index = 2)
    private int modifyPosY(int par1) {
        if (par1 == 120) {
            par1 = 94;
        }
        return par1;
    }

    @ModifyArg(method = {"initGui"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiButton;<init>(IIIIILjava/lang/String;)V"), index = 3)
    private int modifyWidth(int par1) {
        if (par1 == 150) {
            par1 = 152;
        }
        return par1;
    }

    @Inject(method = {"actionPerformed"}, at = {@At("HEAD")}, cancellable = true)
    private void actionPerformed(GuiButton par1GuiButton, CallbackInfo ci) {
        if (par1GuiButton.id == 5) {
            do {
                this.worldTypeId++;
                if (this.worldTypeId >= WorldType.worldTypes.length) {
                    this.worldTypeId = 0;
                }
            } while (WorldType.worldTypes[this.worldTypeId] == null || !WorldType.worldTypes[this.worldTypeId].getCanBeCreated());
            this.generatorOptionsToUse = "";
            updateButtonText();
            func_82288_a(this.moreOptions);
            ci.cancel();
        }
        if (par1GuiButton.id == 7) {
            this.bonusItems = !this.bonusItems;
            updateButtonText();
            ci.cancel();
        }
        if (par1GuiButton.id == 6) {
            this.commandsToggled = true;
            this.commandsAllowed = !this.commandsAllowed;
            updateButtonText();
            ci.cancel();
        }
    }

    @Inject(method = {"updateButtonText"}, at = {@At("TAIL")})
    private void updateButtonText(CallbackInfo ci) {
        this.buttonBonusItems.displayString = I18n.getString("selectWorld.bonusItems") + " ";
        if (this.bonusItems) {
            this.buttonBonusItems.displayString += I18n.getString("options.on");
        } else {
            this.buttonBonusItems.displayString += I18n.getString("options.off");
        }
    }
}