package au.com.addstar.minigames.extras;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import au.com.mineauz.minigames.config.Flag;
import au.com.mineauz.minigames.menu.MenuItem;

public class BasicFlag<T> extends Flag<T> {
	@Override
	public void saveValue(String path, FileConfiguration config) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void loadValue(String path, FileConfiguration config) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MenuItem getMenuItem(String name, Material displayItem) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MenuItem getMenuItem(String name, Material displayItem, List<String> description) {
		throw new UnsupportedOperationException();
	}

}
