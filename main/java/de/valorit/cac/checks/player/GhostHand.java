package de.valorit.cac.checks.player;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GhostHand {

    private final Module NAME = Module.GHOSTHAND;
    private final CheckResult PASS = new CheckResult();

    //TODO
    public CheckResult performCheck(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return PASS;
        }
        if(b.getType() == Material.CHEST) {
            if(p.getTargetBlock(null, 5).getType() != Material.CHEST) {
                //Player is hacking
                e.setCancelled(true);
                return new CheckResult(NAME, true, p);
            }
        }
        return PASS;
    }

}
