package au.com.addstar.minigames.extras.visibility;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.events.JoinMinigameEvent;
import au.com.mineauz.minigames.events.QuitMinigameEvent;
import au.com.mineauz.minigames.gametypes.MinigameType;
import au.com.mineauz.minigames.minigame.Minigame;

class VisibilityListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerJoinMinigame(JoinMinigameEvent event) {
		Minigame minigame = event.getMinigame();
		if (minigame.getType() != MinigameType.SINGLEPLAYER) {
			return;
		}
		
		Player thisPlayer = event.getPlayer();
		VisibilityModule module = (VisibilityModule)minigame.getModule(VisibilityModule.Name);
		if (module.getHidePlayers()) {
			// Hide players from eachother
			for (MinigamePlayer player : minigame.getPlayers()) {
				player.getPlayer().hidePlayer(thisPlayer);
				thisPlayer.hidePlayer(player.getPlayer());
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerLeaveMinigame(QuitMinigameEvent event) {
		Minigame minigame = event.getMinigame();
		if (minigame.getType() != MinigameType.SINGLEPLAYER) {
			return;
		}
		
		Player thisPlayer = event.getPlayer();
		VisibilityModule module = (VisibilityModule)minigame.getModule(VisibilityModule.Name);
		if (module.getHidePlayers()) {
			// Show players to eachother
			for (MinigamePlayer player : minigame.getPlayers()) {
				player.getPlayer().showPlayer(thisPlayer);
				thisPlayer.showPlayer(player.getPlayer());
			}
		}
	}
}
