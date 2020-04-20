package de.valorit.cac.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.utils.packets.PacketVersionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


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
    }

    public static void sendNoPerm(Player p) {
        p.sendMessage(PREFIX + "§4You don't have the required permission to do that!");
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
        sendError(sender, "The player §6" + notOnline + "§c is not online!");
    }

    public static double round(double num) {
        return (double) Math.round(num * 10000d) / 10000d;
    }

    public static String getUUID(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder contentBuilder = new StringBuilder();

            String str;
            while((str = reader.readLine()) != null) {
                contentBuilder.append(str);
            }
            reader.close();

            String content = contentBuilder.toString();

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            MojangAPIResponse response = gson.fromJson(content, MojangAPIResponse.class);

            return response.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void sendCheckResult(Player receiver, CheckResult result) {
        User user = CheckResultsManager.getUser(result.getPlayer());
        int level = user.getLevel(result.getModule());

        receiver.sendMessage(PREFIX + " The player §c" + user.getPlayer().getName() + "§7 ("+ PacketVersionManager.getCraftPlayerManager().getPing(user.getPlayer())
                + "ms) failed " + result.getModule() + " (vl: §c" + level + "§7)");
    }


    static class MojangAPIResponse {

        private String id;
        private String name;

        public MojangAPIResponse(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }


}
