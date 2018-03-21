package au.com.addstar.minigames.extras.disguises;

import me.libraryaddict.disguise.disguisetypes.*;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

class StoredDisguise {
	private final DisguiseType type;

	// Other information
	private MaterialData block;
	private String playerName;

	private StoredDisguise(DisguiseType type) {
		this.type = type;
	}

	public DisguiseType getType() {
		return type;
	}

	public MaterialData getBlockData() {
		return block;
	}

	public String getPlayerName() {
		return playerName;
	}
	
	public Disguise createDisguise() {
		switch (type) {
		case PLAYER:
			return new PlayerDisguise(playerName);
		case FALLING_BLOCK:
			return new MiscDisguise(DisguiseType.FALLING_BLOCK, block.getItemTypeId(), block.getData());
		default:
			if (type.isMob()) {
				return new MobDisguise(type, true);
			} else {
				return new MiscDisguise(type);
			}
		}
	}
	
	public void save(ConfigurationSection section) {
		section.set("type", type.name());
		if (type == DisguiseType.PLAYER) {
			section.set("player", playerName);
		} else if (type == DisguiseType.FALLING_BLOCK) {
			section.set("block", block.getItemType().name());
			section.set("block-data", block.getData());
		}
	}
	
	private void load0(ConfigurationSection section) {
		if (type == DisguiseType.PLAYER) {
			playerName = section.getString("player");
		} else if (type == DisguiseType.FALLING_BLOCK) {
			Material blockType = Material.valueOf(section.getString("block"));
			block = blockType.getNewData((byte)section.getInt("block-data"));
		}
	}

	public static StoredDisguise playerDisguise(String player) {
		StoredDisguise disguise = new StoredDisguise(DisguiseType.PLAYER);
		disguise.playerName = player;

		return disguise;
	}

	public static StoredDisguise entityDisguise(EntityType entity) {
        return new StoredDisguise(DisguiseType.getType(entity));
	}

	public static StoredDisguise disguise(DisguiseType type) {
        return new StoredDisguise(type);
	}
	
	public static StoredDisguise load(ConfigurationSection section) {
		DisguiseType type = DisguiseType.valueOf(section.getString("type"));
		StoredDisguise disguise = new StoredDisguise(type);
		disguise.load0(section);
		
		return disguise;
	}

	public static StoredDisguise blockDisguise(Material material, int data) {
		StoredDisguise disguise = new StoredDisguise(DisguiseType.FALLING_BLOCK);
		disguise.block = material.getNewData((byte) data);
		return disguise;
	}

	@Override
	public String toString() {
		switch (type) {
		case FALLING_BLOCK:
			return "Block: " + block;
		case PLAYER:
			return "Player: " + playerName;
		default:
			return type.name();
		}
	}
}
