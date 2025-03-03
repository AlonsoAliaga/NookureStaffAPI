package com.alonsoaliaga.nookurestaffapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LocalUtils {
    private final static String PLUGIN_NAME = "NookureStaffAPI";
    public static void logp(String message) {
        Bukkit.getConsoleSender().sendMessage(colorize("&9["+PLUGIN_NAME+"] &7"+message));
    }
    public static void loge(String message) {
        Bukkit.getConsoleSender().sendMessage(colorize("&c["+PLUGIN_NAME+"] "+message));
    }
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }
}