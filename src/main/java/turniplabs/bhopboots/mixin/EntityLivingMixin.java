package turniplabs.bhopboots.mixin;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.helper.Utils;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.bhopboots.BhopBoots;

@Mixin(value = EntityLiving.class, remap = false)
@Debug(export = true)
public class EntityLivingMixin {
    @Shadow
    protected float moveStrafing;
    @Unique
    private float oldYaw = 0.0F;

    @Unique
    private boolean lastTickOnGround;

    @Unique
    private boolean lastlastTickOnGround;

    @ModifyVariable(method = "moveEntityWithHeading",
            at = @At(value = "STORE", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/src/MathHelper;floor_double(D)I")),
            ordinal = 2
    )
    private float bhopboots_moveEntityWithHeading1(float friction) {
        EntityLiving entity = (EntityLiving) (Object) this;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;

            if (player.noClip) {
                return friction;
            }

            ItemStack bootItem = player.inventory.armorItemInSlot(0);
            if (bootItem == null) {
                return friction;
            }

            if (bootItem.itemID != BhopBoots.bhopBoots.itemID) {
                return friction;
            }

            if (!this.lastlastTickOnGround && this.lastTickOnGround) {
                return 0.8f * 0.91f;
            }
        }

        return friction;
    }

    @ModifyVariable(method = "moveEntityWithHeading",
            at = @At(value = "STORE", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/src/MathHelper;floor_double(D)I", ordinal = 3)),
            ordinal = 2
    )
    private float bhopboots_moveEntityWithHeading2(float friction) {
        EntityLiving entity = (EntityLiving) (Object) this;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;

            if (player.noClip) {
                return friction;
            }

            ItemStack bootItem = player.inventory.armorItemInSlot(0);
            if (bootItem == null) {
                return friction;
            }

            if (bootItem.itemID != BhopBoots.bhopBoots.itemID) {
                return friction;
            }

            if (!this.lastlastTickOnGround && this.lastTickOnGround) {
                return 0.8f * 0.91f;
            }
        }

        return friction;
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void bhopboots_onLivingUpdate1(CallbackInfo ci) {
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

            float motion = 1.0f + Math.abs(this.moveStrafing) / 8.0f;
            player.motionX *= motion;
            player.motionZ *= motion;
        }
    }

    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    private void bhopboots_onLivingUpdate2(CallbackInfo ci) {
        EntityLiving entity = (EntityLiving) (Object) this;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            this.lastlastTickOnGround = this.lastTickOnGround;
            this.lastTickOnGround = player.onGround;
        }

    }
}
