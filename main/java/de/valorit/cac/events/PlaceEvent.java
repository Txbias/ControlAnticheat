package de.valorit.cac.events;

import de.valorit.cac.checks.player.ReachCheck;
import de.valorit.cac.utils.GameEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceEvent extends GameEvent implements Listener {

    ReachCheck reachCheck = new ReachCheck();

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        addCheckResult(reachCheck.checkBlockPlace(e));
    }

}
