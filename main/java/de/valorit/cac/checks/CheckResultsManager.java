package de.valorit.cac.checks;

import de.valorit.cac.Main;
import de.valorit.cac.config.Config;
import de.valorit.cac.utils.GameEvent;
import de.valorit.cac.utils.User;
import de.valorit.cac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CheckResultsManager {

    public static List<User> users = new ArrayList<>();

    public void handleCheckResults() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            List<CheckResult> results = GameEvent.getResults();
            List<User> flaggedUsers = new ArrayList<>();

            for (int i = 0; i < results.size(); i++) {
                if(results.get(i) == null) {
                    continue;
                }
                if (results.get(i).isHacking) {
                    User user = getUser(results.get(i).getPlayer());
                    user.incrementLevel(results.get(i).getModule());
                    updateUser(user);

                    if(!flaggedUsers.contains(user)) {
                        flaggedUsers.add(user);
                    }
                    if(user.getLevel(results.get(i).getModule()) % 4 == 0) {
                        Utils.broadCheckResult(results.get(i));
                    }
                }
            }

            if(Config.isAutoBanEnabled()) {
                executeAutoBans(flaggedUsers);
            }
            GameEvent.clearResults();
        }, 40, 20);
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

    /**
     * Checks for all users in flaggedUsers whether they should be banned
     * @param flaggedUsers All users to be checked
     */
    private void executeAutoBans(List<User> flaggedUsers) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            int maxFlagsPerModule = Config.getMaxFlagsPerModule();
            int maxFlagsTotal = Config.getMaxFlagsTotal();
            String banCommand = Config.getBanCommand();

            for(User user : flaggedUsers) {
                if(checkPlayer(user, maxFlagsPerModule, maxFlagsTotal)) {
                    Player p = user.getPlayer();
                    banPlayer(p, banCommand);
                }
            }
        });
    }

    /**
     * Bans a player from the server using the main thread
     * @param p The player to ban
     * @param banCommand The command that should be used to ban players
     */
    private void banPlayer(Player p, String banCommand) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Bukkit.dispatchCommand(console, prepareBanCommand(p, banCommand));
            Utils.broadCastMessage("The player §6" + p.getName() + "§7 was banned.");
        });
    }

    /**
     * Check whether a player should be auto banned
     * @param user User object of the player
     * @param maxFlagsPerModule The maximum allowed flags per module
     * @param maxFlagsTotal The maximum flags allowed for all modules combined
     * @return Returns true if a player should be banned
     */
    private boolean checkPlayer(User user, int maxFlagsPerModule, int maxFlagsTotal) {
        int totalFlags = 0;
        for(Module m : Module.getModules()) {
            int flags = user.getLevel(m);
            totalFlags += flags;
            if(flags >= maxFlagsPerModule) {
                return true;
            }
        }
        return totalFlags >= maxFlagsTotal;
    }

    /**
     * Inserts a player into the ban command
     * @param p The Player that should be banned
     * @param command The ban command as String with placeholders
     * @return Returns the command as String
     */
    private String prepareBanCommand(Player p, String command) {
        command = command.replace("[player]", p.getName());
        command = command.replace("/", "");
        return command;
    }

}
