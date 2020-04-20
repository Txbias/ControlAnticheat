package de.valorit.cac;

import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Config {

    private static final File CONFIG_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "config.yml");
    private static final File SETTINGS_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "settings.yml");
    private static YamlConfiguration config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
    private static YamlConfiguration settings = YamlConfiguration.loadConfiguration(SETTINGS_FILE);

    private static long minTimeBetweenBowShots;
    private static int maxPackets;
    private static int maxPing;
    private static double maxCombatRange;


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
    }

    public static void loadSettings() {
        if(!SETTINGS_FILE.exists()) {
            System.out.println("No settings file found.");
            createDefaultSettings();
            return;
        }
        List<String> enabled = settings.getStringList("notifications.enabled");

        for(String name : enabled) {
            Player p = Bukkit.getPlayer(Utils.getUUID(name));
            if(p.hasPermission(Permissions.NOTIFY) || p.hasPermission(Permissions.ADMIN) || p.isOp()) {
                CheckResultsManager.getUser(p).setReceivesNotifications(true);
            }
        }

    }

    public static void setReceivesNotifications(Player p, boolean value) {
        List<String> enabled = settings.getStringList("notifications.enabled");
        String UUID = Utils.getUUID(p.getName());

        if(value) {
            if(!enabled.contains(UUID)) {
                enabled.add(UUID);
            }
        } else {
            enabled.remove(UUID);
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

    public static double getMaxCombatRange() {
        return maxCombatRange;
    }

    public static boolean isReceivingNotifications(Player p) {
        if(p.hasPermission(Permissions.NOTIFY) || p.hasPermission(Permissions.ADMIN) || p.isOp()) {
            List<String> enabled = settings.getStringList("notifications.enabled");
            String UUID = Utils.getUUID(p.getName());
            return enabled.contains(UUID);
        }
        return false;
    }
}
