package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.IRoll;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	
	@Shadow @Final private Camera camera;
	
	@Inject(method = "renderWorld", at = @At("HEAD"))
	private void rollRender(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo callback) {
		IRoll.rollTransform(matrices, camera.getFocusedEntity(), true);
	}
	
}
