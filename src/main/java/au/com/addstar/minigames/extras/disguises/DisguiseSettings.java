package au.com.addstar.minigames.extras.disguises;

import au.com.addstar.minigames.extras.BasicFlag;
import au.com.mineauz.minigames.objects.MinigamePlayer;
import au.com.mineauz.minigames.config.BooleanFlag;
import au.com.mineauz.minigames.config.EnumFlag;
import au.com.mineauz.minigames.menu.Callback;
import com.google.common.base.Preconditions;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

class DisguiseSettings {
	private BasicFlag<StoredDisguise> disguise;
	private BasicFlag<StoredDisguise> alternate;
	
	private EnumFlag<ShowSetting> showDisguiseSelf;
	

	
	private EnumFlag<ShowSetting> showDisguiseTeam;
 
	private BooleanFlag showPlayerName;
	
	public DisguiseSettings() {
        disguise = new BasicFlag<>();
        alternate = new BasicFlag<>();
		showPlayerName = new BooleanFlag(true, "");
		
		// Default values
        showDisguiseSelf = new EnumFlag<>(ShowSetting.Hide, "Show Disguise Self");
        showDisguiseTeam = new EnumFlag<>(ShowSetting.Hide, "Show Team Disguise");
	}
	
	// Primary disguise
	public StoredDisguise getPrimary() {
		return disguise.getFlag();
	}
	
	public void setPrimary(StoredDisguise disguise) {
		this.disguise.setFlag(disguise);
	}
	
	public Callback<StoredDisguise> getPrimaryCallback() {
		return disguise.getCallback();
	}
	
	// Secondary disguise
	public StoredDisguise getSecondary() {
		return alternate.getFlag();
	}
	
	public void setSecondary(StoredDisguise disguise) {
		alternate.setFlag(disguise);
	}
	
	public Callback<StoredDisguise> getSecondaryCallback() {
		return alternate.getCallback();
	}
	
	// ShowSelfDisguise
	public ShowSetting getShowDisguiseSelf() {
		return showDisguiseSelf.getFlag();
	}
	
	public void setShowDisguiseSelf(ShowSetting show) {
		Preconditions.checkNotNull(show);
		showDisguiseSelf.setFlag(show);
	}
	
	public ShowSetting getShowDisguiseTeam() {
		return showDisguiseTeam.getFlag();
	}
	
	public void setShowDisguiseTeam(ShowSetting setting) {
		Preconditions.checkNotNull(setting);
		this.showDisguiseTeam.setFlag(setting);
	}
	
	public Callback<ShowSetting> getShowDisguiseTeamCallback(){
		return showDisguiseTeam.getCallback();
	}
	
	public Callback<ShowSetting> getShowDisguiseSelfCallback() {
		return showDisguiseSelf.getCallback();
	}
	
	// ShowPlayerName
	public boolean getShowPlayerName() {
		return showPlayerName.getFlag();
	}
	
	public void setShowPlayerName(boolean show) {
		showPlayerName.setFlag(show);
	}
	
	public Callback<Boolean> getShowPlayerNameCallback() {
		return showPlayerName.getCallback();
	}
	
	
	public void enableDisguise(MinigamePlayer player) {
		if (disguise.getFlag() == null) {
			return;
		}
		
		// Make the disguises
		Disguise primary = disguise.getFlag().createDisguise();
		Disguise secondary = null;
		if (alternate.getFlag() != null) {
			secondary = alternate.getFlag().createDisguise();
			secondary.setHideArmorFromSelf(false);
			secondary.setHideHeldItemFromSelf(false);
			showDisguiseName(player.getPlayer(), secondary, getShowPlayerName());
		}
		
		primary.setHideArmorFromSelf(false);
		primary.setHideHeldItemFromSelf(false);
		showDisguiseName(player.getPlayer(), primary, getShowPlayerName());
		
		// Set the self visibility
		switch (getShowDisguiseSelf()) {
		case Hide:
			primary.setViewSelfDisguise(false);
			if (secondary != null) {
				secondary.setViewSelfDisguise(false);
			}
			break;
		case Primary:
			primary.setViewSelfDisguise(true);
			if (secondary != null) {
				secondary.setViewSelfDisguise(false);
			}
			break;
		case Secondary:
			primary.setViewSelfDisguise(false);
			if (secondary != null) {
				secondary.setViewSelfDisguise(true);
			}
			break;
		}
        List<Player> teamPlayerNotSeePrimary = new ArrayList<>();
        List<Player> teamPlayerSeeSecondary = new ArrayList<>();
        for (MinigamePlayer mp : player.getMinigame().getPlayers()) {
            if (mp.getTeam().equals(mp.getTeam())) {
                switch (getShowDisguiseTeam()) {
                    case Hide:
                        teamPlayerNotSeePrimary.add(mp.getPlayer());
                        break;
                    case Primary:
                        break;
                    case Secondary:
                        teamPlayerNotSeePrimary.add(mp.getPlayer());
                        teamPlayerSeeSecondary.add(mp.getPlayer());
                }
            }
        }


        DisguiseAPI.disguiseIgnorePlayers(player.getPlayer(), primary, teamPlayerNotSeePrimary);
		if (secondary != null) {
			DisguiseAPI.disguiseToPlayers(player.getPlayer(), secondary, player.getPlayer());
            if (teamPlayerSeeSecondary.size() > 0) {
                DisguiseAPI.disguiseToPlayers(player.getPlayer(), secondary, teamPlayerSeeSecondary);
            }
		}
	}
	
	private void showDisguiseName(Player player, Disguise disguise, boolean show) {
		disguise.setDynamicName(show);
		if (show) {
            if (disguise.getWatcher() instanceof LivingWatcher) {
                disguise.getWatcher().setCustomName(player.getDisplayName());
                disguise.getWatcher().setCustomNameVisible(true);
            }
        }
	}
	
	public void disableDisguise(MinigamePlayer player) {
		DisguiseAPI.undisguiseToAll(player.getPlayer());
	}
	
	public void save(ConfigurationSection section) {
		if (!showDisguiseSelf.getFlag().equals(showDisguiseSelf.getDefaultFlag())) {
			section.set("show-self", showDisguiseSelf.getFlag().name());
		}
  
		if (!showDisguiseTeam.getFlag().equals(showDisguiseTeam.getDefaultFlag())) {
            section.set("show-team", showDisguiseTeam.getFlag().name());
        }
		
		if (showPlayerName.getFlag() != showPlayerName.getDefaultFlag()) {
			section.set("show-name", showPlayerName.getFlag());
		}
		
		if (disguise.getFlag() != null) {
			disguise.getFlag().save(section.createSection("primary"));
		}
		
		if (alternate.getFlag() != null) {
			alternate.getFlag().save(section.createSection("secondary"));
		}
	}
	
	public void load(ConfigurationSection section) {
		if (section.contains("show-self")) {
			showDisguiseSelf.setFlag(ShowSetting.valueOf(section.getString("show-self")));
		}
        
        if (section.contains("show-team")) {
            showDisguiseTeam.setFlag(ShowSetting.valueOf(section.getString("show-team")));
        }
		
		if (section.contains("show-name")) {
			showPlayerName.setFlag(section.getBoolean("show-name"));
		}
		
		if (section.contains("primary")) {
			disguise.setFlag(StoredDisguise.load(section.getConfigurationSection("primary")));
		}
		
		if (section.contains("secondary")) {
			alternate.setFlag(StoredDisguise.load(section.getConfigurationSection("secondary")));
		}
	}
	
	public enum ShowSetting {
		Hide,
		Primary,
		Secondary
	}
}
