package de.valorit.cac.events;

import de.valorit.cac.Main;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.utils.GameEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerEvent extends GameEvent implements Listener {

    //GhostHand ghostHandCheck = new GhostHand();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        CheckResultsManager.getUser(p).setInventoryOpen(true);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            CheckResultsManager.getUser(p).setInventoryOpen(false);
        }, 3);
    }

    /*@EventHandler
    public void onInteract(PlayerInteractEvent e) {
        addCheckResult(ghostHandCheck.performCheck(e));
    }*/



}
