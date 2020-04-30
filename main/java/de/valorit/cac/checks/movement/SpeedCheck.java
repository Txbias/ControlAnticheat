package de.valorit.cac.checks.movement;

import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class SpeedCheck {

    private final Module NAME = Module.SPEED;
    private final CheckResult PASS = new CheckResult();

    private double maxDistance = 0.75;

    public CheckResult performCheck(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        User user = CheckResultsManager.getUser(p);
        Location from = e.getFrom();
        Location to = e.getTo();

        Vector vec = to.toVector();
        double vectorDistance = vec.distance(from.toVector());

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        if(user.isPushed()) {
            return PASS;
        }

        if(user.isUsingElytra()) {
            return PASS;
        }

        if(vectorDistance == 0 || p.isInsideVehicle() || p.getFallDistance() > 1.2) {
            return PASS;
        }

        if(p.getGameMode() == GameMode.CREATIVE || PlayerUtils.isInLiquid(p)) {
            return PASS;
        }

        if(PlayerUtils.isLiquid(PlayerUtils.getBlockUnderPlayer(p))) {
            return PASS;
        }
        
        if(PlayerUtils.getBlockMaterialUnderPlayer(p) == Material.AIR) {
            return PASS;
        }

        if(PlayerUtils.blockOverPlayer(p)) {
            maxDistance = 0.78;
        }

        if(p.hasPotionEffect(PotionEffectType.SPEED)) {
            maxDistance = 0.88;
        }
        if(vectorDistance > maxDistance) {
            if(p.getVelocity().getY() != 0 && (p.getVelocity().getX() != 0 || p.getVelocity().getZ() != 0)) {
                if(!(p.getVelocity().getY() > 0.75) && vectorDistance < 0.8) {
                    //Player is hacking
                    System.out.println("Velocity: " + p.getVelocity());
                    System.out.println("distance: " + vectorDistance);
                    p.teleport(from);
                    return new CheckResult(NAME, true, p);
                }
            }
        }

        return PASS;
    }

}
