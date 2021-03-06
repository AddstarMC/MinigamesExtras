package au.com.addstar.minigames.extras.effects.actions;

import au.com.addstar.minigames.extras.effects.EffectModule;
import au.com.addstar.minigames.extras.effects.EmitterTemplate;
import au.com.addstar.minigames.extras.effects.menu.MenuItemVector;
import au.com.addstar.monolith.attachments.AttachmentFunctions;
import au.com.addstar.monolith.attachments.EntityAttachment;
import au.com.addstar.monolith.effects.BaseEffect;
import au.com.addstar.monolith.effects.emitters.Emitter;
import au.com.mineauz.minigames.objects.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.menu.*;
import au.com.mineauz.minigamesregions.Node;
import au.com.mineauz.minigamesregions.Region;
import au.com.mineauz.minigamesregions.actions.AbstractAction;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;

public class AttachEffectAction extends AbstractAction {
	private EmitterTemplate template;
	private Vector attachmentOffset;
	private boolean relativeToLook; 
	private boolean isPrivate;
	
	public AttachEffectAction() {
		template = new EmitterTemplate();
		attachmentOffset = new Vector();
		isPrivate = false;
	}
	
	@Override
	public String getName() {
		return "ATTACH_EFFECT";
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
		out.put("Offset", String.format("%.1f,%.1f,%.1f", attachmentOffset.getX(), attachmentOffset.getY(), attachmentOffset.getZ()));
		out.put("Relative", relativeToLook);
	}

	@Override
	public boolean useInRegions() {
		return true;
	}

	@Override
	public boolean useInNodes() {
		return true;
	}

	@Override
	public void executeRegionAction(MinigamePlayer player, Region region) {
		execute(player);
	}

	@Override
	public void executeNodeAction(MinigamePlayer player, Node node) {
		execute(player);
	}
	
	private void execute(MinigamePlayer player) {
		if (player == null) {
			return;
		}
		
		EffectModule module = EffectModule.getModule(player.getMinigame());
		
		BaseEffect effect = module.getEffect(template.getEffect());
		if (effect == null) {
			return;
		}
		
		EntityAttachment<Player> attachment;
		
		if (relativeToLook) {
            attachment = new EntityAttachment<>(
                    player.getPlayer(),
                    AttachmentFunctions.lookRelativeOffset(attachmentOffset.clone())
            );
		} else {
            attachment = new EntityAttachment<>(player.getPlayer(), attachmentOffset.clone());
		}
		
		Emitter emitter = template.create(attachment);
		emitter.setEffect(effect);
		
		if (isPrivate) {
			emitter.getViewers().add(player.getPlayer());
		}
		
		module.addEmitter(emitter, player.getPlayer());
		
		// Play the effect
		emitter.start();
	}

	@Override
	public void saveArguments(FileConfiguration config, String path) {
		ConfigurationSection section = config.createSection(path);
		section.set("offset", attachmentOffset);
		section.set("lookrel", relativeToLook);
		section.set("private", isPrivate);
		template.save(section.createSection("template"));
	}

	@Override
	public void loadArguments(FileConfiguration config, String path) {
		ConfigurationSection section = config.getConfigurationSection(path);
		if (section == null) {
			return;
		}
		
		attachmentOffset = section.getVector("offset", new Vector());
		relativeToLook = section.getBoolean("lookrel", false);
		isPrivate = section.getBoolean("isPrivate", false);
		if (section.isConfigurationSection("template")) {
			template.load(section.getConfigurationSection("template"));
		}
	}

	@Override
	public boolean displayMenu(final MinigamePlayer player, Menu previous) {
		final Menu menu = new Menu(6, "Attach Effect", player);
		
		menu.addItem(new MenuItem("Emitter", Material.CAULDRON) {
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
		
		menu.addItem(new MenuItemNewLine());
		menu.addItem(new MenuItemVector("Attachment Offset", Material.COMPARATOR, attachmentOffset, 0.1f));
		menu.addItem(new MenuItemBoolean("Relative to entity look", Material.LEVER, new Callback<Boolean>() {
			@Override
			public void setValue(Boolean value) {
				relativeToLook = value;
			}
			
			@Override
			public Boolean getValue() {
				return relativeToLook;
			}
		}));
		
		menu.addItem(new MenuItemBack(previous), menu.getSize() - 9);
		menu.displayMenu(player);
		return true;
	}
	
}
