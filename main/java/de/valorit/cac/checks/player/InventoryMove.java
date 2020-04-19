package de.valorit.cac.checks.player;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class InventoryMove {

    private final Module NAME = Module.INVENTORYMOVE;
    private final CheckResult PASS = new CheckResult();

    public CheckResult performCheck(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if(p.getGameMode() == GameMode.CREATIVE) {
            return PASS;
        }

        if(!PlayerUtils.isInAir(p) && p.getFallDistance() == 0) {
            if(CheckResultsManager.getUser(p).isInventoryOpen()) {
                p.teleport(e.getFrom());
                CheckResultsManager.getUser(p).setInventoryOpen(false);
                return new CheckResult(NAME, true, p);
            }
        }
        return PASS;
    }

}
