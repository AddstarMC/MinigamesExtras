package au.com.addstar.minigames.extras.effects;

import org.bukkit.Effect;
import org.bukkit.Effect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class ParticleIcons {
	private ParticleIcons() {}
	
	public static ItemStack getIcon(Effect effect) {
		if (effect.getType() != Type.PARTICLE) {
			return null;
		}
		
		Material type;
		short data = 0;
		
		switch (effect) {
		case FIREWORKS_SPARK:
			type = Material.FIREWORK;
			break;
		case CRIT:
			type = Material.DIAMOND_SWORD;
			break;
		case MAGIC_CRIT:
		case POTION_SWIRL:
		case POTION_SWIRL_TRANSPARENT:
		case SPELL:
			type = Material.POTION;
			data = 64;
			break;
		case INSTANT_SPELL:
			type = Material.POTION;
			data = 16421;
			break;
		case WITCH_MAGIC:
			type = Material.MONSTER_EGG;
			data = 66;
			break;
		case NOTE:
			type = Material.NOTE_BLOCK;
			break;
		case PORTAL:
			type = Material.OBSIDIAN;
			break;
		case FLYING_GLYPH:
			type = Material.ENCHANTMENT_TABLE;
			break;
		case FLAME:
			type = Material.FLINT_AND_STEEL;
			break;
		case LAVA_POP:
		case LAVADRIP:
			type = Material.LAVA_BUCKET;
			break;
		case FOOTSTEP:
			type = Material.DIAMOND_BOOTS;
			break;
		case SPLASH:
			type = Material.BOAT;
			break;
		case EXPLOSION:
		case EXPLOSION_LARGE:
		case EXPLOSION_HUGE:
			type = Material.TNT;
			break;
		case COLOURED_DUST:
			type = Material.REDSTONE;
			break;
		case SNOWBALL_BREAK:
			type = Material.SNOW_BALL;
			break;
		case WATERDRIP:
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
		case HAPPY_VILLAGER:
			type = Material.INK_SACK;
			data = 15;
			break;
		case PARTICLE_SMOKE:
		case SMALL_SMOKE:
		case CLOUD:
		case VILLAGER_THUNDERCLOUD:
		case LARGE_SMOKE:
		case VOID_FOG:
			type = Material.PAPER;
			break;
		default:
			return null;
		}
		
		return new ItemStack(type, 1, data);
	}
}
