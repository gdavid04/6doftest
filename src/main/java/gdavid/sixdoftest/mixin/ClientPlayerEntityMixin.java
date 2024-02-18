package gdavid.sixdoftest.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends PlayerEntityMixin {

    @Override
    public float getRollf(float partialTicks) {
        // We know our own roll in real-time, no need to interpolate
        return getRollf();
    }

}
