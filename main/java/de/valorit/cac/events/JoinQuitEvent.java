package de.valorit.cac.events;

import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.combat.Killaura;
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

        Killaura.addPlayer(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        CheckResultsManager.users.remove(p);

        Killaura.removePlayer(p);
    }



}
