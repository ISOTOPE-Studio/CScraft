package cc.isotopestudio.cscraft;

import cc.isotopestudio.cscraft.command.CommandCscraft;
import cc.isotopestudio.cscraft.listener.PlayerListener;
import cc.isotopestudio.cscraft.util.PluginFile;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class CScraft extends JavaPlugin {

    private static final String pluginName = "CScraft";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("CScraft").append("]").append(ChatColor.RED).toString();

    public static CScraft plugin;

    public static PluginFile config;
    public static PluginFile warpData;

    @Override
    public void onEnable() {
        plugin = this;
        config = new PluginFile(this, "config.yml", "config.yml");
        config.setEditable(false);
        warpData = new PluginFile(this, "warps.yml");

        this.getCommand("w").setExecutor(new CommandCscraft());

        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().info(pluginName + "成功加载!");
        getLogger().info(pluginName + "由ISOTOPE Studio制作!");
        getLogger().info("http://isotopestudio.cc");
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginName + "成功卸载!");
    }

}
