package cc.isotopestudio.cscraft;

import cc.isotopestudio.cscraft.command.CommandCs;
import cc.isotopestudio.cscraft.command.CommandCsclass;
import cc.isotopestudio.cscraft.command.CommandCsreload;
import cc.isotopestudio.cscraft.command.CommandCsroom;
import cc.isotopestudio.cscraft.debugGUI.LogGUI;
import cc.isotopestudio.cscraft.debugGUI.SettingsGUI;
import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.element.HostileSnowman;
import cc.isotopestudio.cscraft.players.PlayerInfo;
import cc.isotopestudio.cscraft.players.PlayerListener;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.task.*;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.Util;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class CScraft extends JavaPlugin {

    private static final String pluginName = "CScraft";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("CScraft").append("]").append(ChatColor.RED).toString();

    public static CScraft plugin;

    public static PluginFile config;
    public static PluginFile classData;
    public static PluginFile playerData;
    public static PluginFile classMsgData;
    public static final Set<PluginFile> roomFiles = new HashSet<>();
    public static final Set<PluginFile> msgFiles = new HashSet<>();

    @Override
    public void onEnable() {
        plugin = this;
        config = new PluginFile(this, "config.yml", "config.yml");
        config.setEditable(false);
        classData = new PluginFile(this, "class.yml");
        playerData = new PluginFile(this, "player.yml");
        classMsgData = new PluginFile(this, "classMsg.yml", "classMsg.yml");
        classMsgData.setEditable(false);

        HostileSnowman.registerEntity("Snowman", 97, EntitySnowman.class, HostileSnowman.class);

        this.getCommand("csclass").setExecutor(new CommandCsclass());
        this.getCommand("csroom").setExecutor(new CommandCsroom());
        this.getCommand("cs").setExecutor(new CommandCs());
        this.getCommand("csreload").setExecutor(new CommandCsreload());

        PlayerListener.enableListener();

        new UpdateConfig().run();
        new CheckPlayerLocation().runTaskTimer(this, 20, 20);
        new RoomLobbyUpdateTask().runTaskTimer(this, 10, 20);
        new RoomGameUpdateTask().runTaskTimer(this, 10, 20);
        new ParticleTask().runTaskTimer(this, 11, 2);

        GameItems.update();

        new SettingsGUI().run();
        new LogGUI().run();
        SettingsGUI.on(true);
        SettingsGUI.text.setWrapStyleWord(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                SettingsGUI.text.setText(Util.playerToStringSet(PlayerInfo.playerRoomMap.keySet()) + "\n");
                for (Room room : Room.rooms.values())
                    SettingsGUI.text.append(room.infoString() + "\n");
            }
        }.runTaskTimer(this, 20, 20);

        new InvisibleArmorTask().runTaskLater(this, 40);

        getLogger().info(pluginName + " " + getDescription().getVersion() + " �ɹ�����!");
        getLogger().info(pluginName + "��ISOTOPE Studio����!");
        getLogger().info("http://isotopestudio.cc");
    }

    public void onReload() {
        playerData.reload();
        config.reload();
        classData.reload();
        classMsgData.reload();
        new UpdateConfig().run();
    }

    @Override
    public void onDisable() {
        //End all games
        getLogger().info(pluginName + "�ɹ�ж��!");
    }

}
