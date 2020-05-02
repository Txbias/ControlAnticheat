package de.valorit.cac.commands;

import de.valorit.cac.config.Messages;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.Utils;
import de.valorit.cac.version_dependent.VersionManager;
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
                if(p.isOp() || p.hasPermission(Permissions.PING) || p.hasPermission(Permissions.ADMIN)) {
                     if(args.length == 0) {
                         //Player asks for own ping
                         Utils.sendMessage(p, Messages.getPingInfo(VersionManager.getCraftPlayerManager().getPing(p)));
                         return true;
                     } else if(args.length == 1) {
                         //Player asks for the ping of other player
                         Player target = Bukkit.getPlayer(args[0]);
                         if(target == null) {
                             Utils.sendError(p, Messages.getNotOnline(args[0]));
                             return false;
                         }

                         Utils.sendMessage(p, Messages.getPingInfoPlayer(target, VersionManager.getCraftPlayerManager().getPing(target)));
                         return true;
                     } else {
                         Utils.sendError(p, Messages.getUsagePing());
                         return false;
                     }
                } else {
                    Utils.sendNoPerm(p);
                    return false;
                }
            } else {
                //Command executed by console
                if(args.length != 1) {
                    Utils.sendError(sender, Messages.getNoConsole());
                    return false;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if(target == null) {
                    Utils.sendError(sender, Messages.getNotOnline(args[0]));
                    return false;
                }

                Utils.sendMessage(sender, Messages.getPingInfoPlayer(target, VersionManager.getCraftPlayerManager().getPing(target)));
                return true;
            }
        }
        return false;
    }


}
