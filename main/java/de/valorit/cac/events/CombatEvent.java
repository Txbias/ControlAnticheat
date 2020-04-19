package de.valorit.cac.events;

import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.checks.combat.FastBow;
import de.valorit.cac.checks.player.ReachCheck;
import de.valorit.cac.utils.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class CombatEvent extends GameEvent implements Listener {

    FastBow fastBow = new FastBow();
    ReachCheck reachCheck = new ReachCheck();

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        addCheckResult(fastBow.performCheck(e));
    }

    @EventHandler
    public void onSwitch(PlayerItemHeldEvent e) {
        fastBow.updateUser(e);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        fastBow.updateUser(e);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        addCheckResult(reachCheck.performCheck(e));

        if(e.getDamager() instanceof Player) {
            User user = CheckResultsManager.getUser((Player) e.getDamager());
            if(!user.canAttack()) {
                e.setCancelled(true);
                addCheckResult(new CheckResult(Module.KILLAURA, true, (Player) e.getDamager()));
            }

        }

    }

}
