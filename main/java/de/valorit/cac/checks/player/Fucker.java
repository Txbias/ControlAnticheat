package de.valorit.cac.checks.player;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.PlayerUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class Fucker {

    private final Module NAME = Module.FUCKER;
    private final CheckResult PASS = new CheckResult();

    //TODO
    public CheckResult performCheck(BlockBreakEvent e) {
        Player p = e.getPlayer();

        Block b = e.getBlock();
        Block target = p.getTargetBlock(null, 5);

        if(!(target.getType() == b.getType()) && !(PlayerUtils.isLiquid(target))) {
            //Player is hacking
            e.setCancelled(true);
            return new CheckResult(NAME, true, p);
        }

        return PASS;
    }

}
