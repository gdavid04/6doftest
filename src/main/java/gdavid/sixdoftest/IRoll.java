package gdavid.sixdoftest;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

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
