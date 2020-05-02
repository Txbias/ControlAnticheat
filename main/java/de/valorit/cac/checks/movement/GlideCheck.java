package de.valorit.cac.checks.movement;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import de.valorit.cac.utils.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class GlideCheck {

    private final Module NAME = Module.GlIDE;
    private final CheckResult PASS = new CheckResult();

    double lastDistance = -1;
    public CheckResult performCheck(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        User user = CheckResultsManager.getUser(p);

        Location from = e.getFrom();
        Location to = e.getTo();
        Vector vec = to.toVector();

        double vectorDistance = vec.distance(from.toVector());
        double distance = from.getY() - to.getY();

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        if(p.isOnGround() || p.isInsideVehicle() || p.getAllowFlight() || p.getLocation().getBlock().getType() ==
                Material.LADDER || p.getLocation().getBlock().getType() == Material.VINE || PlayerUtils.isInLiquid(p)) {
            lastDistance = distance;
            return PASS;
        }

        if(user.isUsingElytra()) {
            lastDistance = distance;
            return PASS;
        }


        if(distance == 0 && vectorDistance != 0 && p.getFallDistance() != 0 && p.getVelocity().getY() < -0.6) {
            //Player is hacking
            p.teleport(from);
            PlayerUtils.movePlayerDown(p);
            lastDistance = distance;
            return new CheckResult(NAME, true, p);
        }

        if(distance == 0) {
            lastDistance = distance;
            return PASS;
        }


        if(distance < 0.17d && distance != 0 && p.getVelocity().getY() < 0 && p.getVelocity().getY() > -0.2) {
            if(lastDistance == distance) {
                //Player is hacking
                p.teleport(from);
                PlayerUtils.movePlayerDown(p);
                lastDistance = distance;
                return new CheckResult(NAME, true, p);
            }
        }
        lastDistance = distance;
        return PASS;
    }

}
