package au.com.addstar.minigames.extras.effects;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import au.com.addstar.minigames.extras.effects.menu.MenuItemParticleType;
import au.com.addstar.minigames.extras.effects.menu.MenuItemVector;
import au.com.addstar.monolith.effects.BaseEffect;
import au.com.addstar.monolith.effects.EffectParticle;
import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;
import au.com.mineauz.minigames.menu.MenuItemBack;
import au.com.mineauz.minigames.menu.MenuItemDecimal;
import au.com.mineauz.minigames.menu.MenuItemInteger;

public final class EffectMenus {
	private EffectMenus() {}
	
	public static Menu effectEditMenu(Menu parent, BaseEffect effect) {
		if (effect instanceof EffectParticle) {
			return EffectMenus.particleEffectEditMenu(parent, (EffectParticle)effect);
		} else {
			throw new AssertionError();
		}
	}
	
	public static Menu particleEffectEditMenu(final Menu parent, final EffectParticle effect) {
		final Menu menu = new Menu(6, "Edit effect", parent.getViewer());
		
		// Set the type
		menu.addItem(new MenuItemParticleType("Set Type", Material.PAPER, effect));
		
		menu.addItem(new MenuItemVector("Range", Material.FENCE, effect.getRange(), 0.1f));
		menu.addItem(new MenuItemDecimal("Speed", Material.MINECART, new Callback<Double>() {
			@Override
			public void setValue(Double value) {
				effect.setSpeed(value.floatValue());
			}
			
			@Override
			public Double getValue() {
				return (double)effect.getSpeed();
			}
		}, 0.1, 1.0, 0.0, null));
		
		menu.addItem(new MenuItemInteger("Particle Count", Material.PAPER, new Callback<Integer>() {
			@Override
			public void setValue(Integer value) {
				effect.setCount(value.intValue());
			}
			
			@Override
			public Integer getValue() {
				return effect.getCount();
			}
		}, 1, null));
		
		// TODO: Color edit
		
		menu.addItem(new MenuItemBack(parent), menu.getSize() - 9);
		return menu;
	}
	
	public static Menu selectEffect(final Menu parent, EffectModule module, final Callback<String> callback) {
		Menu menu = new Menu(6, "Select Effect", parent.getViewer());
		
		for (final String key : module.getEffects().keySet()) {
			menu.addItem(new MenuItem(WordUtils.capitalizeFully(key), Material.FIREWORK_CHARGE) {
				@Override
				public ItemStack onClick() {
					callback.setValue(key);
					parent.displayMenu(parent.getViewer());
					return null;
				}
			});
		}
		
		menu.addItem(new MenuItemBack(parent), menu.getSize() - 9);
		return menu;
	}
}
