package au.com.addstar.minigames.extras.disguises;

import org.bukkit.plugin.Plugin;

import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigames.minigame.modules.LoadoutModule;

public final class Disguises {
	public static void initialize(Minigames minigames, Plugin thisPlugin) {
		minigames.getMinigameData().addModule(DisguisesModule.class);
		LoadoutModule.registerAddon(thisPlugin, new DisguiseAddon());
	}
}
