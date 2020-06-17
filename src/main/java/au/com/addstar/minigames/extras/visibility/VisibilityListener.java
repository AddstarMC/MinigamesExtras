package au.com.addstar.minigames.extras.visibility;

import au.com.mineauz.minigames.objects.MinigamePlayer;
import au.com.mineauz.minigames.events.JoinMinigameEvent;
import au.com.mineauz.minigames.events.QuitMinigameEvent;
import au.com.mineauz.minigames.gametypes.MinigameType;
import au.com.mineauz.minigames.minigame.Minigame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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
            // Hide players from each other
			for (MinigamePlayer player : minigame.getPlayers()) {
                player.getPlayer().hidePlayer(Visibility.plugin, thisPlayer);
                thisPlayer.hidePlayer(Visibility.plugin, player.getPlayer());
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
            // Show players to each other
			for (MinigamePlayer player : minigame.getPlayers()) {
                player.getPlayer().showPlayer(Visibility.plugin, thisPlayer);
                thisPlayer.showPlayer(Visibility.plugin, player.getPlayer());
			}
		}
	}
}
