package de.valorit.cac.checks.movement;

import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class FlightCheck {

    private final Module NAME = Module.FLIGHT;
    private final CheckResult PASS = new CheckResult();

    private final HashMap<Player, Long> elytraByPass = new HashMap<>();


    private final double VANILLA_FLIGHT_DISTANCE = 0.96D;


    public CheckResult performCheck(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        User user = CheckResultsManager.getUser(p);

        if(!elytraByPass.containsKey(p)) {
            elytraByPass.put(p, System.currentTimeMillis());
            return PASS;
        }

        Location from = e.getFrom();
        Location to = e.getTo();

        Vector vec = to.toVector();
        double distance = vec.distance(from.toVector());

        if(p.getGameMode() == GameMode.CREATIVE || p.getAllowFlight() || p.isInsideVehicle() || PlayerUtils.isInLiquid(p) ||
            p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        if(user.isPushed()) {
            return PASS;
        }

        if(PlayerUtils.isInAir(p)) {
            if(p.getFallDistance() == 0.0f) {
                //Vanilla Fly
                if(distance > VANILLA_FLIGHT_DISTANCE) {
                    if(!p.isSneaking() && p.getLocation().getBlockY() - p.getEyeLocation().getBlockY() == 1) {
                        return PASS;
                    } else if(user.isUsingElytra()) {
                        if(System.currentTimeMillis() - elytraByPass.get(p) <= 100) {
                            //Player is hacking
                            punish(e);
                            return new CheckResult(NAME, true, p);
                        } else {
                            elytraByPass.replace(p, System.currentTimeMillis());
                        }
                    } else {
                        //Player is hacking
                        punish(e);
                        return new CheckResult(NAME, true, p);
                    }

                }
            }
        }
        return PASS;
    }

    private void punish(PlayerMoveEvent e) {
        e.getPlayer().teleport(e.getFrom());
        PlayerUtils.movePlayerDown(e.getPlayer());
    }


}
