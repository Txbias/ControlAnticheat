package de.valorit.cac.checks.movement;

import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import de.valorit.cac.utils.version_dependent.VersionManager;
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
    private final HashMap<Player, Double> lastDistance = new HashMap<>();
    private final HashMap<Player, Float> lastFallDistance = new HashMap<>();
    private final HashMap<Player, Long> lastElytra = new HashMap<>();


    private final double VANILLA_FLIGHT_DISTANCE = 0.96D;


    public CheckResult performCheck(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        User user = CheckResultsManager.getUser(p);

        Location from = e.getFrom();
        Location to = e.getTo();

        Vector vec = to.toVector();
        double distance = vec.distance(from.toVector());

        if(!elytraByPass.containsKey(p)) {
            elytraByPass.put(p, System.currentTimeMillis());
            lastElytra.put(p, System.currentTimeMillis());
            lastDistance.put(p, distance);
            lastFallDistance.put(p, p.getFallDistance());
            return PASS;
        }

        if(p.getGameMode() == GameMode.CREATIVE || p.getAllowFlight() || p.isInsideVehicle() || PlayerUtils.isInLiquid(p) ||
            p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        if(user.isPushed()) {
            updateHashMaps(e);
            return PASS;
        }

        if(user.isUsingElytra()) {
            lastElytra.replace(p, System.currentTimeMillis());
        }

        if(PlayerUtils.isInAir(p)) {
                //Vanilla Fly
                if(distance > VANILLA_FLIGHT_DISTANCE) {
                    if(p.getFallDistance() == 0) {
                        if (VersionManager.getVersion().equals("v1_12_R1")) { // Check if elytras exist
                            if (!p.isSneaking() && p.getLocation().getBlockY() - p.getEyeLocation().getBlockY() == 1) {
                                lastElytra.replace(p, System.currentTimeMillis());
                                return PASS;
                            } else if (user.isUsingElytra()) {
                                lastElytra.replace(p, System.currentTimeMillis());
                                if (System.currentTimeMillis() - elytraByPass.get(p) <= 45) {
                                    //Player is hacking
                                    updateHashMaps(e);
                                    punish(e);
                                    System.out.println(1);
                                    System.out.println(System.currentTimeMillis() - elytraByPass.get(p));
                                    return new CheckResult(NAME, true, p);
                                } else {
                                    elytraByPass.replace(p, System.currentTimeMillis());
                                }
                            } else {
                                //Player is hacking
                                if(System.currentTimeMillis() - lastElytra.get(p) <= 100) {
                                    updateHashMaps(e);
                                    return PASS;
                                }
                                System.out.println(2);
                                updateHashMaps(e);
                                punish(e);
                                return new CheckResult(NAME, true, p);

                            }
                        } else {
                            System.out.println(3);
                            updateHashMaps(e);
                            punish(e);
                            return new CheckResult(NAME, true, p);
                        }
                    }
                } else if(lastDistance.get(p) == distance && p.getFallDistance() == lastFallDistance.get(p)) {
                    if(distance != 0) {
                        updateHashMaps(e);
                        punish(e);
                        return new CheckResult(NAME, true, p);
                    } else if(!PlayerUtils.isPlatformBelow(p)) {
                        updateHashMaps(e);
                        punish(e);
                        return new CheckResult(NAME, true, p);
                    }
                }
        }
        updateHashMaps(e);
        return PASS;
    }

    private void punish(PlayerMoveEvent e) {
        e.getPlayer().teleport(e.getFrom());
        PlayerUtils.movePlayerDown(e.getPlayer());
    }

    private void updateHashMaps(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        lastDistance.replace(p, e.getTo().toVector().distance(e.getFrom().toVector()));
        lastFallDistance.replace(p, p.getFallDistance());
    }


}
