package de.valorit.cac.events;

import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.config.Settings;
import de.valorit.cac.utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        CheckResultsManager.users.add(new User(p));

        Settings.loadSettings(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        CheckResultsManager.users.remove(p);

        CheckResultsManager.getUser(p).getReader().uninject();
    }



}
