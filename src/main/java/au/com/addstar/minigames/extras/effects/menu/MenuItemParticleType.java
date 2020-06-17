package au.com.addstar.minigames.extras.effects.menu;

import au.com.addstar.minigames.extras.effects.EffectParticle;
import au.com.addstar.minigames.extras.effects.ParticleIcons;
import au.com.mineauz.minigames.MinigameMessageType;
import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigames.menu.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
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
        if (effect.getData() instanceof BlockData) {
            dataLine = WordUtils.capitalizeFully(((BlockData) effect.getData()).getMaterial().name().replace('_', ' '));
        } else if (effect.getData() instanceof Particle.DustOptions) {
            Particle.DustOptions data = (Particle.DustOptions) effect.getData();
            dataLine = WordUtils.capitalizeFully("Color: " + data.getColor().asRGB()) + " Size: " + data.getSize();
        } else if (effect.getData() instanceof ItemStack) {
            ItemStack data = (ItemStack) effect.getData();
            dataLine = WordUtils.capitalizeFully("Item: " + data.getType().name().replace('_', ' '));


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
                        public String getValue() {
                            return "STONE AXE";
                        }

                        @Override
                        public void setValue(String value) {
                            Material material = Material.matchMaterial(value.toUpperCase());
                            if (material != null) {
                                effect.setType(type);
                                effect.setData(new ItemStack(material, 1));
                                goBack();
                            } else {
                                parent.getViewer().sendMessage("Invalid block type!", MinigameMessageType.ERROR);
                            }
                        }
                    });
                    break;
                case BLOCK_CRACK:
                case BLOCK_DUST:
                case FALLING_DUST:
                    item = new MenuItemString(name, Material.DIAMOND_PICKAXE, new Callback<String>() {
                        @Override
                        public String getValue() {
                            return "STONE";
                        }

                        @Override
                        public void setValue(String value) {
                            Material material = Material.matchMaterial(value.toUpperCase());
                            if (material != null) {
                                effect.setType(type);
                                effect.setData(Bukkit.createBlockData(material));
                                goBack();
                            } else {
                                parent.getViewer().sendMessage("Invalid block type!", MinigameMessageType.ERROR);
                            }
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
                    ItemStack icon  =  ParticleIcons.getIcon(type);
                    if(icon != null) {
                        item.setItem(icon);
                    }
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
