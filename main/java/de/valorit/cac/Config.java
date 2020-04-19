package de.valorit.cac;

import de.valorit.cac.utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public class Config {

    private static final File CONFIG_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "config.yml");
    private static YamlConfiguration config = YamlConfiguration.loadConfiguration(CONFIG_FILE);

    private static long minTimeBetweenBowShots;
    private static int maxPackets;
    private static int maxPing;
    private static double maxCombatRange;


    public static void loadConfig() {
        if(!CONFIG_FILE.exists()) {
            System.out.println(Utils.PREFIX + "No config found.");
            System.out.println(Utils.PREFIX + "Creating default config...");
            save();
            createDefaultConfig();
        }

        minTimeBetweenBowShots = config.getLong("minTimeBetweenBowShots");
        maxPackets = config.getInt("maxPackets");
        maxPing = config.getInt("maxPing");
        maxCombatRange = config.getDouble("maxCombatRange");
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
        loadConfig();
    }

    public static void save() {
        try {
            config.save(CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig() {
        save(); // Create file
        config.set("minTimeBetweenBowShots", 149);
        config.set("maxPackets", 80);
        config.set("maxPing", 300);
        config.set("maxCombatRange", 4);
        save();
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
}
