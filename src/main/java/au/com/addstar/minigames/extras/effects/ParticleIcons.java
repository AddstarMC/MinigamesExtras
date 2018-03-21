package au.com.addstar.minigames.extras.effects;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public final class ParticleIcons {
	private ParticleIcons() {}

    public static ItemStack getIcon(Particle effect) {

		
		Material type;
		short data = 0;
		
		switch (effect) {
		case FIREWORKS_SPARK:
			type = Material.FIREWORK;
			break;
		case CRIT:
			type = Material.DIAMOND_SWORD;
			break;
            case CRIT_MAGIC:
            case SPELL_MOB:
            case SPELL_MOB_AMBIENT:
		case SPELL:
			type = Material.POTION;
			data = 64;
			break;
            case SPELL_INSTANT:
			type = Material.POTION;
			data = 16421;
			break;
            case SPELL_WITCH:
			type = Material.MONSTER_EGG;
			data = 66;
			break;
		case NOTE:
			type = Material.NOTE_BLOCK;
			break;
		case PORTAL:
			type = Material.OBSIDIAN;
			break;
            case ENCHANTMENT_TABLE:
			type = Material.ENCHANTMENT_TABLE;
			break;
		case FLAME:
			type = Material.FLINT_AND_STEEL;
			break;
            case LAVA:
            case DRIP_LAVA:
			type = Material.LAVA_BUCKET;
			break;
		case FOOTSTEP:
			type = Material.DIAMOND_BOOTS;
			break;
            case WATER_SPLASH:
			type = Material.BOAT;
			break;
            case EXPLOSION_NORMAL:
		case EXPLOSION_LARGE:
		case EXPLOSION_HUGE:
			type = Material.TNT;
			break;
            case REDSTONE:
			type = Material.REDSTONE;
			break;
            case SNOWBALL:
			type = Material.SNOW_BALL;
			break;
            case WATER_DROP:
			type = Material.WATER_BUCKET;
			break;
		case SNOW_SHOVEL:
			type = Material.SNOW;
			break;
		case SLIME:
			type = Material.SLIME_BALL;
			break;
		case HEART:
			type = Material.GOLDEN_APPLE;
			break;
            case VILLAGER_HAPPY:
			type = Material.INK_SACK;
			data = 15;
			break;
            case SMOKE_NORMAL:
            case SMOKE_LARGE:
		case CLOUD:
            case VILLAGER_ANGRY:
            case TOWN_AURA:
            case SUSPENDED_DEPTH:
			type = Material.PAPER;
			break;
		default:
			return null;
		}
		
		return new ItemStack(type, 1, data);
	}
}
