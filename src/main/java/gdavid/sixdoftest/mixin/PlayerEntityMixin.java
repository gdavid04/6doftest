package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.IRoll;
import gdavid.sixdoftest.SpaceManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.Math;

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
    public void rotate(float dr, float dx, float dy) {
        Quaterniond rot = new Quaterniond()
                .rotateZ(Math.toRadians(dr))
                .rotateX(Math.toRadians(dy))
                .rotateY(Math.toRadians(dx));
        Vector3d cur = rotate(new Vector3d(self().getPitch(), self().getYaw(), roll), rot);
        Vector3d prev = rotate(new Vector3d(self().prevPitch, self().prevYaw, prevRoll), rot);
        self().setPitch((float) cur.x);
        self().setYaw((float) cur.y);
        roll = (float) cur.z;
        self().prevPitch = (float) prev.x;
        self().prevYaw = (float) prev.y;
        prevRoll = (float) prev.z;
    }
    
    @Unique
    private Vector3d rotate(Vector3d base, Quaterniond rot) {
        return new Quaterniond()
            .rotateZ(Math.toRadians(base.z))
            .rotateX(Math.toRadians(base.x))
            .rotateY(Math.toRadians(base.y))
            .premul(rot)
            .getEulerAnglesZXY(new Vector3d())
            .mul(180 / Math.PI);
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
