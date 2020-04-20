package de.valorit.cac.checks.player;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class NoFall {

    private final Module NAME = Module.NOFALL;
    private final CheckResult PASS = new CheckResult();

    private double lastFallDistance = -1;
    private double lastHealth = -1;
    public CheckResult performCheck(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if(p.hasPermission(Permissions.BYPASS)) {
           lastFallDistance = p.getFallDistance();
           lastHealth = p.getHealth();
           return PASS;
        }

        if(p.getFallDistance() == 0 && lastFallDistance > 3) {
            //Player has fallen

            Material underPlayer = PlayerUtils.getBlockMaterialUnderPlayer(p);

            if(!(PlayerUtils.isInCobweb(p)) && !(PlayerUtils.isInLiquid(p)) && !(underPlayer== Material.SLIME_BLOCK)
                && !(underPlayer == Material.HAY_BLOCK) && !(underPlayer == Material.SPONGE)) {
                double damageTaken = lastHealth - p.getHealth();
                double requiredDamage = (lastFallDistance * 0.5) - 1.5;

                if(requiredDamage > damageTaken) {
                    //Player is hacking
                    double difference = requiredDamage - damageTaken;
                    if(difference > 3) {
                        double newHealth = p.getHealth() - difference;
                        if (newHealth < 0) {
                            newHealth = 0;
                        } else if (newHealth > 20) {
                            newHealth = 20;
                        }
                        p.setHealth(newHealth);

                        lastHealth = p.getHealth();
                        lastFallDistance = p.getFallDistance();
                        return new CheckResult(NAME, true, p);
                    }
                }

            }
        }

        lastHealth = p.getHealth();
        lastFallDistance = p.getFallDistance();
        return PASS;
    }

}
