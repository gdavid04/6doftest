package gdavid.sixdoftest;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;

public interface IRoll {

    /**
     * Apply rotation.
     * For handling per frame changes, bypasses tick interpolation locally.
     */
    void rotate(float dr, float dx, float dy);

    float getRollf();
    
    /**
     * Get the interpolated roll.
     */
    float getRollf(float partialTicks);
    
    static Vector3d rotate(double pitch, double yaw, double roll, Quaterniond rot) {
        return new Quaterniond()
                .rotateZ(Math.toRadians(roll))
                .rotateX(Math.toRadians(pitch))
                .rotateY(Math.toRadians(yaw))
                .premul(rot)
                .getEulerAnglesZXY(new Vector3d())
                .mul(180 / Math.PI);
    }

    @Environment(EnvType.CLIENT)
    static void rollTransform(MatrixStack ms, Entity entity, boolean invert) {
        if (entity instanceof IRoll roll) rollTransform(ms, roll.getRollf(MinecraftClient.getInstance().getTickDelta()), invert);
    }

    @Environment(EnvType.CLIENT)
    static void rollTransform(MatrixStack ms, float roll, boolean invert) {
        Quaternionf q = RotationAxis.NEGATIVE_Z.rotationDegrees(roll * (invert ? -1 : 1));
        var mats = ms.peek();
        mats.getPositionMatrix().rotateLocal(q);
        mats.getNormalMatrix().rotateLocal(q);
    }

}
