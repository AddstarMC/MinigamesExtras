package au.com.addstar.minigames.extras.effects.actions;

import java.util.Map;

import au.com.mineauz.minigamesregions.actions.AbstractAction;
import org.bukkit.configuration.file.FileConfiguration;

import au.com.addstar.minigames.extras.effects.EffectModule;
import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigamesregions.Node;
import au.com.mineauz.minigamesregions.Region;
import au.com.mineauz.minigamesregions.actions.ActionInterface;

public class DetachEffectAction extends AbstractAction {
	@Override
	public String getName() {
		return "DETACH_EFFECT";
	}

	@Override
	public String getCategory() {
		return "Effects";
	}

	@Override
	public void describe(Map<String, Object> out) {
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
		module.removeEmitters(player.getPlayer());
	}
	

	@Override
	public void saveArguments(FileConfiguration config, String path) {
	}

	@Override
	public void loadArguments(FileConfiguration config, String path) {
	}

	@Override
	public boolean displayMenu(MinigamePlayer player, Menu previous) {
		return false;
	}

}
