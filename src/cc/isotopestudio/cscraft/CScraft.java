package cc.isotopestudio.cscraft;

import cc.isotopestudio.cscraft.command.CommandCs;
import cc.isotopestudio.cscraft.command.CommandCsclass;
import cc.isotopestudio.cscraft.command.CommandCsreload;
import cc.isotopestudio.cscraft.command.CommandCsroom;
import cc.isotopestudio.cscraft.listener.PlayerListener;
import cc.isotopestudio.cscraft.task.CheckPlayerLocation;
import cc.isotopestudio.cscraft.task.UpdateConfig;
import cc.isotopestudio.cscraft.util.PluginFile;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

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
    public static final Set<PluginFile> roomFiles = new HashSet<>();
    public static final Set<PluginFile> msgFiles = new HashSet<>();

    @Override
    public void onEnable() {
        plugin = this;
        config = new PluginFile(this, "config.yml", "config.yml");
        config.setEditable(false);
        classData = new PluginFile(this, "class.yml");
        playerData = new PluginFile(this, "player.yml");

        this.getCommand("csclass").setExecutor(new CommandCsclass());
        this.getCommand("csroom").setExecutor(new CommandCsroom());
        this.getCommand("cs").setExecutor(new CommandCs());
        this.getCommand("csreload").setExecutor(new CommandCsreload());

        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        new UpdateConfig().run();
        new CheckPlayerLocation().runTaskTimer(this, 20, 20);

        getLogger().info(pluginName + "成功加载!");
        getLogger().info(pluginName + "由ISOTOPE Studio制作!");
        getLogger().info("http://isotopestudio.cc");
    }

    public void onReload() {
        playerData.reload();
        config.reload();
        new UpdateConfig().run();
    }

    @Override
    public void onDisable() {
        //End all games
        getLogger().info(pluginName + "成功卸载!");
    }

}
