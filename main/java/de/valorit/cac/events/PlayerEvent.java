package de.valorit.cac.events;

import de.valorit.cac.Main;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.player.GhostHand;
import de.valorit.cac.utils.GameEvent;
import de.valorit.cac.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerEvent extends GameEvent implements Listener {

    GhostHand ghostHandCheck = new GhostHand();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        CheckResultsManager.getUser(p).setInventoryOpen(true);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            CheckResultsManager.getUser(p).setInventoryOpen(false);
        }, 3);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        addCheckResult(ghostHandCheck.performCheck(e));
    }

    @EventHandler
    public void onDie(PlayerRespawnEvent e) {
        User user = CheckResultsManager.getUser(e.getPlayer());
        user.setLastDeath(System.currentTimeMillis());
    }


}
