package au.com.addstar.minigames.extras.effects.menu;

import java.util.Arrays;

import au.com.mineauz.minigames.MinigameMessageType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.menu.MenuItem;

public class MenuItemVector extends MenuItem {
	private Vector vector;
	private int component;
	
	private float increment;
	
	public MenuItemVector(String name, Material displayItem, Vector vector, float increment) {
		super(name, displayItem);
		
		this.vector = vector;
		this.increment = increment;
		update();
	}
	
	@Override
	public void update() {
		StringBuffer buffer = new StringBuffer();
		
		// X
		if (component == 0) {
			buffer.append(ChatColor.YELLOW);
			buffer.append(ChatColor.UNDERLINE);
		} else {
			buffer.append(ChatColor.GREEN);
		}
		
		buffer.append(String.format("%02.2f", vector.getX()));
		buffer.append(ChatColor.GREEN);
		buffer.append(":");
		
		// Y
		if (component == 1) {
			buffer.append(ChatColor.YELLOW);
			buffer.append(ChatColor.UNDERLINE);
		} else {
			buffer.append(ChatColor.GREEN);
		}
		
		buffer.append(String.format("%02.2f", vector.getY()));
		buffer.append(ChatColor.GREEN);
		buffer.append(":");
		
		// Z
		if (component == 2) {
			buffer.append(ChatColor.YELLOW);
			buffer.append(ChatColor.UNDERLINE);
		} else {
			buffer.append(ChatColor.GREEN);
		}
		
		buffer.append(String.format("%02.2f", vector.getZ()));
		buffer.append(ChatColor.GREEN);
		
		setDescription(Arrays.asList(buffer.toString()));
	}

	private void add(float amount) {
		switch (component) {
		case 0:
			vector.setX(vector.getX() + amount);
			break;
		case 1:
			vector.setY(vector.getY() + amount);
			break;
		case 2:
			vector.setZ(vector.getZ() + amount);
			break;
		}
		
		update();
	}
	
	@Override
	public ItemStack onClick() {
		add(increment);
		
		return getItem();
	}
	
	@Override
	public ItemStack onRightClick() {
		add(-increment);
		
		return getItem();
	}
	
	@Override
	public ItemStack onShiftClick() {
		++component;
		if (component > 2) {
			component = 0;
		}
		
		update();
		
		return getItem();
	}
	
	@Override
	public ItemStack onShiftRightClick() {
		--component;
		if (component < 0) {
			component = 2;
		}
		
		update();
		
		return getItem();
	}
	
	@Override
	public ItemStack onDoubleClick() {
		MinigamePlayer player = getViewer();
		player.setNoClose(true);
		player.getPlayer().closeInventory();
		player.sendMessage("Enter the new value into chat for " + getName() + ". The expected format is '<X>:<Y>:<Z>' where <?> represents a floating point value. The menu will automatically reopen in 10s if nothing is entered.", null);
		
		player.setManualEntry(this);
		getContainer().startReopenTimer(10);
		return getItem();
	}
	
	@Override
	public void checkValidEntry(String entry) {
		String[] parts = entry.split(":");
		if (parts.length != 3) {
			getViewer().sendMessage( "Entered value does not match the required format", MinigameMessageType.ERROR);
		} else {
			try {
				double x,y,z;
				x = Double.parseDouble(parts[0]);
				y = Double.parseDouble(parts[1]);
				z = Double.parseDouble(parts[2]);
				
				vector.setX(x);
				vector.setY(y);
				vector.setZ(z);
				update();
			} catch (NumberFormatException e) {
				getViewer().sendMessage("Entered value does not match the required format", MinigameMessageType.ERROR);
			}
		}
		
		getContainer().cancelReopenTimer();
		getContainer().displayMenu(getContainer().getViewer());
	}
	
	private MinigamePlayer getViewer() {
		return getContainer().getViewer();
	}
}
