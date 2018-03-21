package au.com.addstar.minigames.extras.disguises;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import au.com.addstar.minigames.extras.MenuItemEnum;
import au.com.addstar.minigames.extras.disguises.DisguiseSettings.ShowSetting;
import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;
import au.com.mineauz.minigames.menu.MenuItemBack;
import au.com.mineauz.minigames.menu.MenuItemBoolean;
import au.com.mineauz.minigames.menu.MenuItemNewLine;
import au.com.mineauz.minigames.menu.MenuItemPage;

public class DisguiseSelection {
	public static Menu createDisguiseSelectMenu(final Menu previous, final MinigamePlayer viewer, final Callback<StoredDisguise> callback) {
		Menu menu = new Menu(6, "Select Disguise", viewer);
		
		menu.addItem(new MenuItemDisguise(DisguiseType.PLAYER, previous, callback)); // TODO: Allow player selection: name, random in game, random in team, random in other team
		menu.addItem(new MenuItem("Block", Material.STONE) {
			@Override
			public ItemStack onClickWithItem(ItemStack item) {
				if (item.getType().isBlock()) {
					callback.setValue(StoredDisguise.blockDisguise(item.getType(), item.getDurability()));
					previous.displayMenu(viewer);
				}
				
				return super.onClickWithItem(item);
			}
		});
		menu.addItem(new MenuItemPage("Creatures", Material.MONSTER_EGG, createEntitiesMenu(menu, previous, viewer, callback)));
		menu.addItem(new MenuItemPage("Misc", Material.MONSTER_EGG, createMiscMenu(menu, previous, viewer, callback)));
		
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
		int data = 0;
		switch (type) {
		case ARMOR_STAND:
			itemType = Material.ARMOR_STAND;
			break;
		case ARROW:
			itemType = Material.ARROW;
			break;
		case BOAT:
			itemType = Material.BOAT;
			break;
		case EGG:
			itemType = Material.EGG;
			break;
		case ENDER_PEARL:
			itemType = Material.ENDER_PEARL;
			break;
		case ENDER_SIGNAL:
			itemType = Material.EYE_OF_ENDER;
			break;
		case EXPERIENCE_ORB:
		case THROWN_EXP_BOTTLE:
			itemType = Material.EXP_BOTTLE;
			break;
		case FALLING_BLOCK:
			itemType = Material.STONE;
			break;
		case FIREBALL:
		case SMALL_FIREBALL:
			itemType = Material.FIREBALL;
			break;
		case FIREWORK:
			itemType = Material.FIREWORK;
			break;
		case ITEM_FRAME:
			itemType = Material.ITEM_FRAME;
			break;
		case MINECART:
			itemType = Material.MINECART;
			break;
		case MINECART_CHEST:
			itemType = Material.STORAGE_MINECART;
			break;
		case MINECART_COMMAND:
			itemType = Material.COMMAND_MINECART;
			break;
		case MINECART_FURNACE:
			itemType = Material.POWERED_MINECART;
			break;
		case MINECART_HOPPER:
			itemType = Material.HOPPER_MINECART;
			break;
		case MINECART_TNT:
			itemType = Material.EXPLOSIVE_MINECART;
			break;
		case MINECART_MOB_SPAWNER:
			itemType = Material.MINECART;
			break;
		case FISHING_HOOK:
			itemType = Material.FISHING_ROD;
			break;
		case LEASH_HITCH:
			itemType = Material.LEASH;
			break;
		case PAINTING:
			itemType = Material.PAINTING;
			break;
		case PRIMED_TNT:
			itemType = Material.TNT;
			break;
		case SNOWBALL:
			itemType = Material.SNOW_BALL;
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
			itemType = Material.SULPHUR;
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
			itemType = Material.IRON_BARDING;
			break;
		case IRON_GOLEM:
			itemType = Material.IRON_INGOT;
			break;
		case MAGMA_CUBE:
			itemType = Material.MAGMA_CREAM;
			break;
		case MUSHROOM_COW:
			itemType = Material.MUSHROOM_SOUP;
			break;
		case OCELOT:
			itemType = Material.RAW_FISH;
			break;
		case PIG:
			itemType = Material.PORK;
			break;
		case PIG_ZOMBIE:
			itemType = Material.GOLD_NUGGET;
			break;
		case RABBIT:
			itemType = Material.RABBIT_FOOT;
			break;
		case SHEEP:
			itemType = Material.WOOL;
			break;
		case SKELETON:
			itemType = Material.BONE;
			break;
		case SLIME:
			itemType = Material.SLIME_BALL;
			break;
		case SQUID:
			itemType = Material.INK_SACK;
			break;
		case VILLAGER:
			itemType = Material.EMERALD;
			break;
		case WITCH:
			itemType = Material.POTION;
			break;
		case WITHER:
			itemType = Material.SKULL_ITEM;
			data = 1;
			break;
		case ZOMBIE:
			itemType = Material.ROTTEN_FLESH;
			break;
		case PLAYER:
			itemType = Material.SKULL_ITEM;
			data = 3;
			break;
		default:
			if (type.isMob()) {
				// entity ids can actually go higher than this :/
				itemType = Material.MONSTER_EGG;
				data = type.getEntityType().getTypeId();
			}
			else
				itemType = Material.BARRIER;
		}
		return new ItemStack(itemType, 1, (short)data);
	}
	
	private static String getDisguiseName(DisguiseType type) {
		return WordUtils.capitalizeFully(type.name().replace("_", " "));
	}
	
	public static Menu createEditMenu(Menu previous, MinigamePlayer player, DisguiseSettings settings) {
		Menu subMenu = new Menu(6, "Disguise Settings", player);
		
		subMenu.addItem(new MenuItemEnum<>("Disguise To Self", Material.ARMOR_STAND, settings.getShowDisguiseSelfCallback(), ShowSetting.class));
		subMenu.addItem(new MenuItemEnum<>("Disguise to Team", Material.BREWING_STAND_ITEM,settings.getShowDisguiseTeamCallback(),ShowSetting.class));
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
			super(getDisguiseName(type), Material.WOOD);
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
