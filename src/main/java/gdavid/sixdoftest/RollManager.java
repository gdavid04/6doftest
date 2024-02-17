package gdavid.sixdoftest;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public class RollManager {

    public static float getRoll(Entity entity) {
        return (float) entity.getVelocity().dotProduct(entity.getRotationVecClient().crossProduct(new Vec3d(0, 1, 0)));
    }

    @Environment(EnvType.CLIENT)
    public static void rollTransform(MatrixStack ms, Entity entity) {
        rollTransform(ms, getRoll(entity));
    }

    @Environment(EnvType.CLIENT)
    public static void rollTransform(MatrixStack ms, float roll) {
        Quaternionf q = RotationAxis.POSITIVE_Z.rotation(roll);
        var mats = ms.peek();
        mats.getPositionMatrix().rotateLocal(q);
        mats.getNormalMatrix().rotateLocal(q);
    }

}
