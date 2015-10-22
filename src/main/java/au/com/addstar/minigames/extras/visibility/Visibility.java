package au.com.addstar.minigames.extras.visibility;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import au.com.mineauz.minigames.Minigames;

public final class Visibility {
	public static void initialize(Minigames minigames, Plugin thisPlugin) {
		minigames.mdata.addModule(VisibilityModule.class);
		Bukkit.getPluginManager().registerEvents(new VisibilityListener(), thisPlugin);
	}
}
