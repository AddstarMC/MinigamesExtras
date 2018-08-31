package au.com.addstar.minigames.extras.effects;

import au.com.addstar.minigames.extras.effects.menu.MenuItemAddEffect;
import au.com.addstar.minigames.extras.effects.menu.MenuItemEffect;
import au.com.addstar.monolith.effects.BaseEffect;
import au.com.addstar.monolith.effects.emitters.ContinuousEmitter;
import au.com.addstar.monolith.effects.emitters.Emitter;
import au.com.mineauz.minigames.config.Flag;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;
import au.com.mineauz.minigames.menu.MenuItemBack;
import au.com.mineauz.minigames.minigame.Minigame;
import au.com.mineauz.minigames.minigame.MinigameState;
import au.com.mineauz.minigames.minigame.modules.MinigameModule;
import au.com.mineauz.minigamesregions.Node;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EffectModule extends MinigameModule {
	private static final String NAME = "EFFECTS";
	
	private final Map<String, BaseEffect> effects;
	
	private final Set<Emitter> sessionEmitters;
	private final SetMultimap<Player, Emitter> playerEmitters;
	
	private final Map<Node, Emitter> nodeEmitters;
	
	public EffectModule(Minigame mgm) {
		super(mgm);
		
		effects = Maps.newHashMap();
		nodeEmitters = Maps.newIdentityHashMap();
		sessionEmitters = Collections.newSetFromMap(Maps.<Emitter, Boolean>newIdentityHashMap());
		playerEmitters = Multimaps.newSetMultimap(Maps.<Player, Collection<Emitter>>newHashMap(), () -> Collections.newSetFromMap(Maps.newIdentityHashMap()));
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
	
	/**
	 * Registers a new emitter with this minigame. The minigame must have players in it
	 * @param emitter The emitter to add
	 */
	public void addEmitter(Emitter emitter) {
		Preconditions.checkNotNull(emitter);
		Preconditions.checkState(getMinigame().getState() != MinigameState.IDLE);
		
		if (emitter instanceof ContinuousEmitter) {
			sessionEmitters.add(emitter);
		}
		Effects.getEmitters().addEmitter(emitter);
	}
	
	/**
	 * Registers a new emitter with this minigame belonging to a player. The minigame must have players in it
	 * @param emitter The emitter to add
	 * @param owner The owner of the emitter
	 */
	public void addEmitter(Emitter emitter, Player owner) {
		Preconditions.checkNotNull(emitter);
		Preconditions.checkNotNull(owner);
		Preconditions.checkState(getMinigame().getState() != MinigameState.IDLE);
		
		if (emitter instanceof ContinuousEmitter) {
			sessionEmitters.add(emitter);
			playerEmitters.put(owner, emitter);
		}
		
		Effects.getEmitters().addEmitter(emitter);
	}
	
	/**
	 * Sets the emitter tied to a node. This is just for reference. 
	 * You must still register the emitter with {@link #addEmitter(Emitter)} 
	 * or {@link #addEffect(String, BaseEffect)}.
	 * If there is an existing emitter, it will be unregistered
	 * @param node The node to set emitter on
	 * @param emitter The emitter to set
	 */
	public void setNodeEmitter(Node node, Emitter emitter) {
		Emitter existing = nodeEmitters.put(node, emitter);
		if (existing != null) {
			existing.stop();
			sessionEmitters.remove(existing);
			Effects.getEmitters().removeEmitter(existing);
		}
	}
	
	public Emitter getNodeEmitter(Node node) {
		return nodeEmitters.get(node);
	}
	
	/**
	 * Removes all emitters owned by a player
	 * @param player The player that owns them
	 */
	public void removeEmitters(Player player) {
		Set<Emitter> emitters = playerEmitters.removeAll(player);
		sessionEmitters.removeAll(emitters);
		for (Emitter emitter : emitters) {
			Effects.getEmitters().removeEmitter(emitter);
		}
	}
	
	/**
	 * Removes all emitters
	 */
	public void removeAllEmitters() {
		playerEmitters.clear();
		for (Emitter emitter : sessionEmitters) {
			Effects.getEmitters().removeEmitter(emitter);
		}
		nodeEmitters.clear();
		sessionEmitters.clear();
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
		} catch (InstantiationException | IllegalAccessException e) {
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
