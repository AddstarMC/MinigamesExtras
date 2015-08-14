package au.com.addstar.minigames.extras.disguises;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.config.Flag;
import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.InteractionInterface;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;
import au.com.mineauz.minigames.menu.MenuItemBack;
import au.com.mineauz.minigames.menu.MenuItemCustom;
import au.com.mineauz.minigames.minigame.Minigame;
import au.com.mineauz.minigames.minigame.modules.MinigameModule;

public class DisguisesModule extends MinigameModule {
	public DisguisesModule(Minigame mgm) {
		super(mgm);
	}

	@Override
	public void addEditMenuOptions(final Menu menu) {
		MenuItemCustom openOptions = new MenuItemCustom("Disguise Settings", Material.POTION);
		openOptions.setClick(new InteractionInterface() {
			@Override
			public Object interact(Object arg0) {
				Menu subMenu = createMenu(menu, menu.getViewer());
				subMenu.displayMenu(menu.getViewer());

				return null;
			}
		});

		menu.addItem(openOptions);
	}

	private Menu createMenu(Menu previous, final MinigamePlayer player) {
		final Menu menu = new Menu(6, "Disguise Settings", player);

		MenuItem test = new MenuItem("Test", Material.STONE) {
			@Override
			public ItemStack onClick() {
				Menu subMenu = DisguiseSelection.createDisguiseSelectMenu(menu, player, new Callback<StoredDisguise>() {
					@Override
					public void setValue(StoredDisguise value) {
						System.out.println(value);
					}

					@Override
					public StoredDisguise getValue() {
						throw new UnsupportedOperationException();
					}
				});
				subMenu.displayMenu(player);
				return null;
			}
		};

		menu.addItem(test);
		// TODO: This
		menu.addItem(new MenuItemBack(previous), menu.getSize() - 9);
		return menu;
	}

	@Override
	public boolean displayMechanicSettings(Menu menu) {
		return false;
	}

	@Override
	public Map<String, Flag<?>> getFlags() {
		return null;
	}

	@Override
	public String getName() {
		return "disguises";
	}

	@Override
	public void load(FileConfiguration config) {
	}

	@Override
	public void save(FileConfiguration config) {
	}

	@Override
	public boolean useSeparateConfig() {
		return false;
	}
}
