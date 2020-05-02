package de.valorit.cac.commands;

import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.config.Config;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.User;
import de.valorit.cac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CacCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("cac")) {
            if(args.length == 2 && args[0].equalsIgnoreCase("info")) {
                //Displays info about a player
                return handleInfo(sender, args);
            } else if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                //Reloads the config
                return handleReload(sender, args);
            } else if(args.length == 1 && args[0].equalsIgnoreCase("notify")) {
                //Disables notifications for a staff member
                return handleNotify(sender, args);
            } else if(args.length >= 3 && args[0].equalsIgnoreCase("setup")) {
                //Lets you set the ban command
                return handleSetup(sender, args);
            }
            else {
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    if(!p.hasPermission(Permissions.ADMIN) && !p.hasPermission(Permissions.RELOAD) && !p.isOp() &&
                            !p.hasPermission(Permissions.INFO) && !p.hasPermission(Permissions.NOTIFY)) {
                        Utils.sendNoPerm(p);
                        return false;
                    }
                }

                String message = "§7-------------§6CAC - Help§7-------------\n" +
                        "§6/cac info [name]§7: Shows which cheats were detected for a player\n" +
                        "§6/cac reload§7: Reloads the config\n" +
                        "§6/cac notify§7: Toggles your notifications\n" +
                        "§6/cac setup [ban command]§7: Lets you set the ban command. (You can use [player] as a placeholder)\n" +
                        "§7-------------§6CAC - Help§7-------------\n";
                sender.sendMessage(message);
            }
        }
        return false;
    }


    private boolean handleInfo(CommandSender sender, String[] args) {
        //Displays info about a player
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.hasPermission(Permissions.ADMIN) && !p.hasPermission(Permissions.INFO) && !p.isOp()) {
                Utils.sendNoPerm(p);
                return false;
            }
        }

        Player target = Bukkit.getPlayer(args[1]);

        if(target == null) {
            Utils.sendNotOnline(sender, args[1]);
            return false;
        }

        User user = CheckResultsManager.getUser(target);

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("§7-------------§6Info - ").append(target.getName()).append("§7-------------\n");

        int detectedCheats = 0;
        for(int i = 0; i < user.getLevels().size(); i++) {
            int level = user.getLevel(i);
            if(level > 0) {
                detectedCheats++;
                messageBuilder.append("§7");
                messageBuilder.append(user.getModule(i));
                messageBuilder.append(": §e");
                messageBuilder.append(level);
                messageBuilder.append("\n");
            }
        }

        if(detectedCheats == 0) {
            Utils.sendMessage(sender, "The player §6" + target.getName() + "§7 wasn't detected for any cheats!");
            return true;
        }

        messageBuilder.append("§7-------------§6Info - ").append(target.getName()).append("§7-------------");

        sender.sendMessage(messageBuilder.toString());
        return true;
    }


    private boolean handleReload(CommandSender sender, String[] args) {
        if(sender instanceof  Player) {
            Player p = (Player) sender;
            if(!p.hasPermission(Permissions.ADMIN) && !p.hasPermission(Permissions.RELOAD) && !p.isOp()) {
                Utils.sendNoPerm(p);
                return false;
            }
        }

        Config.reloadConfig();
        Utils.sendMessage(sender, "Config reloaded.");

        return true;
    }


    private boolean handleNotify(CommandSender sender, String[] args){
        if(!(sender instanceof Player)) {
            Utils.sendMessageToConsole(sender, "You need to be a player to be able to do that!");
            return false;
        }
        Player p = (Player) sender;
        if(!p.hasPermission(Permissions.NOTIFY) && !p.hasPermission(Permissions.ADMIN) && !p.isOp()) {
            Utils.sendNoPerm(p);
            return false;
        }
        User user = CheckResultsManager.getUser(p);
        if(user.isReceivesNotifications()) {
            user.setReceivesNotifications(false);
            Utils.sendMessage(p, "You notifications are now disabled.");
        } else {
            user.setReceivesNotifications(true);
            Utils.sendMessage(p, "You notifications are now enabled.");
        }
        return true;
    }


    private boolean handleSetup(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.hasPermission(Permissions.ADMIN)) {
                Utils.sendNoPerm(p);
                return false;
            }
        }

        StringBuilder commandBuilder = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            if(i != 0) {
                commandBuilder.append(args[i]).append(" ");
            }
        }

        String command = commandBuilder.toString();
        Config.setBanCommand(command);

        String message = "The ban command was set to §6" + command;
        if(sender instanceof Player) {
            Utils.sendMessage(sender, message);
        } else {
            Utils.sendMessageToConsole(sender, message);
        }

        return true;
    }

}
