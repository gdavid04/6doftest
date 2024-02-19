package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.SpaceManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerMixin extends LivingEntity {
	
	@Unique
	private ClientPlayerEntity self() {
		return (ClientPlayerEntity) (Object) this;
	}
	
	@Shadow protected abstract boolean isCamera();
	
	protected ClientPlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "tickNewAi", at = @At("TAIL"))
	private void tickNewAi(CallbackInfo info) {
		if (!isCamera() || !SpaceManager.isIn6dof(self())) return;
		self().upwardSpeed = (jumping ? 1 : 0) - (self().input.sneaking ? 1 : 0);
	}
	
}
