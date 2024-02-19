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
import net.minecraft.util.math.Vec3d;
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
	@Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
	private void rollLook(double dx, double dy, CallbackInfo callback) {
		if (!SpaceManager.isIn6dof(self())) return;
		if (!(self() instanceof ClientPlayerEntity)) return;
		IRoll roll = (IRoll) self();
		double time = GlfwUtil.getTime();
		double deltaTime = time - lastRollUpdateTime;
		float dr = (ClientMod.keyRollRight.isPressed() ? 1 : 0) - (ClientMod.keyRollLeft.isPressed() ? 1 : 0);
		roll.rotate(dr * rollSpeed * (float) deltaTime, (float) dx * 0.15f, (float) dy * 0.15f);
		lastRollUpdateTime = time;
		if (self().getVehicle() != null) self().getVehicle().onPassengerLookAround(self()); // Stay consistent with vanilla behavior
		callback.cancel(); // We already handled mouse look
	}
	
	@Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
	private void movementIn6DOFSpace(float speed, Vec3d movementInput, CallbackInfo callback) {
		if (!SpaceManager.isIn6dof(self()) || !(self() instanceof IRoll roll)) return;
		callback.cancel();
		double sqMag = movementInput.lengthSquared();
		if (sqMag < 1e-7) return;
		// Perform voodoo magic on coordinates
		Vec3d vel = (sqMag > 1 ? movementInput.normalize() : movementInput).multiply(speed)
				.rotateZ((float) Math.toRadians(roll.getRollf()))
				.rotateX((float) Math.toRadians(self().getPitch()))
				.rotateY(-(float) Math.toRadians(self().getYaw()))
				.multiply(1, -1, 1); // Up is down
		self().setVelocity(self().getVelocity().add(vel));
	}

}
