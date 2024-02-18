package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.IRoll;
import gdavid.sixdoftest.SpaceManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IRoll {

    @Unique
    private PlayerEntity self() {
        return (PlayerEntity) (Object) this;
    }

    @Unique
    private float roll;

    /**
     * The previous roll value, used for interpolation.
     */
    @Unique
    private float prevRoll;

    @Override
    public float getRollf() {
        return roll;
    }

    @Override
    public void setRollf(float roll) {
        this.roll = MathHelper.wrapDegrees(roll);
    }

    @Override
    public void addRollf(float roll) {
        setRollf(getRollf() + roll);
        prevRoll = MathHelper.wrapDegrees(prevRoll + roll);
    }

    @Override
    public float getRollf(float partialTicks) {
        return MathHelper.lerp(partialTicks, prevRoll, roll);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void updateRoll(CallbackInfo callback) {
        prevRoll = roll;
        // Transition back to upright when not in 6dof
        if (!SpaceManager.isIn6dof(self())) {
            // TODO: better transition
            if (Math.abs(roll) < 0.01f) roll = 0;
            else roll *= 0.9f;
        }
    }

}
