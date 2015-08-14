package au.com.addstar.minigames.extras.disguises;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import au.com.addstar.minigames.extras.BasicFlag;
import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.config.BooleanFlag;
import au.com.mineauz.minigames.config.EnumFlag;
import au.com.mineauz.minigames.menu.Callback;

import com.google.common.base.Preconditions;

class DisguiseSettings {
	private BasicFlag<StoredDisguise> disguise;
	private BasicFlag<StoredDisguise> alternate;
	
	private EnumFlag<ShowSetting> showDisguiseSelf;
	
	private BooleanFlag showPlayerName;
	
	public DisguiseSettings() {
		disguise = new BasicFlag<StoredDisguise>();
		alternate = new BasicFlag<StoredDisguise>();
		showPlayerName = new BooleanFlag(true, "");
		
		// Default values
		showDisguiseSelf = new EnumFlag<ShowSetting>(ShowSetting.Hide, "");
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
			secondary.setKeepDisguiseOnPlayerLogout(false);
			secondary.setHideArmorFromSelf(false);
			secondary.setHideHeldItemFromSelf(false);
			showDisguiseName(player.getPlayer(), secondary, getShowPlayerName());
		}
		
		primary.setKeepDisguiseOnPlayerLogout(false);
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
		
		DisguiseAPI.disguiseEntity(player.getPlayer(), primary);
		if (secondary != null) {
			DisguiseAPI.disguiseToPlayers(player.getPlayer(), secondary, player.getPlayer());
		}
	}
	
	private void showDisguiseName(Player player, Disguise disguise, boolean show) {
		disguise.setShowName(show);
		
		if (show) {
            if (disguise.getWatcher() instanceof LivingWatcher) {
                ((LivingWatcher) disguise.getWatcher()).setCustomName(player.getDisplayName());
                ((LivingWatcher) disguise.getWatcher()).setCustomNameVisible(true);
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
