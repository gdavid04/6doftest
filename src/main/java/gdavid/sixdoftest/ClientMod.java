package gdavid.sixdoftest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public class ClientMod implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		WorldRenderEvents.AFTER_SETUP.register((ctx) -> {
			Entity focus = ctx.camera().getFocusedEntity();
			if (focus == null) return;
			float roll = (float) focus.getVelocity().dotProduct(focus.getRotationVecClient().crossProduct(new Vec3d(0, 1, 0)));
			Quaternionf q = RotationAxis.POSITIVE_Z.rotation(roll);
			var mats = ctx.matrixStack().peek();
			mats.getPositionMatrix().rotateLocal(q);
			mats.getNormalMatrix().rotateLocal(q);
		});
	}
	
}
