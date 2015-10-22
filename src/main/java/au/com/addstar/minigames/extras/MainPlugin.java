package au.com.addstar.minigames.extras;

import org.bukkit.plugin.java.JavaPlugin;

import au.com.addstar.minigames.extras.disguises.Disguises;
import au.com.addstar.minigames.extras.visibility.Visibility;
import au.com.mineauz.minigames.Minigames;

public class MainPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		Minigames minigamesPlugin = Minigames.plugin;
		
		// Enable each add-on
		if (getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
			Disguises.initialize(minigamesPlugin, this);
		}
		
		Visibility.initialize(minigamesPlugin, this);
	}
}
