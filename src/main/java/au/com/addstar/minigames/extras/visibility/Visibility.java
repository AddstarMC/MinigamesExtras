package au.com.addstar.minigames.extras.visibility;

import au.com.mineauz.minigames.Minigames;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class Visibility {
    public static Minigames plugin;
	public static void initialize(Minigames minigames, Plugin thisPlugin) {
		plugin = minigames;
		minigames.getMinigameManager().addModule(VisibilityModule.class);
		Bukkit.getPluginManager().registerEvents(new VisibilityListener(), thisPlugin);
	}
}
