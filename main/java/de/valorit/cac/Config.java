package de.valorit.cac;

import de.valorit.cac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private static final Main PLUGIN = Main.getInstance();

    private static final File CONFIG_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "config.yml");

    private static FileConfiguration config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
    private static FileConfiguration mainConfig = Main.getInstance().getConfig();

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

        minTimeBetweenBowShots = mainConfig.getLong("minTimeBetweenBowShots");
        maxPackets = mainConfig.getInt("maxPackets");
        maxPing = mainConfig.getInt("maxPing");
        maxCombatRange = mainConfig.getDouble("maxCombatRange");

        autoKick = mainConfig.getBoolean("autoKick");
        autoBan = mainConfig.getBoolean("autoBan");
        banCommand = mainConfig.getString("banCommand");
        maxFlagsPerModule = mainConfig.getInt("maxFlagsPerModule");
        maxFlagsTotal = mainConfig.getInt("maxFlagsTotal");
        alertsInConsole = mainConfig.getBoolean("alertsInConsole");
    }

    public static void reloadConfig() {
        try {
            mainConfig = Main.getInstance().getConfig();
            mainConfig.load(CONFIG_FILE);
            loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            Utils.sendMessageToConsole(Bukkit.getServer().getConsoleSender(), "Couldn't reload config.");
        }
    }

    public static void setBanCommand(String banCommand) {
        config.set("banCommand", banCommand);
        try {
            config.save(CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
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
