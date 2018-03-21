package au.com.addstar.minigames.extras.effects.menu;

import au.com.mineauz.minigames.MinigameMessageType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import au.com.addstar.minigames.extras.effects.EffectMenus;
import au.com.addstar.minigames.extras.effects.EffectModule;
import au.com.addstar.monolith.effects.BaseEffect;
import au.com.addstar.monolith.effects.EffectParticle;
import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;
import au.com.mineauz.minigames.menu.MenuItemBack;

public class MenuItemAddEffect extends MenuItem {
	private final EffectModule module;
	
	private String name;
	
	public MenuItemAddEffect(String name, Material displayItem, EffectModule module) {
		super(name, displayItem);
		this.module = module;
	}
	
	@Override
	public ItemStack onClick() {
		MinigamePlayer player = getContainer().getViewer();
		player.setNoClose(true);
		player.getPlayer().closeInventory();
		player.sendMessage("Enter the name of this effect. The menu will automatically reopen in 20s if nothing is entered.", null);
		
		player.setManualEntry(this);
		getContainer().startReopenTimer(20);
		return null;
	}
	
	@Override
	public void checkValidEntry(String entry) {
		getContainer().cancelReopenTimer();
		if (module.getEffect(entry) != null) {
			getContainer().getViewer().sendMessage("That effect already exists", MinigameMessageType.ERROR);
			getContainer().displayMenu(getContainer().getViewer());
		} else {
			name = entry;
			showTypeSelect();
		}
	}
	
	private void showTypeSelect() {
		Menu menu = new Menu(6, "Select Effect Type", getContainer().getViewer());
		
		menu.addItem(new MenuItem("Particle Effect", Material.FIREWORK_CHARGE) {
			@Override
			public ItemStack onClick() {
				showEditMenu(getContainer(), new EffectParticle());
				return null;
			}
		});
		
		menu.addItem(new MenuItemBack(getContainer()), menu.getSize() - 9);
		menu.displayMenu(getContainer().getViewer());
	}
	
	private void showEditMenu(Menu parent, final BaseEffect effect) {
		Menu menu = EffectMenus.effectEditMenu(parent, effect);
		
		menu.addItem(new MenuItem("Save and Add Effect", Material.ITEM_FRAME) {
			@Override
			public ItemStack onClick() {
				module.addEffect(name, effect);
				// Go back
				MenuItemAddEffect.this.getContainer().displayMenu(getContainer().getViewer());
				return null;
			}
		}, menu.getSize() - 1);
		
		menu.displayMenu(getContainer().getViewer());
	}
}
