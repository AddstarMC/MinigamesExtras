package au.com.addstar.minigames.extras.disguises;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;

public class MenuItemSelectDisguise extends MenuItem {
	private Callback<StoredDisguise> callback;
	
	public MenuItemSelectDisguise(String name, Material material, Callback<StoredDisguise> callback) {
		super(name, material);
		
		this.callback = callback;
		
		updateDescription();
	}
	
	private void updateDescription() {
		StoredDisguise disguise = callback.getValue();
		
		if (disguise != null) {
			setDescription(Arrays.asList(ChatColor.GREEN + disguise.toString(), "Shift + Right click to clear disguise"));
		} else {
			setDescription(Arrays.asList(ChatColor.GRAY + "None"));
		}
	}
	
	@Override
	public ItemStack onClick() {
		// Wrap it so we can update the description
		Callback<StoredDisguise> wrapped = new Callback<StoredDisguise>() {
			@Override
			public void setValue(StoredDisguise value) {
				callback.setValue(value);
				updateDescription();
			}
			
			@Override
			public StoredDisguise getValue() {
				return callback.getValue();
			}
		};
		
		Menu subMenu = DisguiseSelection.createDisguiseSelectMenu(getContainer(), getContainer().getViewer(), wrapped);
		subMenu.displayMenu(getContainer().getViewer());
		
		return null;
	}
	
	@Override
	public ItemStack onShiftRightClick() {
		callback.setValue(null);
		updateDescription();
		return getItem();
	}
}
