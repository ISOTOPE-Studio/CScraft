package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import static cc.isotopestudio.cscraft.CScraft.msgFiles;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class ProtectRoom extends Room implements Listener {

    private Location entityALocation;
    private Location entityBLocation;

    private Snowman entityA;
    private Snowman entityB;

    private int health;

    public ProtectRoom(String name) {
        super(name);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg_protect.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);

        entityALocation = Util.stringToLocation(config.getString("entityA"));
        entityBLocation = Util.stringToLocation(config.getString("entityB"));
        health = config.getInt("health");
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

    private static final PotionEffect SLOW = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 200);

    @Override
    public void join(Player player) {
        super.join(player);
        if (getTeamAplayer().size() > getTeamBplayer().size()) {
            getTeamBplayer().add(player);
        } else if (getTeamAplayer().size() < getTeamBplayer().size()) {
            getTeamAplayer().add(player);
        } else {
            if (Math.random() < 0.5)
                getTeamBplayer().add(player);
            else
                getTeamAplayer().add(player);
        }

        player.getInventory().setItem(3, GameItems.getTeam1Item());
        player.getInventory().setItem(5, GameItems.getTeam2Item());
    }

    private NoMoveTask noMoveTask1;
    private NoMoveTask noMoveTask2;

    @Override
    public void prestart() {
        super.prestart();
        entityA = (Snowman) entityALocation.getWorld().spawnEntity(entityALocation, EntityType.SNOWMAN);
        entityB = (Snowman) entityBLocation.getWorld().spawnEntity(entityBLocation, EntityType.SNOWMAN);
        entityA.setMaxHealth(health);
        entityA.setHealth(health);
        entityB.setMaxHealth(health);
        entityB.setHealth(health);
        entityA.addPotionEffect(SLOW, true);
        entityB.addPotionEffect(SLOW, true);
        entityA.setCanPickupItems(false);
        entityB.setCanPickupItems(false);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        noMoveTask1 = new NoMoveTask(entityA, entityALocation);
        noMoveTask2 = new NoMoveTask(entityB, entityBLocation);
        noMoveTask1.runTaskTimer(plugin, 1, 10);
        noMoveTask2.runTaskTimer(plugin, 1, 10);
    }

    @Override
    void resetRoom() {
        super.resetRoom();
        noMoveTask1.cancel();
        noMoveTask2.cancel();
        HandlerList.unregisterAll(this);
        entityA.remove();
        entityB.remove();
    }

    @Override
    public void updateScoreBoardInGame() {
        for (Player player : getPlayers()) {
            if (getTeamAplayer().contains(player)) {
                scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldGreen("我方血量")).setScore((int) Math.round(entityA.getHealth()));
                scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldRed("敌方血量")).setScore((int) Math.round(entityB.getHealth()));
            } else {
                scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldGreen("我方血量")).setScore((int) Math.round(entityB.getHealth()));
                scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldRed("敌方血量")).setScore((int) Math.round(entityA.getHealth()));
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
                "\nentityA=" + Util.locationToString(entityALocation) +
                "\nentityB=" + Util.locationToString(entityBLocation) +
                "\nhealth=" + health;
    }

    public static String name() {
        return S.toBoldPurple("守卫模式");
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        event.getDrops().clear();
        event.setDroppedExp(0);
        if (event.getEntity() == entityA)
            teamBWin();
        else if (event.getEntity() == entityB)
            teamAWin();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() == entityA || event.getEntity() == entityB) {
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent) event;
                if (event1.getDamager() instanceof Player)
                    if ((event1.getEntity() == entityA && getTeamBplayer().contains(event1.getDamager())) ||
                            (event1.getEntity() == entityB && getTeamAplayer().contains(event1.getDamager()))) {
                        return;
                    }
            }
            event.setCancelled(true);
        }
    }

    private class NoMoveTask extends BukkitRunnable {

        private final Entity entity;
        private final Location location;

        private NoMoveTask(Entity entity, Location location) {
            this.entity = entity;
            this.location = location;
        }

        @Override
        public void run() {
            if (entity.getLocation().distance(location) > 2) {
                entity.teleport(location);
            }
        }
    }

}
