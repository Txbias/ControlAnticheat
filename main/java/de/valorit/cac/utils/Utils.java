package de.valorit.cac.utils;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.config.Config;
import de.valorit.cac.config.Messages;
import de.valorit.cac.version_dependent.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {

    public static final String PLUGIN_NAME = "ControlAnticheat";
    public static final String PREFIX = "§6CAC §7>> ";

    public static void broadCheckResult(CheckResult result) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(Permissions.NOTIFY) || p.isOp() || p.hasPermission(Permissions.ADMIN)) {
                User user = CheckResultsManager.getUser(p);
                if(user.isReceivesNotifications()) {
                    Utils.sendCheckResult(p, result);
                }
            }
        }
        if(Config.areAlertsInConsoleEnabled()) {
            Player p = result.getPlayer();
            User user = CheckResultsManager.getUser(p);
            Bukkit.getConsoleSender().sendMessage("CAC >> " + Messages.getCheckResult(p, VersionManager.getCraftPlayerManager().getPing(p),
                    result.getModule(), user.getLevel(result.getModule())));
        }
    }

    public static void broadCastWarning(String warning) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission(Permissions.NOTIFY) || p.hasPermission(Permissions.ADMIN) || p.isOp()) {
                User user = CheckResultsManager.getUser(p);
                if(user.isReceivesNotifications()) {
                    Utils.sendWarning(p, warning);
                }
            }
        }
        if(Config.areAlertsInConsoleEnabled()) {
            Bukkit.getConsoleSender().sendMessage(warning);
        }
    }

    public static void broadCastMessage(String message) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission(Permissions.NOTIFY) || p.hasPermission(Permissions.ADMIN) || p.isOp()) {
                User user = CheckResultsManager.getUser(p);
                if(user.isReceivesNotifications()) {
                    Utils.sendMessage(p, message);
                }
            }
        }
    }

    public static void sendNoPerm(Player p) {
        p.sendMessage(PREFIX + Messages.getNoPermission());
    }

    public static void sendError(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + "§c" + message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + "§7" + message);
    }

    public static void sendMessageToConsole(CommandSender sender, String message) {
        sender.sendMessage("CAC >> " + message);
    }

    public static void sendWarning(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + "§e" + message);
    }

    public static void sendNotOnline(CommandSender sender, String notOnline) {
        sendError(sender, Messages.getNotOnline(notOnline));
    }

    public static double round(double num) {
        return (double) Math.round(num * 10000d) / 10000d;
    }

    private static void sendCheckResult(Player receiver, CheckResult result) {
        User user = CheckResultsManager.getUser(result.getPlayer());
        Player p = user.getPlayer();
        int level = user.getLevel(result.getModule());
        receiver.sendMessage(PREFIX + " " + Messages.getCheckResult(p, VersionManager.getCraftPlayerManager().getPing(p),
                result.getModule(), level));
    }

}
