package au.com.addstar.minigames.extras.effects.menu;

import au.com.mineauz.minigames.MinigameMessageType;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import au.com.addstar.minigames.extras.effects.EffectMenus;
import au.com.addstar.minigames.extras.effects.EffectModule;
import au.com.addstar.monolith.effects.BaseEffect;
import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;

public class MenuItemEffect extends MenuItem {
	private final BaseEffect effect;
	private String name;
	
	private final EffectModule module;
	
	public MenuItemEffect(String name, BaseEffect effect, EffectModule module) {
		super(WordUtils.capitalizeFully(name), Material.FIREWORK_CHARGE);
		
		this.name = name;
		this.effect = effect;
		this.module = module;
		
		setDescription(MinigameUtils.stringToList("Left Click to edit;Shift+Left Click to rename;Shift+Right Click to remove"));
	}
	
	@Override
	public void update() {
		ItemStack item = getItem();
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.RESET + WordUtils.capitalizeFully(name));
		
		item.setItemMeta(meta);
		setItem(item);
	}
	
	@Override
	public ItemStack onClick() {
		Menu menu = EffectMenus.effectEditMenu(getContainer(), effect);
		menu.displayMenu(getContainer().getViewer());
		return null;
	}
	
	@Override
	public ItemStack onShiftClick() {
		MinigamePlayer player = getContainer().getViewer();
		player.setNoClose(true);
		player.getPlayer().closeInventory();
		player.sendMessage("Enter the new name of this effect. The menu will automatically reopen in 20s if nothing is entered.", null);
		
		player.setManualEntry(this);
		getContainer().startReopenTimer(20);
		return null;
	}
	
	@Override
	public void checkValidEntry(String entry) {
		if (entry.equalsIgnoreCase(name)) {
			getContainer().getViewer().sendMessage(ChatColor.GRAY + "Rename cancelled", MinigameMessageType.INFO);
			return;
		}
		
		if (module.getEffect(entry) != null) {
			getContainer().getViewer().sendMessage("That effect already exists", MinigameMessageType.ERROR);
		} else {
			module.removeEffect(name);
			name = entry;
			module.addEffect(name, effect);
			update();
		}
		
		getContainer().cancelReopenTimer();
		getContainer().displayMenu(getContainer().getViewer());
	}
	
	@Override
	public ItemStack onShiftRightClick() {
		module.removeEffect(name);
		getContainer().removeItem(getSlot());
		return null;
	}
}
