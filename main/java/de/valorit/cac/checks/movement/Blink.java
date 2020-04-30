package de.valorit.cac.checks.movement;

import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.GameEvent;
import de.valorit.cac.utils.Permissions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Blink {

    private final Module NAME = Module.BLINK;

    Location lastLocation = null;
    long lastPositionPacket = -1;
    long lastDifference = -1;
    public void performCheck(Player p) {

        User user = CheckResultsManager.getUser(p);
        long joinTime = user.getJoinTime();

        if(p.hasPermission(Permissions.BYPASS)) {
            return;
        }

        if(user.isPushed()) {
            return;
        }

        if(user.isUsingElytra()) {
            return;
        }

        if(lastLocation == null) {
            lastLocation = p.getLocation();
        }
        if(lastPositionPacket == -1) {
            lastPositionPacket = System.currentTimeMillis();
        }

        if(System.currentTimeMillis() - lastPositionPacket == 0 && System.currentTimeMillis() - joinTime > 5000 &&
            lastDifference == 0) {
            p.teleport(lastLocation);
            GameEvent.addCheckResult(new CheckResult(NAME, true, p));
        }

        lastDifference = System.currentTimeMillis() - lastPositionPacket;
        lastLocation = p.getLocation();
        lastPositionPacket = System.currentTimeMillis();
    }

}
