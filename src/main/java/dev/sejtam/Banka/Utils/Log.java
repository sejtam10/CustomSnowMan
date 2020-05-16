package dev.sejtam.Banka.Utils;

import dev.sejtam.Banka.Banka;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class Log {
    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void sendMessage(String message) {
        console.sendMessage(_(Banka.getInstance().getPrefix() + message));
    }
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(_(Banka.getInstance().getPrefix() + message));
    }
    public static void warning(String message) {
        sendMessage("&6" + message);
    }
    public static void warning(CommandSender sender, String message) {
        sendMessage(sender, "&6" + message);
    }
    public static void error(String message) {
        sendMessage("&c" + message);
    }
    public static void error(CommandSender sender, String message) {
        sendMessage(sender, "&c" + message);
    }
    public static void error(Throwable t, String message) {
        sendMessage("&4[" + Banka.getInstance().getName() + " - " + t.getMessage() + "] " + message);
        t.printStackTrace();
    }
    public static void log(String message) {
        sendMessage("&7" + message);
    }
    public static void log(CommandSender sender, String message) {
        sendMessage(sender, "&7" + message);
    }
    public static void debug(String message) {
        if(Banka.getInstance().getDebugMode())
            sendMessage("&8[&3" + Banka.getInstance().getName() + "-DEBUG&8] &7" + message);
    }

    public static String _(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
