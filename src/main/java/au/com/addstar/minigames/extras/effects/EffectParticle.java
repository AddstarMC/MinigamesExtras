package au.com.addstar.minigames.extras.effects;

import au.com.addstar.monolith.effects.BaseEffect;
import com.google.common.base.Preconditions;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created for the AddstarMC IT Project.
 * Created by Narimm on 17/06/2020.
 */
public class EffectParticle extends BaseEffect {

    private Particle type;
    private Vector range;
    private float size = 1;
    private float speed;
    private int count;

    private Color colour;

    private static List<Class> dataClasses;

    static {

        dataClasses.add(Particle.DustOptions.class);
        dataClasses.add(BlockData.class);
        dataClasses.add(ItemStack.class);
        dataClasses.add(Void.class);

    }


    public Object getData() {
        return data;
    }

    private Object data;

    public EffectParticle(Particle type, Vector range, float speed, int count){
        Preconditions.checkArgument(speed >= 0);
        Preconditions.checkArgument(count > 0);
        this.type = type;
        this.range = range.clone();
        this.speed = speed;
        this.count = count;

    }

    public EffectParticle( Particle type, Color colour, Vector range, float speed, int count )
    {
        this(type,range,speed,count);
        this.colour = colour;
    }

    public EffectParticle()
    {
        range = new Vector();
    }

    public void setData(@Nullable Object data) {
        Preconditions.checkArgument(!(data instanceof Void),"Data cannot be VOID");
        Preconditions.checkArgument(dataClasses.contains(data.getClass()), "Particle data not valid");
        this.data = data;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Particle getType()
    {
        return type;
    }

    public void setType( Particle type )
    {
        this.type = type;
    }


    public Vector getRange()
    {
        return range;
    }

    public void setRange( Vector range )
    {
        this.range = range;
    }

    public float getSpeed()
    {
        return speed;
    }

    public void setSpeed( float speed )
    {
        Preconditions.checkArgument(speed >= 0);
        this.speed = speed;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount( int count )
    {
        Preconditions.checkArgument(count > 0);
        this.count = count;
    }

    public Color getColour()
    {
        return colour;
    }

    public void setColour( Color colour )
    {
        this.colour = colour;
    }

    /**
     * Emits this particle effect so everyone can see it
     *
     * @param location The location to emit at
     */
    @Override
    public void spawn( Location location )
    {
        World world = location.getWorld();
        if (world == null)
            return;
        checkSetColour();
        if (data != null)
            world.spawnParticle(type, location, count, (float) range.getX(), (float) range.getY(), (float) range.getZ(), speed, data);
        else
            world.spawnParticle(type, location, count, (float) range.getX(), (float) range.getY(), (float) range.getZ(), speed);

    }

    private void checkSetColour(){
        if (colour == null)
            return;
        if (type == Particle.REDSTONE) {
            // Redstone dust has a default red value that is from 0.8 to
            // 1.0, its random so just average it out
            //r = -0.9f + colour.getRed() / 255f;
            //g = colour.getGreen() / 255f;
            //b = colour.getBlue() / 255f;
            if (data == null) {
                data = new Particle.DustOptions(colour, 1F);
            } else {
                if (data instanceof Particle.DustOptions) {
                    if (colour != ((Particle.DustOptions) data).getColor()) {
                        data = new Particle.DustOptions(colour, size);
                    }
                }
            }
        }
    }
    /**
     * Emits this particle effect so that only {@code player}
     * can see it
     *
     * @param player The player to emit to
     * @param location The location to emit at
     */
    @Override
    public void spawn(Player player, Location location )
    {
        checkSetColour();
        if (data != null)
            player.spawnParticle(type, location, count, (float) range.getX(), (float) range.getY(), (float) range.getZ(), speed, data);
        else
            player.spawnParticle(type, location, count, (float) range.getX(), (float) range.getY(), (float) range.getZ(), speed);
    }

    @Override
    public String toString()
    {
        if ( colour != null )
        {
            return String.format("Effect: %s colour: %s count: %d speed: %.1f range: [%.1f,%.1f,%.1f]", type.name(), colour, count, speed, range.getX(), range.getY(), range.getZ());
        }
        else
        {
            return String.format("Effect: %s count: %d speed: %.1f range: [%.1f,%.1f,%.1f]", type.name(), count, speed, range.getX(), range.getY(), range.getZ());
        }
    }

    @Override
    public void load( ConfigurationSection section )
    {
        if (section.isString("ptype"))
            type = Particle.valueOf(section.getString("ptype"));
        else {
           if (section.getString("type") != null) {
               Logger.getLogger("Minigames").warning("Configuration contains OLD EFFECT TYPE ");
               Logger.getLogger("Minigames").warning("Using default: Enchant table ");
               type = Particle.ENCHANTMENT_TABLE;
           };
        }

        range = section.getVector("range", new Vector());
        speed = (float)section.getDouble("speed");
        count = section.getInt("count");

        if (section.contains("color"))
            colour = Color.fromRGB(section.getInt("color"));
        else
            colour = null;

        data = null;
        if (section.isConfigurationSection("data"))
        {
            ConfigurationSection dataSection = section.getConfigurationSection("data");

            switch (dataSection.getString("="))
            {
                case "mat":
                    // Backwards compatability
                    data = new ItemStack(Material.valueOf(dataSection.getString("value")));
                    break;
                case "matdata":
                    Logger.getLogger("Minigames").warning("Configuration contains OLD data TYPE:matdata - it will be ignored ");
                    break;
                case "itemstack":
                    data = dataSection.getItemStack("value");
                    break;
            }
        }
    }

    @Override
    public void save( ConfigurationSection section )
    {
        section.set("ptype", type.name());
        section.set("range", range);
        section.set("speed", speed);
        section.set("count", count);
        if (colour != null) {
            section.set("color", colour.asRGB());
        }
        if(data != null) {
            if (data.getClass() != type.getDataType()) {
                Logger.getLogger("Minigames").warning("ParticleEffect dataType is inconsistent");
            }
        } else if (data instanceof ItemStack) {
            ConfigurationSection dataSection = section.createSection("data");
            dataSection.set("=", "itemstack");
            dataSection.set("value", data);
        }
    }

    @Override
    public EffectParticle clone()
    {
        EffectParticle clone = new EffectParticle(type,range,speed,count);

        if (colour != null)
            clone.colour = Color.fromRGB(colour.asRGB());
        if (data instanceof ItemStack) {
            clone.data = ((ItemStack) data).clone();
        } else if (data instanceof BlockData) {
            clone.data = ((BlockData) data).clone();
        } else if (data instanceof Particle.DustOptions)
            clone.data = new Particle.DustOptions(Color.fromRGB(((Particle.DustOptions) data).getColor().asRGB()),((Particle.DustOptions) data).getSize());
        return clone;
    }
}