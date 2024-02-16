package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.SpaceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	
	@Unique
	private Entity self() {
		return (Entity) (Object) this;
	}
	
	@Inject(method = "updateSwimming", at = @At("HEAD"), cancellable = true)
	private void swimIn6DOFSpace(CallbackInfo callback) {
		if (!SpaceManager.isIn6dof(self())) return;
		callback.cancel();
		self().setSwimming(!self().hasVehicle());
	}
	
	@Inject(method = "hasNoGravity", at = @At("HEAD"), cancellable = true)
	private void noGravityIn6DOFSpace(CallbackInfoReturnable<Boolean> callback) {
		if (!SpaceManager.isIn6dof(self())) return;
		callback.setReturnValue(true);
	}
	
	@Inject(method = "isCrawling", at = @At("HEAD"), cancellable = true)
	private void noCrawlIn6DOFSpace(CallbackInfoReturnable<Boolean> callback) {
		if (!SpaceManager.isIn6dof(self())) return;
		callback.setReturnValue(false);
	}
	
	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void noFallDamageIn6DOFSpace(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> callback) {
		if (!SpaceManager.isIn6dof(self())) return;
		callback.setReturnValue(false);
	}
	
}
