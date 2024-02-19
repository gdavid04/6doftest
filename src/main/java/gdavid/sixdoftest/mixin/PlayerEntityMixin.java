package gdavid.sixdoftest.mixin;

import gdavid.sixdoftest.IRoll;
import gdavid.sixdoftest.SpaceManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.Math;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IRoll {
    
    @Shadow public abstract PlayerAbilities getAbilities();
    
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
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
        Vector3d cur = IRoll.rotate(getPitch(), getYaw(), roll, rot);
        Vector3d prev = IRoll.rotate(prevPitch, prevYaw, prevRoll, rot);
        setPitch((float) cur.x);
        setYaw((float) cur.y);
        roll = (float) cur.z;
        prevPitch = (float) prev.x;
        prevYaw = (float) prev.y;
        prevRoll = (float) prev.z;
    }
    
    @Override
    public float getRollf() {
        return roll;
    }

    @Override
    public float getRollf(float partialTicks) {
        return MathHelper.lerp(partialTicks, prevRoll, roll);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void updateRoll(CallbackInfo callback) {
        prevRoll = roll;
        // Transition back to upright when not in 6dof
        if (!SpaceManager.isIn6dof(this)) {
            // TODO: better transition
            if (Math.abs(roll) < 0.01f) roll = 0;
            else roll *= 0.9f;
        }
    }
    
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void travelIn6DOFSpace(Vec3d movementInput, CallbackInfo callback) {
        if (!SpaceManager.isIn6dof(this) || !isSwimming() || hasVehicle() || getAbilities().flying) return;
        callback.cancel();
        // Bypass pitch based ascent/descent
        double x = getX();
        double y = getY();
        double z = getZ();
        super.travel(movementInput);
        self().increaseTravelMotionStats(getX() - x, getY() - y, getZ() - z);
    }

}
