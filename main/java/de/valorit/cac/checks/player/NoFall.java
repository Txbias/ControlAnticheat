package de.valorit.cac.checks.player;

import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class NoFall {

    private final Module NAME = Module.NOFALL;
    private final CheckResult PASS = new CheckResult();

    private HashMap<Player, Double> lastFallDistance = new HashMap<>();
    private HashMap<Player, Double> lastHealth = new HashMap<>();

    public CheckResult performCheck(PlayerMoveEvent e) {

        System.out.println(1);

        Player p = e.getPlayer();

        if(p.hasPermission(Permissions.BYPASS)) {
            System.out.println(2);
           return PASS;
        }

        if(!lastFallDistance.containsKey(p)) {
            System.out.println(3);
            lastFallDistance.put(p, (double) p.getFallDistance());
            lastHealth.put(p, p.getHealth());
        }

        if(p.getFallDistance() == 0 && lastFallDistance.get(p) > 3) {
            //Player has fallen
            System.out.println(4);

            Material underPlayer = PlayerUtils.getBlockMaterialUnderPlayer(p);

            if(!(PlayerUtils.isInCobweb(p)) && !(PlayerUtils.isInLiquid(p)) && !(underPlayer== Material.SLIME_BLOCK)
                && !(underPlayer == Material.HAY_BLOCK) && !(underPlayer == Material.SPONGE)) {
                System.out.println(5);

                User user = CheckResultsManager.getUser(p);
                long lastDeath = user.getLastDeath();

                double damageTaken = lastHealth.get(p) - p.getHealth();
                double requiredDamage = (lastFallDistance.get(p) * 0.5) - 1.5;
                if(requiredDamage > damageTaken && System.currentTimeMillis() - lastDeath > 3000) {
                    //Player is hacking

                    System.out.println(System.currentTimeMillis() - lastDeath);

                    double difference = requiredDamage - damageTaken;
                    if(difference > 3) {
                        double newHealth = p.getHealth() - difference;
                        if (newHealth < 0) {
                            newHealth = 0;
                        } else if (newHealth > 20) {
                            newHealth = 20;
                        }
                        p.setHealth(newHealth);

                        updateMaps(p);
                        return new CheckResult(NAME, true, p);
                    }
                }

            }
        }

        updateMaps(p);
        return PASS;
    }

    private void updateMaps(Player p) {
        lastFallDistance.replace(p, (double) p.getFallDistance());
        lastHealth.replace(p, p.getHealth());
    }

}
