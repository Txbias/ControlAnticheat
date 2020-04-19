package de.valorit.cac.checks;

import de.valorit.cac.Main;
import de.valorit.cac.User;
import de.valorit.cac.utils.GameEvent;
import de.valorit.cac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CheckResultsManager {

    private final Main PLUGIN;

    public static List<User> users = new ArrayList<>();


    public CheckResultsManager(Main plugin) {
        this.PLUGIN = plugin;
    }

    public void handleCheckResults() {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
            while (true) {
                List<CheckResult> results = GameEvent.getResults();

                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).isHacking) {
                        User user = getUser(results.get(i).getPlayer());
                        user.incrementLevel(results.get(i).getModule());
                        updateUser(user);
                        if(user.getLevel(results.get(i).getModule()) % 4 == 0) {
                            Utils.broadCheckResult(results.get(i));
                        }
                    }
                }

                GameEvent.clearResults();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static User getUser(Player p) {
        for (User value : users) {
            if (value.getPlayer() == p) {
                return value;
            }
        }
        User user = new User(p);
        users.add(user);
        return user;
    }

    public static void updateUser(User user) {
        String name = user.getPlayer().getName();
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getPlayer().getName().equals(name)) {
                users.set(i, user);
            }
        }
    }

}
