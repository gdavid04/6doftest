package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.SpaceManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
    
    @Inject(method = "getNextAirOnLand", at = @At("HEAD"), cancellable = true)
    private void noAirInSpace(int air, CallbackInfoReturnable<Integer> callback) {
        if (!SpaceManager.isIn6dof(self())) return;
        callback.setReturnValue(air); // Prevent refilling air
    }
    
    @Inject(method = "baseTick", at = @At("HEAD"))
    private void depleteAirInSpace(CallbackInfo callback) {
        if (!SpaceManager.isIn6dof(self()) || self().isSubmergedIn(FluidTags.WATER)) return;
        self().setAir(self().getAir() - 1);
        if (self().getAir() == -20) {
            self().setAir(0);
            self().damage(self().getDamageSources().drown(), 2.0F);
        }
    }

}
