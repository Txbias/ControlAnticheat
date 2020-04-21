package de.valorit.cac.checks.movement;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class TowerCheck {

    private final Module NAME = Module.TOWER;
    private final CheckResult PASS = new CheckResult();

     private final HashMap<Player, Block> lastBlockPlaced = new HashMap<>();
     private final HashMap<Player, Location> lastLocation = new HashMap<>();
     private final HashMap<Player, Long> lastBlockPlacedTime = new HashMap<>();
     private final HashMap<Player, Long> lastDifference = new HashMap<>();
     private final HashMap<Player, Double> lastVelocityY = new HashMap<>();

    public CheckResult performCheck(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        //Check if player has placed a block before
        if(!lastBlockPlaced.containsKey(p)) {
            lastBlockPlaced.put(p, e.getBlock());
            lastLocation.put(p, p.getLocation());
            lastDifference.put(p, 1000L);
            lastBlockPlacedTime.put(p, System.currentTimeMillis());
            lastVelocityY.put(p, 0.0);
            return PASS;
        }

        Block placed = e.getBlockPlaced();

        //Check if player is standing on the placed block
        if(!PlayerUtils.getBlockUnderPlayer(p).equals(placed) && !PlayerUtils.getBlockUnderPlayer(p).getType().equals(Material.AIR)) {
            updateMaps(p, placed);
            return PASS;
        }

        //Check if new block is on top of the old one
        if(!(placed.getLocation().getBlockX() == p.getLocation().getBlockX()) &&
                !(placed.getLocation().getBlockZ() == p.getLocation().getBlockZ())) {
            return PASS;
        }

        //Player is moving sideways
        if(p.getLocation().getBlockX() != lastLocation.get(p).getBlockX() && p.getLocation().getBlockZ() != lastLocation.get(p).getBlockZ()) {
            return PASS;
        }

        if(p.getFallDistance() > 1) {
            return PASS;
        }

        if(placed.getLocation().getY() - lastBlockPlaced.get(p).getLocation().getY() == 1) {
            //New block is one block higher then the last block
            if(p.getLocation().getBlock().getY() - lastLocation.get(p).getBlock().getY() == 1) {
                //Player is also one block higher

                Vector velocity = p.getVelocity();
                //Check if the blocks are placed shortly after each other
                if(System.currentTimeMillis() - lastBlockPlacedTime.get(p) < 400 && lastDifference.get(p) < 400) {

                    if(velocity.getZ() == 0 && velocity.getX() == 0 && velocity.getY() > 0.15 && lastVelocityY.get(p) > 0.15) {
                        //Player is hacking
                        e.setCancelled(true);
                        updateMaps(p, placed);
                        return new CheckResult(NAME, true, p);
                    } else if(velocity.getY() < 0 && velocity.getY() > -0.29) {
                        //Player is hacking
                        e.setCancelled(true);
                        updateMaps(p, placed);
                        return new CheckResult(NAME, true, p);
                    }
                }
            }
        }
        updateMaps(p, placed);
        return PASS;
    }

    /**
     * Update the HashMaps for the next block
     * @param p The player who placed the block
     * @param placed The block that was placed
     */
    private void updateMaps(Player p, Block placed) {
        lastBlockPlaced.replace(p, placed);
        lastLocation.replace(p, p.getLocation());
        lastDifference.put(p, System.currentTimeMillis() - lastBlockPlacedTime.get(p));
        lastBlockPlacedTime.replace(p, System.currentTimeMillis());
        lastVelocityY.replace(p, p.getVelocity().getY());
    }


}
