package de.valorit.cac;

import de.valorit.cac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private static final Main PLUGIN = Main.getInstance();

    private static final File CONFIG_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "config.yml");

    private static FileConfiguration config = Main.getInstance().getConfig();

    private static long minTimeBetweenBowShots;
    private static int maxPackets;
    private static int maxPing;
    private static double maxCombatRange;
    private static boolean alertsInConsole;

    private static boolean autoKick;
    private static boolean autoBan;
    private static String banCommand;
    private static int maxFlagsPerModule;
    private static int maxFlagsTotal;

    public static void loadConfig() {
        if(!CONFIG_FILE.exists()) {
            PLUGIN.saveDefaultConfig();
        }

        minTimeBetweenBowShots = config.getLong("minTimeBetweenBowShots");
        maxPackets = config.getInt("maxPackets");
        maxPing = config.getInt("maxPing");
        maxCombatRange = config.getDouble("maxCombatRange");

        autoKick = config.getBoolean("autoKick");
        autoBan = config.getBoolean("autoBan");
        banCommand = config.getString("banCommand");
        maxFlagsPerModule = config.getInt("maxFlagsPerModule");
        maxFlagsTotal = config.getInt("maxFlagsTotal");
        alertsInConsole = config.getBoolean("alertsInConsole");
    }

    public static void reloadConfig() {
        try {
            config = Main.getInstance().getConfig();
            config.load(CONFIG_FILE);
            loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            Utils.sendMessageToConsole(Bukkit.getServer().getConsoleSender(), "Couldn't reload config.");
        }
    }

    public static long getMinTimeBetweenBowShots() {
        return minTimeBetweenBowShots;
    }

    public static int getMaxPackets() {
        return maxPackets;
    }

    public static int getMaxPing() {
        return maxPing;
    }

    public static double getMaxCombatRange() {
        return maxCombatRange;
    }

    public static boolean isAutoKickEnabled() {
        return autoKick;
    }

    public static boolean isAutoBanEnabled() {
        return autoBan;
    }

    public static boolean areAlertsInConsoleEnabled() {
        return alertsInConsole;
    }

    public static String getBanCommand() {
        return banCommand;
    }

    public static int getMaxFlagsPerModule() {
        return maxFlagsPerModule;
    }

    public static int getMaxFlagsTotal() {
        return maxFlagsTotal;
    }
}
