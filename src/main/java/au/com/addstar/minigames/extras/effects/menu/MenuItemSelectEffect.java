package au.com.addstar.minigames.extras.effects.menu;

import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import au.com.addstar.minigames.extras.effects.EffectMenus;
import au.com.addstar.minigames.extras.effects.EffectModule;
import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;

public class MenuItemSelectEffect extends MenuItem {
	private final EffectModule module;
	private final Callback<String> callback;
	
	public MenuItemSelectEffect(String name, Material displayItem, EffectModule module, Callback<String> callback) {
		super(name, displayItem);
		
		this.module = module;
		this.callback = callback;
		update();
	}
	
	@Override
	public void update() {
		if (callback.getValue() == null) {
			setDescription(Arrays.asList(ChatColor.RED + "Not Set"));			
		} else {
			setDescription(Arrays.asList(ChatColor.GREEN + WordUtils.capitalizeFully(callback.getValue())));
		}
	}
	
	@Override
	public ItemStack onClick() {
		Menu menu = EffectMenus.selectEffect(getContainer(), module, new Callback<String>() {
			@Override
			public void setValue(String value) {
				callback.setValue(value);	
				update();
			}
			
			@Override
			public String getValue() {
				return callback.getValue();
			}
		});
		
		menu.displayMenu(getContainer().getViewer());
		
		return null;
	}
}
