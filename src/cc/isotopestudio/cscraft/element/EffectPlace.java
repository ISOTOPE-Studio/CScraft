package cc.isotopestudio.cscraft.element;
/*
 * Created by Mars Tan on 1/9/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.cscraft.CScraft.plugin;

public class EffectPlace {

    private final Room room;
    private final Location location;
    private final Material material;
    private final PotionEffect effect;
    private final int cd;

    public EffectPlace(Room room, Location location, Material material, PotionEffect effect, int cd) {
        this.room = room;
        this.location = location;
        this.material = material;
        this.effect = effect;
        this.cd = cd;
    }


    public PotionEffect getEffect() {
        return effect;
    }

    public String serialize() {
        return Util.locationToString(location) + ";" + material.name() + ";" + Util.potionEffectToString(effect) + ";" + cd;
    }

    public static EffectPlace deserialize(Room room, String string) {
        String[] s = string.split(";");
        Location loc = Util.stringToLocation(s[0]);
        Material material = Material.getMaterial(s[1]);
        PotionEffect effect = Util.stringToPotionEffect(s[2]);
        int cd = Integer.parseInt(s[3]);
        return new EffectPlace(room, loc, material, effect, cd);
    }

    @Override
    public String toString() {
        return "EffectPlace{" + "location=" + Util.locationToString(location) +
                ", material=" + material +
                ", effect=" + effect +
                ", cd=" + cd +
                '}';
    }

    public void spawn() {
        room.getEffectItems().put(location.getWorld().
                dropItem(location, new ItemStack(material)), this);
    }

    public void respawn() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (room.getStatus() == RoomStatus.PROGRESS) {
                    spawn();
                }
            }
        }.runTaskLater(plugin, cd * 20);
    }
}
