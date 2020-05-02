package de.valorit.cac.config;

import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Messages {

    private final static File MESSAGES_FILE = new File("plugins/" + Utils.PLUGIN_NAME, "messages.yml");
    private static YamlConfiguration config = YamlConfiguration.loadConfiguration(MESSAGES_FILE);

    private static String banBroadcast = "The player §6[player]§7 was banned.";
    private static String cacCommandInfo = "§7-------------§6CAC - Help§7-------------\n" +
            "§6/cac info [name]§7: Shows which cheats were detected for a player\n" +
            "§6/cac reload§7: Reloads the config\n" +
            "§6/cac notify§7: Toggles your notifications\n" +
            "§6/cac setup [ban command]§7: Lets you set the ban command. (You can use [player] as a placeholder)\n" +
            "§7-------------§6CAC - Help§7-------------\n";
    private static String pingInfo = "Your ping: §6[ping]ms";
    private static String pingInfoPlayer = "[player]'s ping: §6[ping]ms";
    private static String noCheats = "The player §6 [player] §7 wasn't detected for any cheats!";
    private static String configReloaded = "Config reloaded.";
    private static String noConsole = "You need to be a player to be able to do that!";
    private static String notificationsEnabled = "You notifications are now enabled.";
    private static String notificationsDisabled = "You notifications are now disabled.";
    private static String banCommandSet = "The ban command was set to §6 [command]";
    private static String notOnline = "The player §6[player]§c is not online!";
    private static String usagePing = "Usage: /ping <Player>";
    private static String noPermission = "§4You don't have the required permission to do that!";
    private static String checkResult = "The player §c[player]§7 ([ping]ms) failed [hack] (vl: §c[level]§7)";
    private static String toManyPackets = "The player §c[player] §e is sending to many packets ([packets])!";
    private static String toManyAttacks = "The player §c [player] §e is attacking to many entities ([attacks])!";
    private static String compatible = "CAC is compatible!";
    private static String inCompatible = "The server version is not compatible with CAC!\nShutting down plugin...";


    public static void loadMessages() {
        if(!MESSAGES_FILE.exists()) {
            save();
            createDefaultConfig();
        }

        banBroadcast = config.getString("banBroadcast");
        cacCommandInfo = config.getString("cacCommandInfo");
        pingInfo = config.getString("pingInfo");
        pingInfoPlayer = config.getString("pingInfoPlayer");
        noCheats = config.getString("noCheats");
        configReloaded = config.getString("configReloaded");
        noConsole = config.getString("noConsole");
        notificationsEnabled = config.getString("notificationsEnabled");
        notificationsDisabled = config.getString("notificationsDisabled");
        banCommandSet = config.getString("banCommandSet");
        notOnline = config.getString("notOnline");
        usagePing = config.getString("usagePing");
        noPermission = config.getString("noPermission");
        checkResult = config.getString("checkResult");
        toManyPackets = config.getString("toManyPackets");
        toManyAttacks = config.getString("toManyAttacks");
        compatible = config.getString("compatible");
        inCompatible = config.getString("inCompatible");
    }

    public static void reloadMessages() {
        config = YamlConfiguration.loadConfiguration(MESSAGES_FILE);
        loadMessages();
    }

    private static void createDefaultConfig() {
        config.set("banBroadcast", banBroadcast);
        config.set("cacCommandInfo", cacCommandInfo);
        config.set("pingInfo", pingInfo);
        config.set("pingInfoPlayer", pingInfoPlayer);
        config.set("noCheats", noCheats);
        config.set("configReloaded", configReloaded);
        config.set("noConsole", noConsole);
        config.set("notificationsEnabled", notificationsEnabled);
        config.set("notificationsDisabled", notificationsDisabled);
        config.set("banCommandSet", banCommandSet);
        config.set("notOnline", notOnline);
        config.set("usagePing", usagePing);
        config.set("noPermission", noPermission);
        config.set("checkResult", checkResult);
        config.set("toManyPackets", toManyPackets);
        config.set("toManyAttacks", toManyAttacks);
        save();
    }

    private static void save() {
        try {
            config.save(MESSAGES_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //region getters
    public static String getBanBroadcast(Player p) {
        return banBroadcast.replace("[player]", p.getName());
    }

    public static String getCacCommandInfo() {
        return cacCommandInfo;
    }

    public static String getPingInfo(int ping) {
        return pingInfo.replace("[ping]", String.valueOf(ping));
    }

    public static String getPingInfoPlayer(Player p, int ping) {
        return pingInfoPlayer.replace("[player]", p.getName()).replace("[ping]", String.valueOf(ping));
    }

    public static String getNoCheats(Player p) {
        return noCheats.replace("[player]", p.getName());
    }

    public static String getConfigReloaded() {
        return configReloaded;
    }

    public static String getNoConsole() {
        return noConsole;
    }

    public static String getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public static String getNotificationsDisabled() {
        return notificationsDisabled;
    }

    public static String getBanCommandSet(String command) {
        return banCommandSet.replace("[command]", command);
    }

    public static String getNotOnline(String name) {
        return notOnline.replace("[player]", name);
    }

    public static String getUsagePing() {
        return usagePing;
    }

    public static String getNoPermission() {
        return noPermission;
    }

    public static String getCheckResult(Player p, int ping, Module hack, int level) {
        String copy = checkResult.replace("[player]", p.getName());
        copy = copy.replace("[ping]", String.valueOf(ping));
        copy = copy.replace("[hack]", String.valueOf(hack));
        copy = copy.replace("[level]", String.valueOf(level));
        return copy;
    }

    public static String getToManyPackets(Player p, int packets) {
        return toManyPackets.replace("[player]", p.getName()).replace("[packets]", String.valueOf(packets));
    }

    public static String getToManyAttacks(Player p, int attacks) {
        return toManyAttacks.replace("[player]", p.getName()).replace("[attacks]", String.valueOf(attacks));
    }

    public static String getCompatible() {
        return compatible;
    }

    public static String getInCompatible() {
        return inCompatible;
    }
    //endregion

}
