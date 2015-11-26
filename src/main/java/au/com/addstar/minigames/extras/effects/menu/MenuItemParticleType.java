package au.com.addstar.minigames.extras.effects.menu;

import java.util.Arrays;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import au.com.addstar.minigames.extras.effects.ParticleIcons;
import au.com.addstar.monolith.effects.EffectParticle;
import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItem;
import au.com.mineauz.minigames.menu.MenuItemBack;
import au.com.mineauz.minigames.menu.MenuItemString;

public class MenuItemParticleType extends MenuItem {
	private final EffectParticle effect;
	
	public MenuItemParticleType(String name, Material displayItem, EffectParticle effect) {
		super(name, displayItem);
		
		this.effect = effect;
	}
	
	@Override
	public void update() {
		String typeLine = WordUtils.capitalizeFully(effect.getType().name().replace('_', ' '));
		String dataLine = null;
		if (effect.getData() instanceof Material) {
			dataLine = WordUtils.capitalizeFully(((Material)effect.getData()).name().replace('_', ' '));
		} else if (effect.getData() instanceof MaterialData) {
			MaterialData data = (MaterialData)effect.getData();
			dataLine = WordUtils.capitalizeFully(data.getItemType().name().replace('_', ' ')) + ":" + data.getData();
		}
		
		if (dataLine != null) {
			setDescription(Arrays.asList(ChatColor.GREEN + typeLine, ChatColor.GRAY + dataLine));
		} else {
			setDescription(Arrays.asList(ChatColor.GREEN + typeLine));
		}
	}
	
	@Override
	public ItemStack onClick() {
		final Menu parent = getContainer();
		Menu menu = new Menu(6, "Select Particle Type", parent.getViewer());
		
		for (final Effect type : Effect.values()) {
			if (type.getType() != Effect.Type.PARTICLE) {
				continue;
			}
			
			String name = type.name().replace('_', ' ');
			name = WordUtils.capitalizeFully(name);
			
			MenuItem item;
			if (type == Effect.ITEM_BREAK) {
				// Just a material
				item = new MenuItemString(name, Material.DIAMOND_SWORD, new Callback<String>() {
					@Override
					public void setValue(String value) {
						Material material = Material.matchMaterial(value.toUpperCase()); 
						if (material != null) {
							effect.setType(type, material);
							goBack();
						} else {
							parent.getViewer().sendMessage("Invalid block type!", "error");
						}
					}
					
					@Override
					public String getValue() {
						return "STONE";
					}
				});
			} else if (type == Effect.TILE_BREAK || type == Effect.TILE_DUST) {
				// material and data value
				item = new MenuItemString(name, Material.DIAMOND_PICKAXE, new Callback<String>() {
					@Override
					public void setValue(String value) {
						String typePart;
						String dataPart;
						
						if (value.contains(":")) {
							String[] parts = value.split(":", 2);
							typePart = parts[0];
							dataPart = parts[1];
						} else {
							typePart = value;
							dataPart = "0";
						}
						
						Material material = Material.matchMaterial(typePart.toUpperCase()); 
						if (material != null) {
							try {
								byte data = Byte.parseByte(dataPart);
								if (data < 0 || data > 15) {
									parent.getViewer().sendMessage("Invalid block data", "error");
								} else {
									effect.setType(type, material.getNewData(data));
									goBack();
								}
							} catch (NumberFormatException e) {
								parent.getViewer().sendMessage("Invalid block data", "error");
							}
						} else {
							parent.getViewer().sendMessage("Invalid block type!", "error");
						}
					}
					
					@Override
					public String getValue() {
						return "STONE";
					}
				});
			} else {
				item = new MenuItem(name, Material.PAPER) {
					@Override
					public ItemStack onClick() {
						effect.setType(type);
						goBack();
						return null;
					}
				};
				item.setItem(ParticleIcons.getIcon(type));
			}
			
			menu.addItem(item);
		}
		
		menu.addItem(new MenuItemBack(parent), menu.getSize() - 9);
		menu.displayMenu(getContainer().getViewer());
		
		return null;
	}
	
	private void goBack() {
		Bukkit.getScheduler().runTask(Minigames.plugin, new Runnable() {
			@Override
			public void run() {
				getContainer().displayMenu(getContainer().getViewer());
			}
		});
	}
	
}
