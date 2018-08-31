package au.com.addstar.minigames.extras.effects.actions;

import au.com.addstar.minigames.extras.effects.EffectModule;
import au.com.addstar.minigames.extras.effects.EmitterTemplate;
import au.com.addstar.monolith.attachments.Attachment;
import au.com.addstar.monolith.attachments.StaticAttachment;
import au.com.addstar.monolith.effects.BaseEffect;
import au.com.addstar.monolith.effects.emitters.Emitter;
import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.menu.*;
import au.com.mineauz.minigamesregions.Node;
import au.com.mineauz.minigamesregions.Region;
import au.com.mineauz.minigamesregions.actions.AbstractAction;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayEffectAction extends AbstractAction {
	private EmitterTemplate template;
	private boolean isPrivate;
	
	public PlayEffectAction() {
		template = new EmitterTemplate();
	}
	
	@Override
	public String getName() {
		return "PLAY_EFFECT";
	}

	@Override
	public String getCategory() {
		return "Effects";
	}

	@Override
	public void describe(Map<String, Object> out) {
		out.put("Effect", template.getEffect());
		out.put("Type", template.getType());
		out.put("Private", isPrivate);
	}

	@Override
	public boolean useInRegions() {
		return false;
	}

	@Override
	public boolean useInNodes() {
		return true;
	}

	@Override
	public void executeRegionAction(MinigamePlayer player, Region region) {
	}

	@Override
	public void executeNodeAction(MinigamePlayer player, Node node) {
		if (player == null) {
			// TODO: Remove this check if we can get the minigame without player
			return;
		}
		
		EffectModule module = EffectModule.getModule(player.getMinigame());
		Attachment attachment = new StaticAttachment(node.getLocation());
		
		BaseEffect effect = module.getEffect(template.getEffect());
		if (effect == null) {
			return;
		}
		
		Emitter emitter = template.create(attachment);
		emitter.setEffect(effect);
		
		if (isPrivate) {
			emitter.getViewers().add(player.getPlayer());
			module.addEmitter(emitter, player.getPlayer());
		} else {
			module.addEmitter(emitter);
		}
		
		module.setNodeEmitter(node, emitter);
		
		// Play the effect
		emitter.start();
	}

	@Override
	public void saveArguments(FileConfiguration config, String path) {
		ConfigurationSection section = config.createSection(path);
		section.set("private", isPrivate);
		template.save(section.createSection("template"));
	}

	@Override
	public void loadArguments(FileConfiguration config, String path) {
		ConfigurationSection section = config.getConfigurationSection(path);
		if (section == null) {
			return;
		}
		
		isPrivate = section.getBoolean("isPrivate", false);
		if (section.isConfigurationSection("template")) {
			template.load(section.getConfigurationSection("template"));
		}
	}

	@Override
	public boolean displayMenu(final MinigamePlayer player, final Menu previous) {
		final Menu menu = new Menu(6, "Play Effect", player);
		
		menu.addItem(new MenuItem("Emitter", Material.CAULDRON_ITEM) {
			@Override
			public ItemStack onClick() {
				template.displayMenu(player, menu);
				return null;
			}
		});
		
		menu.addItem(new MenuItemBoolean("Private Viewing", MinigameUtils.stringToList("NOTE: Some effect types may;not support private viewing"), Material.LEVER, new Callback<Boolean>() {
			@Override
			public void setValue(Boolean value) {
				isPrivate = value;
			}
			
			@Override
			public Boolean getValue() {
				return isPrivate;
			}
		}));
		
		menu.addItem(new MenuItemBack(previous), menu.getSize() - 9);
		menu.displayMenu(player);
		return true;
	}

}
