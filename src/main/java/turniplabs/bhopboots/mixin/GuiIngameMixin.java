package turniplabs.bhopboots.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.bhopboots.BhopBoots;

@Mixin(value = GuiIngame.class, remap = false)
public class GuiIngameMixin {

    @Shadow
    protected Minecraft mc;

    @Unique
    private int tsp;

    @Unique
    private int lineHeight;

    @Unique
    private int line;

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "renderGameOverlay",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/option/BooleanOption;value:Ljava/lang/Object;"
            ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/core/HitResult;side:Lnet/minecraft/core/util/helper/Side;"),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/client/option/BooleanOption;value:Ljava/lang/Object;")
            )
    )
    private void bhopboots_captureLocals1(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci,  @Local(ordinal = 5) int tsp, @Local(ordinal = 6) int line, @Local(ordinal = 7) int lineHeight) {
        this.tsp = tsp;
        this.lineHeight = lineHeight;
        this.line = line;
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "renderGameOverlay",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiIngame;drawString(Lnet/minecraft/client/render/FontRenderer;Ljava/lang/String;III)V"
            ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameSettings;showDebugScreen:Lnet/minecraft/client/option/BooleanOption;"),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameSettings;armorDurabilityOverlay:Lnet/minecraft/client/option/BooleanOption;")
            )
    )
    private void bhopboots_captureLocals2(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci,  @Local(ordinal = 5) int tsp, @Local(ordinal = 6) int line, @Local(ordinal = 7) int lineHeight) {
        this.tsp = tsp;
        this.lineHeight = lineHeight;
        this.line = line;
    }

    @Inject(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameSettings;armorDurabilityOverlay:Lnet/minecraft/client/option/BooleanOption;"))
    private void bhopboots_renderGameOverlay(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.mc.gameSettings.showDebugScreen.value) {
            return;
        }

        boolean renderSpeedometer = false;
        if (mc.thePlayer.getGamemode() == Gamemode.creative) {
            renderSpeedometer = true;
        } else {
            for (int i = 0; i < this.mc.thePlayer.inventory.getSizeInventory(); i++) {

                ItemStack itemStack = this.mc.thePlayer.inventory.getStackInSlot(i);
                if (itemStack != null && itemStack.itemID == BhopBoots.speedometer.id) {
                    renderSpeedometer = true;
                }
            }
        }

        if (renderSpeedometer) {
            GuiIngame guiIngame = (GuiIngame) (Object) this;
            guiIngame.drawString(this.mc.fontRenderer, "Speed: " + calculateSpeed(this.mc.thePlayer), this.tsp, this.tsp + this.lineHeight * this.line, 0xFFFFFF);
        }
    }

    private double calculateSpeed(EntityPlayer player) {
        double x = (player.x - player.xOld) * 20;
        double y = (player.y - player.yOld) * 20;
        double z = (player.z - player.zOld) * 20;
        double speed = Math.sqrt(x*x + y*y + z*z);
        return Utils.floor100(speed);
    }
}
