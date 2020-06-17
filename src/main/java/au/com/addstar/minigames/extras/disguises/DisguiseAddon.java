package au.com.addstar.minigames.extras.disguises;

import au.com.mineauz.minigames.objects.MinigamePlayer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.minigames.objects.MinigamePlayer;
import au.com.mineauz.minigames.PlayerLoadout;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;
import au.com.mineauz.minigames.minigame.modules.LoadoutModule.LoadoutAddon;

public class DisguiseAddon implements LoadoutAddon<DisguiseSettings> {
	@Override
	public String getName() {
		return "disguise";
	}

	@Override
	public void addMenuOptions(Menu menu, final PlayerLoadout loadout) {
		MenuItem chooseDisguise = new MenuItem("Disguise", Material.PAINTING) {
			@Override
			public ItemStack onClick() {
				DisguiseSettings settings = loadout.getAddonValue(DisguiseAddon.class);
				if (settings == null) {
					settings = new DisguiseSettings();
					loadout.setAddonValue(DisguiseAddon.class, settings);
				}
				
				Menu subMenu = DisguiseSelection.createEditMenu(getContainer(), getContainer().getViewer(), settings);
				subMenu.displayMenu(getContainer().getViewer());
				return null;
			}
		};
		
		menu.addItem(chooseDisguise);
	}

	@Override
	public void save(ConfigurationSection section, DisguiseSettings value) {
		value.save(section);
	}

	@Override
	public DisguiseSettings load(ConfigurationSection section) {
		DisguiseSettings settings = new DisguiseSettings();
		settings.load(section);
		return settings;
	}

	@Override
	public void applyLoadout(MinigamePlayer player, DisguiseSettings value) {
		value.enableDisguise(player);
	}

	@Override
	public void clearLoadout(MinigamePlayer player, DisguiseSettings value) {
		value.disableDisguise(player);
	}
}
