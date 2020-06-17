package au.com.addstar.minigames.extras.effects.menu;

import au.com.mineauz.minigames.MinigameMessageType;
import au.com.mineauz.minigames.objects.MinigamePlayer;
import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.MenuItem;
import com.google.common.collect.Maps;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

public class MenuItemColor extends MenuItem {
	private static final Map<String, Color> namedColours = Maps.newHashMap();
	
	static {
		// Build name map
		try	{
			for (Field field : Color.class.getFields()) {
				if (field.getType().equals(Color.class) && Modifier.isStatic(field.getModifiers())) {
					namedColours.put(field.getName().toLowerCase(), (Color)FieldUtils.readStaticField(field));
				}
			}
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}
	
	private Callback<Color> callback;
	
	public MenuItemColor(String name, Material displayItem, Callback<Color> callback) {
		super(name, displayItem);
		this.callback = callback;
		update();
	}
	
	@Override
	public void update() {
		Color color = callback.getValue();
		String line;
		if (color == null) {
			line = ChatColor.RED + "Not Set";
		} else {
			line = ChatColor.GREEN + String.format("%06X", color.asRGB());
		}
		
		setDescription(Arrays.asList(line, "Left click to edit", "Right click to clear"));
	}
	
	@Override
	public ItemStack onClick() {
		MinigamePlayer player = getContainer().getViewer();
		player.setNoClose(true);
		player.getPlayer().closeInventory();
        player.sendInfoMessage("Enter the new value into chat for " + getName() + ". Please enter the name of a colour, the hex value, or 'none'. The menu will automatically reopen in 15s if nothing is entered.");
		
		player.setManualEntry(this);
		getContainer().startReopenTimer(15);
		return getItem();
	}
	
	@Override
	public ItemStack onRightClick() {
		callback.setValue(null);
		update();
		return getItem();
	}
	
	@Override
	public void checkValidEntry(String entry) {
		MinigamePlayer player = getContainer().getViewer();
		if (entry.equalsIgnoreCase("none")) {
			callback.setValue(null);
		} else {
			Color color = namedColours.get(entry.toLowerCase());
			if (color != null) {
				callback.setValue(color);
			} else {
				try {
					int rgb = Integer.parseInt(entry, 16);
					
					if (rgb < 0) {
						player.sendMessage("That is not a valid color.", MinigameMessageType.ERROR);
					} else {
						callback.setValue(Color.fromRGB(rgb));
					}
				} catch (NumberFormatException e) {
					player.sendMessage("That is not a valid color.", MinigameMessageType.ERROR);				}
			}
		}
		
		update();
		
		getContainer().cancelReopenTimer();
		getContainer().displayMenu(getContainer().getViewer());
	}
}
