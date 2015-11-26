package au.com.addstar.minigames.extras.effects;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import au.com.addstar.minigames.extras.effects.menu.MenuItemAddEffect;
import au.com.addstar.minigames.extras.effects.menu.MenuItemEffect;
import au.com.addstar.monolith.effects.BaseEffect;
import au.com.mineauz.minigames.config.Flag;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;
import au.com.mineauz.minigames.menu.MenuItemBack;
import au.com.mineauz.minigames.minigame.Minigame;
import au.com.mineauz.minigames.minigame.modules.MinigameModule;

public class EffectModule extends MinigameModule {
	private static final String NAME = "EFFECTS";
	
	private final Map<String, BaseEffect> effects;
	
	public EffectModule(Minigame mgm) {
		super(mgm);
		
		effects = Maps.newHashMap();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Map<String, Flag<?>> getFlags() {
		return null;
	}
	
	public BaseEffect getEffect(String name) {
		return effects.get(name.toLowerCase());
	}
	
	public void addEffect(String name, BaseEffect effect) throws IllegalArgumentException {
		Preconditions.checkArgument(!effects.containsKey(name.toLowerCase()));
		effects.put(name.toLowerCase(), effect);
	}
	
	public void removeEffect(String name) {
		effects.remove(name.toLowerCase());
	}
	
	public Map<String, BaseEffect> getEffects() {
		return Collections.unmodifiableMap(effects);
	}

	@Override
	public boolean useSeparateConfig() {
		return true;
	}

	@Override
	public void save(FileConfiguration config) {
		ConfigurationSection root = config.createSection(getMinigame() + "." + getName());
		
		ConfigurationSection effectSection = root.createSection("effects");
		for (Entry<String, BaseEffect> effect : effects.entrySet()) {
			ConfigurationSection dest = effectSection.createSection(effect.getKey());
			dest.set("=", effect.getValue().getClass().getName());
			effect.getValue().save(dest);
		}
	}

	@Override
	public void load(FileConfiguration config) {
		ConfigurationSection root = config.getConfigurationSection(getMinigame() + "." + getName());
		if (root == null) {
			return;
		}
		
		ConfigurationSection effectSection = root.getConfigurationSection("effects");
		effects.clear();
		
		if (effectSection != null) {
			for (String key : effectSection.getKeys(false)) {
				ConfigurationSection section = effectSection.getConfigurationSection(key);
				
				BaseEffect effect = createEffect(section.getString("="));
				if (effect == null) {
					continue;
				}
				effect.load(section);
				effects.put(key.toLowerCase(), effect);
			}
		}
	}
	
	private BaseEffect createEffect(String type) {
		try {
			Class<?> rawClass = Class.forName(type);
			if (!BaseEffect.class.isAssignableFrom(rawClass)) {
				return null;
			}
			
			Class<? extends BaseEffect> clazz = rawClass.asSubclass(BaseEffect.class);
			return clazz.newInstance();
		} catch (ClassNotFoundException e) {
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void addEditMenuOptions(Menu editMenu) {
		editMenu.addItem(new MenuItem("Effects", Material.FIREWORK_CHARGE) {
			@Override
			public ItemStack onClick() {
				displayMenu(getContainer());
				return null;
			}
		});
	}
	
	private void displayMenu(Menu parent) {
		Menu moduleMenu = new Menu(6, "Effects", parent.getViewer());
		
		for (Entry<String, BaseEffect> effect : effects.entrySet()) {
			moduleMenu.addItem(new MenuItemEffect(effect.getKey(), effect.getValue(), this));
		}
		
		moduleMenu.addItem(new MenuItemAddEffect("Add Effect", Material.ITEM_FRAME, this), moduleMenu.getSize() - 1);
		moduleMenu.addItem(new MenuItemBack(parent), moduleMenu.getSize() - 9);
		
		moduleMenu.displayMenu(parent.getViewer());
	}

	@Override
	public boolean displayMechanicSettings(Menu previous) {
		return false;
	}

	public static EffectModule getModule(Minigame minigame) {
		return (EffectModule)minigame.getModule(NAME);
	}
}
