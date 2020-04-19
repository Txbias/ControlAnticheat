package de.valorit.cac.commands;

import de.valorit.cac.utils.Utils;
import de.valorit.cac.utils.packets.PacketVersionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("ping")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if(p.isOp() || p.hasPermission("cac.ping") || p.hasPermission("cac.*")) {
                     if(args.length == 0) {
                         //Player asks for own ping
                         Utils.sendMessage(p, "Your ping: §6" + PacketVersionManager.getCraftPlayerManager().getPing(p) + "ms");
                         return true;
                     } else if(args.length == 1) {
                         //Player asks for the ping of other player
                         Player target = Bukkit.getPlayer(args[0]);
                         if(target == null) {
                             Utils.sendError(p, "The player §6" + args[0] + "§c is not online!");
                             return false;
                         }

                         Utils.sendMessage(p, args[0] + "'s ping: §6" +  PacketVersionManager.getCraftPlayerManager().getPing(target) + "ms");
                         return true;
                     } else {
                         Utils.sendError(p, "Usage: /ping <Player>");
                         return false;
                     }
                } else {
                    Utils.sendNoPerm(p);
                    return false;
                }
            } else {
                //Command executed by console
                if(args.length != 1) {
                    Utils.sendError(sender, "You need to be a player to be able to request your ping!");
                    return false;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if(target == null) {
                    Utils.sendError(sender, "The player §6" + args[0] + "§c is not online!");
                    return false;
                }

                Utils.sendMessage(sender, args[0] + "s ping: §6" + PacketVersionManager.getCraftPlayerManager().getPing(target) + "ms");
                return true;
            }
        }
        return false;
    }


}
