package cc.isotopestudio.cscraft.command;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.S;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static cc.isotopestudio.cscraft.CScraft.plugin;

public class CommandCsreload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("csreload")) {
            if (!sender.hasPermission("cscraft.admin")) {
                sender.sendMessage(S.toPrefixRed("你没有权限"));
                return true;
            }
            plugin.onReload();
            sender.sendMessage(S.toPrefixGreen("成功重载"));
            return true;
        }
        return false;
    }
}