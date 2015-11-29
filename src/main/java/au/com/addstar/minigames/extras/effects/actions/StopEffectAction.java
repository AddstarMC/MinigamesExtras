package au.com.addstar.minigames.extras.effects.actions;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import au.com.addstar.minigames.extras.effects.EffectModule;
import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigamesregions.Node;
import au.com.mineauz.minigamesregions.Region;
import au.com.mineauz.minigamesregions.actions.ActionInterface;

public class StopEffectAction extends ActionInterface {
	@Override
	public String getName() {
		return "STOP_EFFECT";
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
		module.setNodeEmitter(node, null);
	}

	@Override
	public void saveArguments(FileConfiguration config, String path) {
	}

	@Override
	public void loadArguments(FileConfiguration config, String path) {
	}

	@Override
	public boolean displayMenu(final MinigamePlayer player, final Menu previous) {
		return false;
	}

}
