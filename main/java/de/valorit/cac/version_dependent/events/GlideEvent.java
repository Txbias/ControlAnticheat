package de.valorit.cac.version_dependent.events;

import de.valorit.cac.Main;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class GlideEvent implements Listener {


    boolean toggledOffSoon = false;
    @EventHandler
    public void onToggle(EntityToggleGlideEvent e) {
        if(!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();
        User user = CheckResultsManager.getUser(p);

        if(user.isUsingElytra()) {
            toggledOffSoon = true;
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> user.setUsingElytra(false), 10);
        } else {
            user.setUsingElytra(true);
            if(toggledOffSoon) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
                    toggledOffSoon = false;
                    user.setUsingElytra(true);
                }, 7);
            }
        }

    }




}
