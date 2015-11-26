package au.com.addstar.minigames.extras.effects.actions;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigamesregions.Node;
import au.com.mineauz.minigamesregions.Region;
import au.com.mineauz.minigamesregions.actions.ActionInterface;

public class AttachEffectAction extends ActionInterface {
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
		
	}

	@Override
	public boolean useInRegions() {
		return false;
	}

	@Override
	public boolean useInNodes() {
		return false;
	}

	@Override
	public void executeRegionAction(MinigamePlayer player, Region region) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeNodeAction(MinigamePlayer player, Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveArguments(FileConfiguration config, String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadArguments(FileConfiguration config, String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean displayMenu(MinigamePlayer player, Menu previous) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
