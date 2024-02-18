package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.ClientMod;
import gdavid.sixdoftest.IRoll;
import gdavid.sixdoftest.SpaceManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow @Final private static TrackedData<Boolean> NO_GRAVITY;

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

	@Redirect(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hasNoGravity()Z"))
	private boolean noSaveGravityIn6DOFSpace(Entity instance) {
		// TODO: do the same hack for Bucketable.copyDataToStack, we don't want people bringing back floating fish from space
		if (SpaceManager.isIn6dof(instance)) return self().getDataTracker().get(NO_GRAVITY);
		return instance.hasNoGravity();
	}

	@Inject(method = "isCrawling", at = @At("HEAD"), cancellable = true)
	private void noCrawlIn6DOFSpace(CallbackInfoReturnable<Boolean> callback) {
		if (!SpaceManager.isIn6dof(self())) return;
		callback.setReturnValue(false);
	}

	@Unique
	private static double lastRollUpdateTime = 0;

	// TODO: sensitivity setting for roll
	@Unique
	private static final float rollSpeed = 90.0f;

	@Environment(EnvType.CLIENT)
	@Inject(method = "changeLookDirection", at = @At("HEAD"))
	private void rollLook(CallbackInfo callback) {
		if (!SpaceManager.isIn6dof(self())) return;
		if (!(self() instanceof ClientPlayerEntity)) return;
		IRoll roll = (IRoll) self();
		double time = GlfwUtil.getTime();
		double deltaTime = time - lastRollUpdateTime;
		float delta = (ClientMod.keyRollRight.isPressed() ? 1 : 0) - (ClientMod.keyRollLeft.isPressed() ? 1 : 0);
		roll.addRollf(delta * rollSpeed * (float) deltaTime);
		lastRollUpdateTime = time;
	}

}
