package au.com.addstar.minigames.extras.effects;

import org.bukkit.Bukkit;

import au.com.addstar.minigames.extras.effects.actions.AttachEffectAction;
import au.com.addstar.minigames.extras.effects.actions.DetachEffectAction;
import au.com.addstar.minigames.extras.effects.actions.PlayEffectAction;
import au.com.addstar.minigames.extras.effects.actions.StopEffectAction;
import au.com.addstar.monolith.effects.emitters.EmitterManager;
import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigamesregions.Main;
import au.com.mineauz.minigamesregions.actions.Actions;

public final class Effects {
	private Effects() {}
	
	private static EmitterManager manager;
	
	public static void initialize(Minigames minigames, Main regions) {
		minigames.mdata.addModule(EffectModule.class);
		Actions.addAction("ATTACH_EFFECT", AttachEffectAction.class);
		Actions.addAction("PLAY_EFFECT", PlayEffectAction.class);
		Actions.addAction("STOP_EFFECT", StopEffectAction.class);
		Actions.addAction("DETACH_EFFECT", DetachEffectAction.class);
		Bukkit.getPluginManager().registerEvents(new EffectListener(), minigames);
		
		manager = new EmitterManager(minigames);
		manager.launchTickTask();
	}
	
	public static EmitterManager getEmitters() {
		return manager;
	}
}
