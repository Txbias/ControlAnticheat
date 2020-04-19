package de.valorit.cac.events;

import de.valorit.cac.checks.player.ReachCheck;
import de.valorit.cac.utils.GameEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakEvent extends GameEvent implements Listener {

    ReachCheck reachCheck = new ReachCheck();
    //Fucker fuckerCheck = new Fucker();


    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        addCheckResult(reachCheck.checkBlockBreak(e));
        //addCheckResult(fuckerCheck.performCheck(e));
    }

}
