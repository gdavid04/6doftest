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
     * Get the roll in degrees.
     */
    float getRollf();

    /**
     * Set the roll in degrees.
     */
    void setRollf(float roll);

    /**
     * Get the interpolated roll.
     */
    float getRollf(float partialTicks);

    @Environment(EnvType.CLIENT)
    static void rollTransform(MatrixStack ms, Entity entity) {
        if (entity instanceof IRoll roll) rollTransform(ms, roll.getRollf(MinecraftClient.getInstance().getTickDelta()));
    }

    @Environment(EnvType.CLIENT)
    static void rollTransform(MatrixStack ms, float roll) {
        Quaternionf q = RotationAxis.POSITIVE_Z.rotationDegrees(roll);
        var mats = ms.peek();
        mats.getPositionMatrix().rotateLocal(q);
        mats.getNormalMatrix().rotateLocal(q);
    }

}
