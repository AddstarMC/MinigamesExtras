package au.com.addstar.minigames.extras.effects;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import au.com.mineauz.minigames.events.EndMinigameEvent;
import au.com.mineauz.minigames.events.QuitMinigameEvent;
import au.com.mineauz.minigames.gametypes.MinigameType;
import au.com.mineauz.minigames.minigame.Minigame;

public class EffectListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onMinigameEnd(EndMinigameEvent event) {
		if (event.getMinigame().getType() == MinigameType.MULTIPLAYER) {
			onEnd(event.getMinigame());
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onMinigameQuit(QuitMinigameEvent event) {
		EffectModule module = EffectModule.getModule(event.getMinigame());
		module.removeEmitters(event.getPlayer());
		if (event.getMinigame().getType() == MinigameType.SINGLEPLAYER && event.getMinigame().getPlayers().size() == 1) {
			onEnd(event.getMinigame());
		}
	}
	
	private void onEnd(Minigame minigame) {
		EffectModule module = EffectModule.getModule(minigame);
		module.removeAllEmitters();
	}
}
