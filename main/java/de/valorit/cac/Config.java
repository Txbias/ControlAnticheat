package de.valorit.cac;

import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Config {

    private static final File CONFIG_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "config.yml");
    private static final File SETTINGS_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "settings.yml");
    private static YamlConfiguration config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
    private static YamlConfiguration settings = YamlConfiguration.loadConfiguration(SETTINGS_FILE);

    private static long minTimeBetweenBowShots;
    private static int maxPackets;
    private static int maxPing;
    private static double maxCombatRange;

    private static boolean autoBan;
    private static String banCommand;
    private static int maxFlagsPerModule;
    private static int maxFlagsTotal;



    public static void loadConfig() {
        if(!CONFIG_FILE.exists()) {
            System.out.println(Utils.PREFIX + "No config found.");
            System.out.println(Utils.PREFIX + "Creating default config...");
            createDefaultConfig();
        }

        minTimeBetweenBowShots = config.getLong("minTimeBetweenBowShots");
        maxPackets = config.getInt("maxPackets");
        maxPing = config.getInt("maxPing");
        maxCombatRange = config.getDouble("maxCombatRange");

        autoBan = config.getBoolean("autoBan");
        banCommand = config.getString("banCommand");
        maxFlagsPerModule = config.getInt("maxFlagsPerModule");
        maxFlagsTotal = config.getInt("maxFlagsTotal");
    }

    public static void loadSettings(Player p) {
        if(!SETTINGS_FILE.exists()) {
            System.out.println("No settings file found.");
            createDefaultSettings();
            return;
        }
        List<String> enabled = settings.getStringList("notifications.enabled");

        String playerUUIDString = p.getUniqueId().toString();

        for(String uuidString : enabled) {
            if(uuidString.equalsIgnoreCase(playerUUIDString)) {
                if(p.hasPermission(Permissions.NOTIFY) || p.hasPermission(Permissions.ADMIN) || p.isOp()) {
                    CheckResultsManager.getUser(p).setReceivesNotifications(true);
                }
                return;
            }
        }

    }

    public static void setReceivesNotifications(Player p, boolean value) {
        List<String> enabled = settings.getStringList("notifications.enabled");
        UUID uuid = p.getUniqueId();

        if(value) {
            if(!enabled.contains(uuid.toString())) {
                enabled.add(uuid.toString());
            }
        } else {
            enabled.remove(uuid.toString());
        }

        settings.set("notifications.enabled", enabled);
        saveSettings();
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
        settings = YamlConfiguration.loadConfiguration(SETTINGS_FILE);
        loadConfig();
    }

    public static void saveConfig() {
        try {
            config.save(CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSettings() {
        try {
            settings.save(SETTINGS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig() {
        saveConfig(); // Create file
        config.set("minTimeBetweenBowShots", 149);
        config.set("maxPackets", 80);
        config.set("maxPing", 300);
        config.set("maxCombatRange", 4);
        config.set("autoBan", true);
        config.set("banCommand", "/ban [player]");
        config.set("maxFlagsPerModule", 15);
        config.set("maxFlagsTotal", 30);
        saveConfig();
    }

    private static void createDefaultSettings() {
        saveSettings();
        settings.set("notifications.enabled", new ArrayList<String>());
        saveSettings();
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

    public static int getMaxFlagsPerModule() {
        return maxFlagsPerModule;
    }

    public static int getMaxFlagsTotal() {
        return maxFlagsTotal;
    }

    public static double getMaxCombatRange() {
        return maxCombatRange;
    }

    public static boolean isAutoBanEnabled() {
        return autoBan;
    }

    public static boolean isReceivingNotifications(Player p) {
        if(p.hasPermission(Permissions.NOTIFY) || p.hasPermission(Permissions.ADMIN) || p.isOp()) {
            List<String> enabled = settings.getStringList("notifications.enabled");
            UUID uuid = p.getUniqueId();
            return enabled.contains(uuid.toString());
        }
        return false;
    }

    public static String getBanCommand() {
        return banCommand;
    }
}
