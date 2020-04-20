package de.valorit.cac.checks.combat;

import de.valorit.cac.Config;
import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class FastBow {

    private final Module NAME = Module.FASTBOW;
    private final CheckResult PASS = new CheckResult();

    public CheckResult performCheck(EntityShootBowEvent e) {
        if(!(e.getEntity() instanceof Player)) {
            return PASS;
        }
        Player p = (Player) e.getEntity();

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        User user = CheckResultsManager.getUser(p);

        if(System.currentTimeMillis() - user.getBowStarted() < Config.getMinTimeBetweenBowShots()) {
            e.setCancelled(true);
            return new CheckResult(NAME, true, p);
        }

        user.setUsingBow(false);
        return new CheckResult(NAME, false, p);
    }

    public void updateUser(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getItem() == null) {
            return;
        }
        if(e.getItem().getType() != Material.BOW) {
            return;
        }
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if(!p.getInventory().contains(Material.ARROW)) {
            return;
        }

        User user = CheckResultsManager.getUser(p);
        user.setUsingBow(true);
    }

    public void updateUser(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        User user = CheckResultsManager.getUser(p);
        user.setUsingBow(false);
    }

}
