package de.valorit.cac.utils;

import de.valorit.cac.checks.CheckResultsManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Settings {

    private static final File SETTINGS_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "settings.yml");
    private static YamlConfiguration settings = YamlConfiguration.loadConfiguration(SETTINGS_FILE);


    public static void loadSettings(Player p) {
        if(!SETTINGS_FILE.exists()) {
            createDefaultSettings();
            return;
        }
        List<String> disabled = settings.getStringList("notifications.disabled");

        String playerUUIDString = p.getUniqueId().toString();

        for(String uuidString : disabled) {
            if(uuidString.equalsIgnoreCase(playerUUIDString)) {
                if(p.hasPermission(Permissions.NOTIFY) || p.hasPermission(Permissions.ADMIN) || p.isOp()) {
                    CheckResultsManager.getUser(p).setReceivesNotifications(false);
                }
                return;
            }
        }

    }

    public static void setReceivesNotifications(Player p, boolean value) {
        List<String> disabled = settings.getStringList("notifications.disabled");
        UUID uuid = p.getUniqueId();

        if(!value) {
            if(!disabled.contains(uuid.toString())) {
                disabled.add(uuid.toString());
            }
        } else {
            disabled.remove(uuid.toString());
        }

        settings.set("notifications.disabled", disabled);
        saveSettings();
    }

    public static void saveSettings() {
        try {
            settings.save(SETTINGS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isReceivingNotifications(Player p) {
        if(p.hasPermission(Permissions.NOTIFY) || p.hasPermission(Permissions.ADMIN) || p.isOp()) {
            List<String> disabled = settings.getStringList("notifications.disabled");
            UUID uuid = p.getUniqueId();
            return !disabled.contains(uuid.toString());
        }
        return false;
    }

    private static void createDefaultSettings() {
        saveSettings();
        settings.set("notifications.disabled", new ArrayList<String>());
        saveSettings();
    }



}
