package de.valorit.cac.checks.player;

import de.valorit.cac.Config;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.version_dependent.VersionManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class ReachCheck {

    private final Module NAME = Module.REACH;
    private final CheckResult PASS = new CheckResult();

    double maxDistanceSurvival = 5.87;
    double maxDistanceCreative = 6.48;

    public CheckResult performCheck(EntityDamageByEntityEvent e) {

        if(!(e.getDamager() instanceof Player)) {
            return PASS;
        }

        Player p = (Player) e.getDamager();
        Entity damaged = e.getEntity();

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        if(VersionManager.getCraftPlayerManager().getPing(p) > Config.getMaxPing()) {
            return PASS;
        }

        Location playerLocation = p.getEyeLocation();
        Location damagedLocation;
        if(damaged instanceof  Player) {
            damagedLocation = ((Player) damaged).getEyeLocation();
        } else {
            damagedLocation = damaged.getLocation();
        }


        double distance = playerLocation.distance(damagedLocation);

        if(distance > Config.getMaxCombatRange() && playerLocation.getY() > damagedLocation.getY()) {
            e.setCancelled(true);
            return new CheckResult(NAME, true, p);
        }

        if(distance > Config.getMaxCombatRange() && playerLocation.getY() == damagedLocation.getY()) {
            //Player is hacking
            e.setCancelled(true);
            return new CheckResult(NAME, true, p);
        }

        if(playerLocation.getY() < damagedLocation.getY()) {
            if(playerLocation.distance(damaged.getLocation()) > Config.getMaxCombatRange()) { // Check with location of the feet
                e.setCancelled(true);
                return new CheckResult(NAME, true, p);
            }
        }

        return PASS;
    }


    //TODO: Fix reach
    public CheckResult checkBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        Location loc = e.getBlock().getLocation();
        Location playerLocation = p.getEyeLocation();

        Vector vec = loc.toVector();
        double distance = vec.distance(playerLocation.toVector());

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        if(p.getGameMode() == GameMode.CREATIVE) {
            if (distance > maxDistanceCreative) {
                //Player is hacking
                e.setCancelled(true);
                return new CheckResult(NAME, true, p);
            }
        } else {
            if(distance > maxDistanceSurvival) {
                //Player is hacking
                e.setCancelled(true);
                return new CheckResult(NAME, true, p);
            }
        }
        return PASS;
    }


    public CheckResult checkBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        Location loc = e.getBlock().getLocation();
        Location playerLocation = p.getEyeLocation();

        Vector vec = loc.toVector();
        double distance = vec.distance(playerLocation.toVector());

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        if(p.getGameMode() == GameMode.CREATIVE) {
            if(distance > maxDistanceCreative) {
                //Player is hacking
                e.setCancelled(true);
                return new CheckResult(NAME, true, p);
            }
        } else {
            if(distance > maxDistanceSurvival) {
                //Player is hacking
                e.setCancelled(true);
                return new CheckResult(NAME, true, p);
            }
        }
        return PASS;
    }

}
