package turniplabs.bhopboots.mixin;

//import net.minecraft.src.EntityLiving;
//import net.minecraft.src.EntityPlayer;
//import net.minecraft.src.ItemStack;
//import net.minecraft.src.helper.Utils;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
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
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/core/util/helper/MathHelper;floor_double(D)I")),
            ordinal = 2
    )
    private float bhopboots_moveEntityWithHeading1(float friction) {
        EntityLiving entity = (EntityLiving) (Object) this;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack bootItem = player.inventory.armorItemInSlot(0);
            if (player.noPhysics || bootItem == null || bootItem.itemID != BhopBoots.bhopBoots.id) {
                return friction;
            }

            if (!this.lastlastTickOnGround && this.lastTickOnGround) {
                return 1.71f;
            }
        }

        return friction;
    }

    @ModifyVariable(method = "moveEntityWithHeading",
            at = @At(value = "STORE", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/core/util/helper/MathHelper;floor_double(D)I", ordinal = 3)),
            ordinal = 2
    )
    private float bhopboots_moveEntityWithHeading2(float friction) {
        EntityLiving entity = (EntityLiving) (Object) this;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack bootItem = player.inventory.armorItemInSlot(0);
            if (player.noPhysics || bootItem == null || bootItem.itemID != BhopBoots.bhopBoots.id) {
                return friction;
            }

            if (!this.lastlastTickOnGround && this.lastTickOnGround) {
                return 1.71f;
            }
        }

        return friction;
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void bhopboots_onLivingUpdate1(CallbackInfo ci) {
        EntityLiving entity = (EntityLiving) (Object) this;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            double deltaYaw = Math.toRadians(player.yRot - this.oldYaw);
            this.oldYaw = player.yRot;

            if (player.noPhysics || player.onGround) {
                return;
            }

            ItemStack bootItem = player.inventory.armorItemInSlot(0);
            if (bootItem == null || bootItem.itemID != BhopBoots.bhopBoots.id) {
                return;
            }

            double newX = Math.cos(deltaYaw) * player.xd - Math.sin(deltaYaw) * player.zd;
            double newZ = Math.sin(deltaYaw) * player.xd + Math.cos(deltaYaw) * player.zd;
            player.xd = newX;
            player.zd = newZ;

            float motion = 1.0f + Math.abs(this.moveStrafing) / 8.0f;
            player.xd *= motion;
            player.zd *= motion;
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
