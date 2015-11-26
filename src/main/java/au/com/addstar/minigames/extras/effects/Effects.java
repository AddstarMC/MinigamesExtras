package au.com.addstar.minigames.extras.effects;

import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigamesregions.Main;

public final class Effects {
	private Effects() {}
	
	public static void initialize(Minigames minigames, Main regions) {
		minigames.mdata.addModule(EffectModule.class);
	}
}
