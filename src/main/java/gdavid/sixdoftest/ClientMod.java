package gdavid.sixdoftest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class ClientMod implements ClientModInitializer {

	public static final KeyBinding keyRollLeft = new KeyBinding("key.sixdoftest.rollLeft", GLFW.GLFW_KEY_Z, KeyBinding.MOVEMENT_CATEGORY);
	public static final KeyBinding keyRollRight = new KeyBinding("key.sixdoftest.rollRight", GLFW.GLFW_KEY_X, KeyBinding.MOVEMENT_CATEGORY);

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(keyRollLeft);
		KeyBindingHelper.registerKeyBinding(keyRollRight);
	}
	
}
