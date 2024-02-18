package gdavid.sixdoftest;

import net.minecraft.entity.Entity;

public class SpaceManager {
	
	public static boolean isIn6dof(Entity entity) {
		// TODO: for testing
		return entity.getX() < 0;
	}
	
}
