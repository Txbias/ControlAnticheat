package de.valorit.cac.events;

import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.movement.FlightCheck;
import de.valorit.cac.checks.movement.GlideCheck;
import de.valorit.cac.checks.movement.SpeedCheck;
import de.valorit.cac.checks.movement.WaterWalk;
import de.valorit.cac.checks.player.InventoryMove;
import de.valorit.cac.checks.player.NoFall;
import de.valorit.cac.utils.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class MoveEvent extends GameEvent implements Listener {

    GlideCheck glideCheck = new GlideCheck();
    FlightCheck flightCheck = new FlightCheck();
    SpeedCheck speedCheck = new SpeedCheck();
    InventoryMove inventoryMoveCheck = new InventoryMove();
    NoFall noFallCheck = new NoFall();
    WaterWalk waterWalkCheck = new WaterWalk();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        addCheckResult(flightCheck.performCheck(e));
        addCheckResult(glideCheck.performCheck(e));
        addCheckResult(speedCheck.performCheck(e));
        addCheckResult(inventoryMoveCheck.performCheck(e));
        addCheckResult(noFallCheck.performCheck(e));
        addCheckResult(waterWalkCheck.performCheck(e));


        Player p = e.getPlayer();
        User user = CheckResultsManager.getUser(p);

        if(user.isUsingElytra() && p.isFlying()) {
            user.setUsingElytra(false);
        }
    }



}
