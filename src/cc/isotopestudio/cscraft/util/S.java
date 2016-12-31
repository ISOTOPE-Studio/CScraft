package cc.isotopestudio.cscraft.util;

import org.bukkit.ChatColor;

import static cc.isotopestudio.cscraft.CScraft.prefix;

/*
 * Created by Mars on 5/15/2016.
 * Copyright ISOTOPE Studio
 */
public class S {

    public static String toRed(String s) {
        return String.valueOf(ChatColor.RED) + s + ChatColor.RESET;
    }

    public static String toGreen(String s) {
        return String.valueOf(ChatColor.GREEN) + s + ChatColor.RESET;
    }

    public static String toYellow(String s) {
        return String.valueOf(ChatColor.YELLOW) + s + ChatColor.RESET;
    }

    public static String toAqua(String s) {
        return String.valueOf(ChatColor.AQUA) + s + ChatColor.RESET;
    }

    public static String toGray(String s) {
        return String.valueOf(ChatColor.GRAY) + s + ChatColor.RESET;
    }

    public static String toGold(String s) {
        return String.valueOf(ChatColor.GOLD) + s + ChatColor.RESET;
    }

    public static String toBoldGreen(String s) {
        return String.valueOf(ChatColor.GREEN) + ChatColor.BOLD + s + ChatColor.RESET;
    }

    public static String toBoldDarkGreen(String s) {
        return String.valueOf(ChatColor.DARK_GREEN) + ChatColor.BOLD + s + ChatColor.RESET;
    }

    public static String toBoldDarkAqua(String s) {
        return String.valueOf(ChatColor.DARK_AQUA) + ChatColor.BOLD + s + ChatColor.RESET;
    }

    public static String toBoldPurple(String s) {
        return String.valueOf(ChatColor.LIGHT_PURPLE) + ChatColor.BOLD + s + ChatColor.RESET;
    }

    public static String toBoldGold(String s) {
        return String.valueOf(ChatColor.GOLD) + ChatColor.BOLD + s + ChatColor.RESET;
    }

    public static String toItalicYellow(String s) {
        return String.valueOf(ChatColor.YELLOW) + ChatColor.ITALIC + s + ChatColor.RESET;
    }

    public static String toPrefixRed(String s) {
        return prefix + ChatColor.RED + s + ChatColor.RESET;
    }

    public static String toPrefixGreen(String s) {
        return prefix + ChatColor.GREEN + s + ChatColor.RESET;
    }

    public static String toPrefixYellow(String s) {
        return prefix + ChatColor.YELLOW + s + ChatColor.RESET;
    }

    public static String toPrefixGray(String s) {
        return prefix + ChatColor.GRAY + s + ChatColor.RESET;
    }

    public static String toBoldRed(String s) {
        return String.valueOf(ChatColor.RED) + ChatColor.BOLD + s + ChatColor.RESET;
    }

    public static String toPrefixAqua(String s) {
        return prefix + ChatColor.AQUA + s + ChatColor.RESET;
    }
}
