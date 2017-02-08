package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.element.HostileSnowman;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scoreboard.DisplaySlot;

import static cc.isotopestudio.cscraft.CScraft.msgFiles;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class ProtectRoom extends Room implements Listener {

    private Location entityALocation;
    private Location entityBLocation;

    private HostileSnowman hostileSnowmanA;
    private HostileSnowman hostileSnowmanB;

    private int health;

    public ProtectRoom(String name) {
        super(name);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg_protect.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);

        entityALocation = Util.stringToLocation(config.getString("entityA"));
        entityBLocation = Util.stringToLocation(config.getString("entityB"));
        health = config.getInt("health", 20);
    }

    public void setEntityA(Location entityA) {
        this.entityALocation = entityA;
        config.set("entityA", Util.locationToString(entityALocation));
        config.save();
    }

    public void setEntityB(Location entityB) {
        this.entityBLocation = entityB;
        config.set("entityB", Util.locationToString(entityBLocation));
        config.save();
    }

    public void setHealth(int health) {
        this.health = health;
        config.set("health", health);
        config.save();
    }

    @Override
    public boolean isReady() {
        return super.isReady() && entityALocation != null && entityBLocation != null;
    }

    @Override
    public void join(Player player) {
        super.join(player);
        if (getTeamAplayer().size() > getTeamBplayer().size()) {
            getTeamBplayer().add(player);
        } else if (getTeamAplayer().size() < getTeamBplayer().size()) {
            getTeamAplayer().add(player);
        } else {
            if (Math.random() < 0.5) {
                getTeamBplayer().add(player);
                player.getEquipment().setHelmet(GameItems.getBlueTeamCap());
            } else {
                getTeamAplayer().add(player);
                player.getEquipment().setHelmet(GameItems.getRedTeamCap());
            }
        }

        player.getInventory().setItem(3, GameItems.getTeam1Item());
        player.getInventory().setItem(5, GameItems.getTeam2Item());
    }

    private Snowman snowmanA;
    private Snowman snowmanB;

    @Override
    public void start() {
        super.start();
        hostileSnowmanA = HostileSnowman.spawn(entityALocation, getTeamBplayer());
        hostileSnowmanB = HostileSnowman.spawn(entityBLocation, getTeamAplayer());
        snowmanA = (Snowman) hostileSnowmanA.getBukkitEntity();
        snowmanB = (Snowman) hostileSnowmanB.getBukkitEntity();
        snowmanA.setCustomName(getPlayerTeamName(getTeamBplayer().iterator().next()));
        snowmanB.setCustomName(getPlayerTeamName(getTeamAplayer().iterator().next()));
        snowmanA.setMaxHealth(health);
        snowmanA.setHealth(health);
        snowmanB.setMaxHealth(health);
        snowmanB.setHealth(health);
        snowmanA.setCanPickupItems(false);
        snowmanB.setCanPickupItems(false);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    void resetRoom() {
        super.resetRoom();
        HandlerList.unregisterAll(this);
        snowmanA.remove();
        snowmanB.remove();
    }

    @Override
    public void updateScoreBoardInGame() {
        for (Player player : getPlayers()) {
            if (getTeamAplayer().contains(player)) {
                scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldGreen("我方血量")).setScore((int) Math.round(snowmanA.getHealth()));
                scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldRed("敌方血量")).setScore((int) Math.round(snowmanB.getHealth()));
            } else {
                scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldGreen("我方血量")).setScore((int) Math.round(snowmanB.getHealth()));
                scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldRed("敌方血量")).setScore((int) Math.round(snowmanA.getHealth()));
            }
        }
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String infoString() {
        return super.infoString() +
                "\nentityALoc=" + Util.locationToString(entityALocation) +
                "\nentityBLoc=" + Util.locationToString(entityBLocation) +
                "\nentityA=" + (hostileSnowmanA == null ? "null" : hostileSnowmanA.toString()) +
                "\nentityA=" + (hostileSnowmanB == null ? "null" : hostileSnowmanB.toString()) +
                "\nentityB=" + Util.locationToString(entityBLocation) +
                "\nhealth=" + health;
    }

    public static String name() {
        return S.toBoldPurple("守卫模式");
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() == snowmanA) {
            teamBWin();
            event.getDrops().clear();
            event.setDroppedExp(0);
        } else if (event.getEntity() == snowmanB) {
            teamAWin();
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity victim = event.getEntity();
        if (victim == snowmanA || victim == snowmanB) {
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent) event;
                if (event1.getDamager() instanceof Player)
                    if ((victim == snowmanA && getTeamBplayer().contains(event1.getDamager())) ||
                            (victim == snowmanB && getTeamAplayer().contains(event1.getDamager()))) {
                        return;
                    }
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(WeaponDamageEntityEvent event) {
        Entity victim = event.getVictim();
//        System.out.println(event.getDamager()); // snowball
//        System.out.println(event.getVictim()); //
//        System.out.println(event.getPlayer()); // shooter
        if (victim == snowmanA || victim == snowmanB) {
//            System.out.println(victim == snowmanA);
//            System.out.println(getTeamBplayer().contains(event.getPlayer()));
//            System.out.println(victim == snowmanB);
//            System.out.println(getTeamAplayer().contains(event.getPlayer()));
            if ((victim == snowmanA && getTeamBplayer().contains(event.getPlayer())) ||
                    (victim == snowmanB && getTeamAplayer().contains(event.getPlayer())))
                return;

        }
        event.setCancelled(true);
    }

}
