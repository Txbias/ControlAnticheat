package de.valorit.cac.checks.movement;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
        Location from = e.getFrom();
        Location to = e.getTo();

        Vector vec = to.toVector();
        double vectorDistance = vec.distance(from.toVector());

        if(p.hasPermission("cac.bypass")) {
            return PASS;
        }


        if(vectorDistance == 0 || p.isInsideVehicle() || p.getFallDistance() > 1.2) {
            return PASS;
        }

        if(p.getGameMode() == GameMode.CREATIVE || PlayerUtils.isInLiquid(p)) {
            return PASS;
        }

        if(PlayerUtils.blockOverPlayer(p)) {
            maxDistance = 0.78;
        }

        if(p.hasPotionEffect(PotionEffectType.SPEED)) {
            maxDistance = 0.88;
        }
        if(vectorDistance > maxDistance) {
            //Player is hacking

            p.teleport(from);
            return new CheckResult(NAME, true, p);
        }

        return PASS;
    }

}
