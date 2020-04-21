package de.valorit.cac.events;

import de.valorit.cac.checks.movement.TowerCheck;
import de.valorit.cac.checks.player.ReachCheck;
import de.valorit.cac.utils.GameEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceEvent extends GameEvent implements Listener {

    ReachCheck reachCheck = new ReachCheck();
    TowerCheck towerCheck = new TowerCheck();

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        addCheckResult(reachCheck.checkBlockPlace(e));
        addCheckResult(towerCheck.performCheck(e));
    }

}
