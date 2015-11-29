package au.com.addstar.minigames.extras.effects;

import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import au.com.addstar.minigames.extras.MenuItemEnum;
import au.com.addstar.monolith.attachments.Attachment;
import au.com.addstar.monolith.effects.emitters.ContinuousEmitter;
import au.com.addstar.monolith.effects.emitters.Emitter;
import au.com.addstar.monolith.effects.emitters.OneShotEmitter;
import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItemBack;
import au.com.mineauz.minigames.menu.MenuItemInteger;
import au.com.mineauz.minigames.menu.MenuItemNewLine;
import au.com.mineauz.minigames.menu.MenuItemString;

public class EmitterTemplate {
	private Type type;
	
	private String effectType;
	
	private int delay;
	private int interval;
	
	public EmitterTemplate() {
		type = Type.OneShot;
		delay = 20;
		interval = 20;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public String getEffect() {
		return effectType;
	}
	
	public void setEffect(String effect) {
		this.effectType = effect;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public int getInterval() {
		return interval;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public void save(ConfigurationSection section) {
		section.set("type", type.name());
		section.set("effect", effectType);
		section.set("delay", delay);
		section.set("interval", interval);
	}
	
	public void load(ConfigurationSection section) {
		type = Type.valueOf(section.getString("type"));
		effectType = section.getString("effect");
		delay = section.getInt("delay");
		interval = section.getInt("interval");
	}
	
	public Emitter create(Attachment attachment) {
		Emitter emitter;
		switch (type) {
		case OneShot:
			emitter = new OneShotEmitter(attachment);
			break;
		case Continuous:
			emitter = new ContinuousEmitter(attachment);
			((ContinuousEmitter)emitter).setDelay(delay * 50, TimeUnit.MILLISECONDS);
			((ContinuousEmitter)emitter).setInterval(interval * 50, TimeUnit.MILLISECONDS);
			break;
		default:
			throw new AssertionError();
		}
		
		return emitter;
	}
	
	public void displayMenu(MinigamePlayer player, Menu previous) {
		Menu menu = new Menu(6, "Emitter", player);
		
		menu.addItem(new MenuItemEnum<>("Type", Material.ENDER_PEARL, new Callback<Type>() {
			@Override
			public Type getValue() {
				return type;
			}
			
			@Override
			public void setValue(Type value) {
				type = value;
			}
		}, Type.class));
		
		menu.addItem(new MenuItemString("Effect", Material.FIREWORK_CHARGE, new Callback<String>() {
			@Override
			public String getValue() {
				return effectType;
			}
			
			@Override
			public void setValue(String value) {
				effectType = value;
			}
		}));
		// TODO: Once we can get the minigame being edited, use this
//		EffectModule module = EffectModule.getModule(player.getMinigame());
//		menu.addItem(new MenuItemSelectEffect("Effect", Material.FIREWORK_CHARGE, module, new Callback<String>() {
//			@Override
//			public void setValue(String value) {
//				effectType = value;
//			}
//			
//			@Override
//			public String getValue() {
//				return effectType;
//			}
//		}));
		
		menu.addItem(new MenuItemNewLine());
		menu.addItem(new MenuItemInteger("Delay", MinigameUtils.stringToList("The number of ticks;until the first emission;Continuous Emitter Only"), Material.WATCH, new Callback<Integer>() {
			@Override
			public void setValue(Integer value) {
				delay = value;
			}
			
			@Override
			public Integer getValue() {
				return delay;
			}
		}, 0, null));
		
		menu.addItem(new MenuItemInteger("Interval", MinigameUtils.stringToList("The number of ticks;between emissions;Continuous Emitter Only"), Material.WATCH, new Callback<Integer>() {
			@Override
			public void setValue(Integer value) {
				interval = value;
			}
			
			@Override
			public Integer getValue() {
				return interval;
			}
		}, 1, null));
		
		menu.addItem(new MenuItemBack(previous), menu.getSize() - 9);
		menu.displayMenu(player);
	}
	
	public enum Type {
		OneShot,
		Continuous
	}
}
