package de.valorit.cac.checks.movement;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.PlayerUtils;
import de.valorit.cac.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class FlightCheck {

    private final Module NAME = Module.FLIGHT;
    private final CheckResult PASS = new CheckResult();

    private final double VANILLA_FLIGHT_DISTANCE = 0.96D;

    double lastDifferenceY = -1;
    double lastDistance = -1;
    public CheckResult performCheck(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        Location from = e.getFrom();
        Location to = e.getTo();
        double differenceY = from.getY() - to.getY();

        Vector vec = to.toVector();
        double distance = vec.distance(from.toVector());

        if(p.getGameMode() == GameMode.CREATIVE || p.getAllowFlight() || p.isInsideVehicle() || PlayerUtils.isInLiquid(p) ||
            p.hasPermission("cac.bypass")) {
            lastDistance = Utils.round(distance);
            lastDifferenceY = differenceY;
            return PASS;
        }

        if(PlayerUtils.isInAir(p)) {
            if(p.getFallDistance() == 0.0f) {
                //Vanilla Fly
                if(distance > VANILLA_FLIGHT_DISTANCE) {
                    //Player is hacking
                    punish(e);
                    lastDifferenceY = differenceY;
                    return new CheckResult(NAME, true, p);
                }
            }
        }
        lastDistance = Utils.round(distance);
        lastDifferenceY = differenceY;
        return PASS;
    }

    private void punish(PlayerMoveEvent e) {
        e.getPlayer().teleport(e.getFrom());
        PlayerUtils.movePlayerDown(e.getPlayer());
    }


}
