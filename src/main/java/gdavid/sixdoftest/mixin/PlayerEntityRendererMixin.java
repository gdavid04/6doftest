package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.IRoll;
import gdavid.sixdoftest.SpaceManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	
	@Redirect(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isTouchingWater()Z"))
	private boolean fix6DOFSwimPose(AbstractClientPlayerEntity instance) {
		return SpaceManager.isIn6dof(instance) || instance.isTouchingWater();
	}

	@Inject(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("TAIL"))
	private void setup6DOFTranform(AbstractClientPlayerEntity player, MatrixStack matrixStack, float f, float g, float h, CallbackInfo callback) {
		if (!SpaceManager.isIn6dof(player)) return;
		IRoll.rollTransform(matrixStack, player, false);
	}

}
