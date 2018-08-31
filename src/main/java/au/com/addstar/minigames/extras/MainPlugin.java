package au.com.addstar.minigames.extras;

import au.com.addstar.minigames.extras.disguises.Disguises;
import au.com.addstar.minigames.extras.effects.Effects;
import au.com.addstar.minigames.extras.visibility.Visibility;
import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigamesregions.Main;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {


    @Override
	public void onEnable() {
		Minigames minigamesPlugin = Minigames.getPlugin();
		
		// Enable each add-on
		if (getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
			Disguises.initialize(minigamesPlugin, this);
		}
		
		Visibility.initialize(minigamesPlugin, this);
		Effects.initialize(minigamesPlugin, Main.getPlugin());
	}
}
