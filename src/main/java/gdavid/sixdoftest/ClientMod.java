package gdavid.sixdoftest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.Entity;

public class ClientMod implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		WorldRenderEvents.AFTER_SETUP.register((ctx) -> {
			Entity focus = ctx.camera().getFocusedEntity();
			IRoll.rollTransform(ctx.matrixStack(), focus);
		});
	}
	
}
