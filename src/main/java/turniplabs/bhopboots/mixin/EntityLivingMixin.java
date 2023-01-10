package turniplabs.bhopboots.mixin;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.bhopboots.BhopBoots;

@Mixin(value = EntityLiving.class, remap = false)
public class EntityLivingMixin {
    @Shadow
    protected float moveStrafing;
    @Unique
    private float oldYaw = 0.0F;

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void bunnyHop(CallbackInfo ci) {
        EntityLiving entity = (EntityLiving) (Object) this;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            double deltaYaw = Math.toRadians(player.rotationYaw - this.oldYaw);
            this.oldYaw = player.rotationYaw;

            if (player.noClip) {
                return;
            }

            if (player.onGround) {
                return;
            }

            ItemStack bootItem = player.inventory.armorItemInSlot(0);
            if (bootItem == null) {
                return;
            }

            if (bootItem.itemID != BhopBoots.bhopBoots.itemID) {
                return;
            }

            double newX = Math.cos(deltaYaw) * player.motionX - Math.sin(deltaYaw) * player.motionZ;
            double newZ = Math.sin(deltaYaw) * player.motionX + Math.cos(deltaYaw) * player.motionZ;
            player.motionX = newX;
            player.motionZ = newZ;

            float speed = 1.0f + Math.abs(this.moveStrafing) / 8.0f;
            player.motionX *= speed;
            player.motionZ *= speed;
        }
    }
}
