package au.com.addstar.minigames.extras.effects.menu;

import au.com.addstar.minigames.extras.effects.ParticleIcons;
import au.com.addstar.monolith.effects.EffectParticle;
import au.com.mineauz.minigames.MinigameMessageType;
import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigames.menu.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Collections;

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
            setDescription(Collections.singletonList(ChatColor.GREEN + typeLine));
		}
	}
	
	@Override
	public ItemStack onClick() {
		final Menu parent = getContainer();
		Menu menu = new Menu(6, "Select Particle Type", parent.getViewer());

        for (final Particle type : Particle.values()) {


            String name = StringUtils.replace(type.toString(), "_", " ");
			name = WordUtils.capitalizeFully(name);
			MenuItem item;
            switch (type) {
                case ITEM_CRACK:
                    // Just a material
                    item = new MenuItemString(name, Material.DIAMOND_SWORD, new Callback<String>() {
                        @Override
                        public void setValue(String value) {
                            Material material = Material.matchMaterial(value.toUpperCase());
                            if (material != null) {
                                effect.setType(type, material);
                                goBack();
                            } else {
                                parent.getViewer().sendMessage("Invalid block type!", MinigameMessageType.ERROR);
                            }
                        }

                        @Override
                        public String getValue() {
                            return "STONE";
                        }
                    });
                    break;
                case BLOCK_CRACK:
                case BLOCK_DUST:
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
                                        parent.getViewer().sendMessage("Invalid block data", MinigameMessageType.ERROR);
                                    } else {
                                        effect.setType(type, material.getNewData(data));
                                        goBack();
                                    }
                                } catch (NumberFormatException e) {
                                    parent.getViewer().sendMessage("Invalid block data", MinigameMessageType.ERROR);
                                }
                            } else {
                                parent.getViewer().sendMessage("Invalid block type!", MinigameMessageType.ERROR);
                            }
                        }

                        @Override
                        public String getValue() {
                            return "STONE";
                        }
                    });
                    break;
                default:
                    item = new MenuItem(name, Material.PAPER) {
                        @Override
                        public ItemStack onClick() {
                            effect.setType(type);
                            goBack();
                            return null;
                        }
                    };
                    item.setItem(ParticleIcons.getIcon(type));
                    break;
            }
			
			menu.addItem(item);
		}
		
		menu.addItem(new MenuItemBack(parent), menu.getSize() - 9);
		menu.displayMenu(getContainer().getViewer());
		
		return null;
	}
	
	private void goBack() {
        Bukkit.getScheduler().runTask(Minigames.getPlugin(), () -> getContainer().displayMenu(getContainer().getViewer()));
	}
	
}
