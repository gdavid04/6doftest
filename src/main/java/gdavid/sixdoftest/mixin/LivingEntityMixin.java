package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.SpaceManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private LivingEntity self() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    private void noFallDamageIn6DOFSpace(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> callback) {
        if (!SpaceManager.isIn6dof(self())) return;
        callback.setReturnValue(0);
    }

}
