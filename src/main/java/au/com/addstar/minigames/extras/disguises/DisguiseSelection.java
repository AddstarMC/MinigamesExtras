package au.com.addstar.minigames.extras.disguises;

import au.com.addstar.minigames.extras.MenuItemEnum;
import au.com.addstar.minigames.extras.disguises.DisguiseSettings.ShowSetting;
import au.com.mineauz.minigames.menu.*;
import au.com.mineauz.minigames.objects.MinigamePlayer;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DisguiseSelection {
	public static Menu createDisguiseSelectMenu(final Menu previous, final MinigamePlayer viewer, final Callback<StoredDisguise> callback) {
		Menu menu = new Menu(6, "Select Disguise", viewer);
		
		menu.addItem(new MenuItemDisguise(DisguiseType.PLAYER, previous, callback)); // TODO: Allow player selection: name, random in game, random in team, random in other team
		menu.addItem(new MenuItem("Block", Material.STONE) {
			@Override
			public ItemStack onClickWithItem(ItemStack item) {
				if (item.getType().isBlock()) {
					callback.setValue(StoredDisguise.blockDisguise(item.getType()));
					previous.displayMenu(viewer);
				}
				
				return super.onClickWithItem(item);
			}
		});
		menu.addItem(new MenuItemPage("Creatures", Material.CHICKEN_SPAWN_EGG, createEntitiesMenu(menu, previous, viewer, callback)));
		menu.addItem(new MenuItemPage("Misc", Material.CREEPER_SPAWN_EGG, createMiscMenu(menu, previous, viewer, callback)));
		
		menu.addItem(new MenuItemBack(previous), menu.getSize()-9);
		
		return menu;
	}
	
	private static Menu createEntitiesMenu(Menu previous, Menu parent, MinigamePlayer viewer, Callback<StoredDisguise> callback) {
		Menu menu = new Menu(6, "Select Creature", viewer);
		
		for (final DisguiseType type : DisguiseType.values()) {
			if (!type.isMob())
				continue;
			
			menu.addItem(new MenuItemDisguise(type, parent, callback));
		}
		
		menu.addItem(new MenuItemBack(previous), menu.getSize()-9);
		
		return menu;
	}
	
	private static Menu createMiscMenu(Menu previous, Menu parent, MinigamePlayer viewer, Callback<StoredDisguise> callback) {
		Menu menu = new Menu(6, "Select Disguise", viewer);
		
		menu.addItem(new MenuItemDisguise(DisguiseType.BOAT, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.ENDER_SIGNAL, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.FIREBALL, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.SMALL_FIREBALL, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.ITEM_FRAME, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.MINECART, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.MINECART_CHEST, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.MINECART_COMMAND, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.MINECART_FURNACE, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.MINECART_HOPPER, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.MINECART_TNT, parent, callback));
		menu.addItem(new MenuItemDisguise(DisguiseType.PAINTING, parent, callback));
		
		menu.addItem(new MenuItemBack(previous), menu.getSize()-9);
		
		return menu;
	}
	
	private static ItemStack getDisplayItem(DisguiseType type) {
		Material itemType;
        ItemStack item;
		switch (type) {
		case ARMOR_STAND:
			itemType = Material.ARMOR_STAND;
			break;
		case ARROW:
			itemType = Material.ARROW;
			break;
		case BOAT:
			itemType = Material.OAK_BOAT;
			break;
		case EGG:
			itemType = Material.EGG;
			break;
		case ENDER_PEARL:
			itemType = Material.ENDER_PEARL;
			break;
		case ENDER_SIGNAL:
			itemType = Material.ENDER_EYE;
			break;
		case EXPERIENCE_ORB:
		case THROWN_EXP_BOTTLE:
			itemType = Material.EXPERIENCE_BOTTLE;
			break;
		case FALLING_BLOCK:
			itemType = Material.STONE;
			break;
		case FIREBALL:
		case SMALL_FIREBALL:
			itemType = Material.FIRE_CHARGE;
			break;
		case FIREWORK:
			itemType = Material.FIREWORK_ROCKET;
			break;
		case ITEM_FRAME:
			itemType = Material.ITEM_FRAME;
			break;
		case MINECART:
			itemType = Material.MINECART;
			break;
		case MINECART_CHEST:
			itemType = Material.CHEST_MINECART;
			break;
		case MINECART_COMMAND:
			itemType = Material.COMMAND_BLOCK_MINECART;
			break;
		case MINECART_FURNACE:
			itemType = Material.FURNACE_MINECART;
			break;
		case MINECART_HOPPER:
			itemType = Material.HOPPER_MINECART;
			break;
		case MINECART_TNT:
			itemType = Material.TNT_MINECART;
			break;
		case MINECART_MOB_SPAWNER:
			itemType = Material.MINECART;
			break;
		case FISHING_HOOK:
			itemType = Material.FISHING_ROD;
			break;
		case LEASH_HITCH:
			itemType = Material.LEAD;
			break;
		case PAINTING:
			itemType = Material.PAINTING;
			break;
		case PRIMED_TNT:
			itemType = Material.TNT;
			break;
		case SNOWBALL:
			itemType = Material.SNOWBALL;
			break;
		case SPLASH_POTION:
			itemType = Material.POTION;
			break;
		// Living ents
		case BLAZE:
			itemType = Material.BLAZE_ROD;
			break;
		case CAVE_SPIDER:
		case SPIDER:
			itemType = Material.STRING;
			break;
		case CHICKEN:
			itemType = Material.FEATHER;
			break;
		case COW:
			itemType = Material.LEATHER;
			break;
		case CREEPER:
			itemType = Material.GUNPOWDER;
			break;
		case ELDER_GUARDIAN:
		case GUARDIAN:
			itemType = Material.PRISMARINE_SHARD;
			break;
		case ENDER_DRAGON:
			itemType = Material.DRAGON_EGG;
			break;
		case ENDERMAN:
			itemType = Material.ENDER_PEARL;
			break;
		case GHAST:
			itemType = Material.GHAST_TEAR;
			break;
		case HORSE:
			itemType = Material.IRON_HORSE_ARMOR;
			break;
		case IRON_GOLEM:
			itemType = Material.IRON_INGOT;
			break;
		case MAGMA_CUBE:
			itemType = Material.MAGMA_CREAM;
			break;
		case MUSHROOM_COW:
			itemType = Material.MUSHROOM_STEW;
			break;
		case OCELOT:
			itemType = Material.SALMON;
			break;
		case PIG:
			itemType = Material.PORKCHOP;
			break;
		case PIG_ZOMBIE:
			itemType = Material.GOLD_NUGGET;
			break;
		case RABBIT:
			itemType = Material.RABBIT_FOOT;
			break;
		case SHEEP:
			itemType = Material.WHITE_WOOL;
			break;
		case SKELETON:
			itemType = Material.BONE;
			break;
		case SLIME:
			itemType = Material.SLIME_BALL;
			break;
		case SQUID:
			itemType = Material.INK_SAC;
			break;
		case VILLAGER:
			itemType = Material.EMERALD;
			break;
		case WITCH:
			itemType = Material.POTION;
			break;
		case WITHER:
			itemType = Material.WITHER_SKELETON_SKULL;
			break;
		case ZOMBIE:
			itemType = Material.ROTTEN_FLESH;
			break;
		case PLAYER:
			itemType = Material.PLAYER_HEAD;
			break;
		default:
			if (type.isMob()) {
				// entity ids can actually go higher than this :/
				String name = type.getEntityClass().getSimpleName();
				itemType = Material.matchMaterial(name + "_SPAWN_EGG");
				if (itemType == null) {
					itemType = Material.CHICKEN_SPAWN_EGG;
				}
			}
			else
				itemType = Material.BARRIER;
		}
        item = new ItemStack(itemType,1);
        return item;
	}
	
	private static String getDisguiseName(DisguiseType type) {
		return WordUtils.capitalizeFully(type.name().replace("_", " "));
	}
	
	public static Menu createEditMenu(Menu previous, MinigamePlayer player, DisguiseSettings settings) {
		Menu subMenu = new Menu(6, "Disguise Settings", player);
		
		subMenu.addItem(new MenuItemEnum<>("Disguise To Self", Material.ARMOR_STAND, settings.getShowDisguiseSelfCallback(), ShowSetting.class));
		subMenu.addItem(new MenuItemEnum<>("Disguise to Team", Material.BREWING_STAND,settings.getShowDisguiseTeamCallback(),ShowSetting.class));
		subMenu.addItem(new MenuItemBoolean("Show Player Name", Material.LEVER, settings.getShowPlayerNameCallback()));
		
		subMenu.addItem(new MenuItemNewLine());
		
		subMenu.addItem(new MenuItemSelectDisguise("Primary Disguise", Material.PAINTING, settings.getPrimaryCallback()));
		subMenu.addItem(new MenuItemSelectDisguise("Secondary Disguise", Material.PAINTING, settings.getSecondaryCallback()));
		
		subMenu.addItem(new MenuItemBack(previous), subMenu.getSize()-9);
		return subMenu;
	}
	
	private static class MenuItemDisguise extends MenuItem {
		private final DisguiseType type;
		private final Callback<StoredDisguise> callback;
		private final Menu parent;
		
		public MenuItemDisguise(DisguiseType type, Menu parent, Callback<StoredDisguise> callback) {
			super(getDisguiseName(type), Material.OAK_WOOD);
			this.type = type;
			this.parent = parent;
			this.callback = callback;
			
			// Update visual
			setItem(getDisplayItem(type));
		}
		
		@Override
		public ItemStack onClick() {
			callback.setValue(StoredDisguise.disguise(type));
			
			parent.displayMenu(getContainer().getViewer());
			return null;
		}
	}
}
