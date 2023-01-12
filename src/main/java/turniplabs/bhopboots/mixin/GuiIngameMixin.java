package turniplabs.bhopboots.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Gamemode;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ItemStack;
import net.minecraft.src.helper.Utils;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.bhopboots.BhopBoots;

@Mixin(value = GuiIngame.class, remap = false)
@Debug(export = true)
public class GuiIngameMixin {

    @Shadow
    private Minecraft mc;

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
                    target = "Lnet/minecraft/src/GameSettings;showDebugScreen:Lnet/minecraft/src/option/BooleanOption;"
            ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/src/MovingObjectPosition;sideHit:I"),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;fpsInOverlay:Lnet/minecraft/src/option/BooleanOption;")
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
                    target = "Lnet/minecraft/src/GuiIngame;drawString(Lnet/minecraft/src/FontRenderer;Ljava/lang/String;III)V"
            ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;showDebugScreen:Lnet/minecraft/src/option/BooleanOption;"),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;armorDurabilityOverlay:Lnet/minecraft/src/option/BooleanOption;")
            )
    )
    private void bhopboots_captureLocals2(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci,  @Local(ordinal = 5) int tsp, @Local(ordinal = 6) int line, @Local(ordinal = 7) int lineHeight) {
        this.tsp = tsp;
        this.lineHeight = lineHeight;
        this.line = line;
    }

    @Inject(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;armorDurabilityOverlay:Lnet/minecraft/src/option/BooleanOption;"))
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
                if (itemStack != null && itemStack.itemID == BhopBoots.speedometer.itemID) {
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
        double x = (player.posX - player.lastTickPosX) * 20;
        double y = (player.posY - player.lastTickPosY) * 20;
        double z = (player.posZ - player.lastTickPosZ) * 20;
        double speed = Math.sqrt(x*x + y*y + z*z);
        return Utils.floor100(speed);
    }
}
