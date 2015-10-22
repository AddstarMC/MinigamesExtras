package au.com.addstar.minigames.extras.visibility;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Maps;

import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.config.BooleanFlag;
import au.com.mineauz.minigames.config.Flag;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItemBoolean;
import au.com.mineauz.minigames.minigame.Minigame;
import au.com.mineauz.minigames.minigame.modules.MinigameModule;

public class VisibilityModule extends MinigameModule {
	public static final String Name = "visibility";
	
	private final BooleanFlag hidePlayers;
	
	public VisibilityModule(Minigame minigame) {
		super(minigame);
		
		hidePlayers = new BooleanFlag(false, "hide-players");
	}
	
	@Override
	public String getName() {
		return Name;
	}

	@Override
	public Map<String, Flag<?>> getFlags() {
		Map<String, Flag<?>> flags = Maps.newHashMap();
		flags.put(hidePlayers.getName(), hidePlayers);
		return flags;
	}
	
	public boolean getHidePlayers() {
		return hidePlayers.getFlag();
	}
	
	public void setHidePlayers(boolean hide) {
		hidePlayers.setFlag(hide);
	}

	@Override
	public boolean useSeparateConfig() {
		return false;
	}

	@Override
	public void save(FileConfiguration config) {
	}

	@Override
	public void load(FileConfiguration config) {
	}

	@Override
	public void addEditMenuOptions(Menu menu) {
		menu.addItem(new MenuItemBoolean("Hide players", MinigameUtils.stringToList("Hides all players from;eachother when the game;starts;Singleplayer Only"), Material.LEVER, hidePlayers.getCallback()));
	}

	@Override
	public boolean displayMechanicSettings(Menu previous) {
		return false;
	}

	
}
