package cc.isotopestudio.cscraft.element;
/*
 * Created by Mars on 1/22/2017.
 * Copyright ISOTOPE Studio
 */
// https://bukkit.org/threads/how-to-override-default-minecraft-mobs.216788/

import cc.isotopestudio.cscraft.util.Util;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HostileSnowman extends EntitySnowman {
    private final Set<Player> attackablePlayer;
    private final Set<String> attackablePlayerName;
    private final Location location;

    private HostileSnowman(World world, Location location, Set<Player> attackablePlayer) {
        super(world);
        this.location = location;
        this.attackablePlayer = attackablePlayer;
        this.attackablePlayerName = Util.playerToStringSet(attackablePlayer);
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.6D));
//        this.goalSelector.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0F, 1.0F));
        this.goalSelector.a(9, new PathfinderGoalInteract(this, EntitySnowman.class, 5.0F, 0.02F));
        this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.6D));

        this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.25, 20, 10.0f));
//        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0f));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));

    }

    @Override
    public void move(double d0, double d1, double d2) {
    }

    @Override
    public void collide(Entity entity) {
    }

    @Override
    public void a(EntityLiving useless, float f) {
        if (!(useless instanceof EntityHuman)) return;

        String playerName = ((EntityHuman) useless).getBukkitEntity().getName();
        if (!attackablePlayerName.contains(playerName)) return;

        Player nearestPlayer = null;
        double min = 8;
        for (Player player : attackablePlayer) {
            if (player.getLocation().distance(location) < min) {
                nearestPlayer = player;
                min = player.getLocation().distance(location);
            }
        }
        if (nearestPlayer != null) {
            HurtfulSnowball entitysnowball = new HurtfulSnowball(this.world, this);
            Location loc = nearestPlayer.getLocation();
            double d0 = loc.getY() + (double) useless.getHeadHeight() - 1.100000023841858D;
            double d1 = loc.getX() - this.locX;
            double d2 = d0 - entitysnowball.locY;
            double d3 = loc.getZ() - this.locZ;
            float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
            entitysnowball.shoot(d1, d2 + (double) f1, d3, 1.6F, 12.0F);

            this.makeSound("random.bow", 1.0F, 1.0F / (this.bc().nextFloat() * 0.4F + 0.8F));
            this.world.addEntity(entitysnowball);
        }
    }

    @Override
    public void g(double d0, double d1, double d2) {
    }

    @Override
    public String toString() {
        return "HostileSnowman{"
                + "attackablePlayer=" + attackablePlayerName +
                "\nisAlive=" + isAlive() +
                "\nisInvisible=" + isInvisible() +
                "\nlocation=" + (int) locX + (int) locY + (int) locZ +
                '}';
    }

    public static HostileSnowman spawn(Location loc, Set<Player> attackablePlayer) {
        World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        final HostileSnowman customEnt = new HostileSnowman(mcWorld, loc, attackablePlayer);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false); //Do we want to remove it when the NPC is far away? I won
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
//        return (Snowman) customEnt.getBukkitEntity();
        return customEnt;
    }

    public static void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
        try {

            List<Map<?, ?>> dataMap = new ArrayList<>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HurtfulSnowball extends EntitySnowball {

        HurtfulSnowball(World world, EntityLiving entityLiving) {
            super(world, entityLiving);
        }

        @Override
        protected void a(MovingObjectPosition var1) {
            if (var1.entity != null) {
                byte var2 = 0;
                if (var1.entity instanceof EntityHuman) {
                    var2 = 1;
                }

                var1.entity.damageEntity(DamageSource.projectile(this, this.getShooter()), (float) var2);
            }

            for (int var3 = 0; var3 < 8; ++var3) {
                this.world.addParticle(EnumParticle.SNOWBALL, this.locX, this.locY, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
            }

            if (!this.world.isClientSide) {
                this.die();
            }

        }
    }
}