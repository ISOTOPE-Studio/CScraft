package cc.isotopestudio.cscraft.element;
/*
 * Created by Mars Tan on 1/9/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

public class EffectPlace {

    private final Location location;
    private final Material material;
    private final PotionEffect effect;
    private final int cd;

    public EffectPlace(Location location, Material material, PotionEffect effect, int cd) {
        this.location = location;
        this.material = material;
        this.effect = effect;
        this.cd = cd;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public PotionEffect getEffect() {
        return effect;
    }

    public int getCooldownTime() {
        return cd;
    }

    public String serialize() {
        return Util.locationToString(location) + ";" + material.name() + ";" + Util.potionEffectToString(effect) + ";" + cd;
    }

    public static EffectPlace deserialize(String string) {
        String[] s = string.split(";");
        Location loc = Util.stringToLocation(s[0]);
        Material material = Material.getMaterial(s[1]);
        PotionEffect effect = Util.stringToPotionEffect(s[2]);
        int cd = Integer.parseInt(s[3]);
        return new EffectPlace(loc, material, effect, cd);
    }
}
