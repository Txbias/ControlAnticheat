package de.valorit.cac.checks.movement;

import de.valorit.cac.Config;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import de.valorit.cac.utils.packets.PacketVersionManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class TowerCheck {

    private static final Module NAME = Module.TOWER;
    private static final CheckResult PASS = new CheckResult();

    private static final HashMap<Player, ArrayList<Block>> lastBlocks = new HashMap<>();
    private static final HashMap<Player, ArrayList<Location>> lastLocations = new HashMap<>();
    private static final HashMap<Player, Long> lastBlockTime = new HashMap<>();
    private static final HashMap<Player, Long> lastDifference = new HashMap<>();

    public CheckResult performCheck(BlockPlaceEvent e) {

        Player p = e.getPlayer();

        if(p.hasPermission(Permissions.BYPASS)) {
            return PASS;
        }

        if(PacketVersionManager.getCraftPlayerManager().getPing(p) > Config.getMaxPing()) {
            System.out.println(1);
            System.out.println(PacketVersionManager.getCraftPlayerManager().getPing(p));
            System.out.println(Config.getMaxPing());
            return PASS;
        }

        Block placed = e.getBlockPlaced();
        Block underPlayer = PlayerUtils.getBlockUnderPlayer(p);

        if(!lastBlocks.containsKey(p)) {
            lastBlocks.put(p, new ArrayList<>());
            lastBlocks.get(p).add(placed);
            lastLocations.put(p, new ArrayList<>());
            lastLocations.get(p).add(p.getLocation());
            lastBlockTime.put(p, System.currentTimeMillis());
            lastDifference.put(p, 10000L);
            return PASS;
        }

        if(!underPlayer.equals(placed) && !underPlayer.getType().equals(Material.AIR)) {
            updateHashMaps(p, placed);
            return PASS;
        }

        Location lastBlockLocation = lastBlocks.get(p).get(lastBlocks.get(p).size() - 1).getLocation();

        //Check whether the new block is on top of the last one
        if(!(placed.getLocation().getBlockY() - lastBlockLocation.getBlockY() == 1 && placed.getLocation().getBlockZ() == lastBlockLocation.getBlockZ() &&
                placed.getLocation().getBlockX() == lastBlockLocation.getBlockX())) {
            updateHashMaps(p, placed);
            return PASS;
        }

        if(PlayerUtils.isInLiquid(p)) {
            updateHashMaps(p, placed);
            return PASS;
        }

        Location lastPlayerLocation = lastLocations.get(p).get(lastLocations.get(p).size() - 1);

        if(p.getLocation().getBlockY() - placed.getLocation().getBlockY() != 1) {
            updateHashMaps(p, placed);
            return PASS;
        }

        //Check if player has moved upwards
        if(!(p.getLocation().getBlockY() - lastPlayerLocation.getBlockY() >= 1)) {
            updateHashMaps(p, placed);
            return PASS;
        }

        if(underPlayer.getType() == Material.AIR && lastBlocks.get(p).get(lastBlocks.get(p).size() - 1).getType() != Material.AIR ) {
            updateHashMaps(p, placed);
            return PASS;
        }

        if(!(p.getLocation().getBlockX() == lastPlayerLocation.getBlockX() && p.getLocation().getBlockZ() == lastPlayerLocation.getBlockZ())) {
            updateHashMaps(p, placed);
            return PASS;
        }

        if(p.getFallDistance() > 1) {
            updateHashMaps(p, placed);
            return PASS;
        }

        if(System.currentTimeMillis() - lastBlockTime.get(p) > 400 || lastDifference.get(p) > 400) {
            updateHashMaps(p, placed);
            return PASS;
        }

        double xDifference = p.getLocation().getX() - lastPlayerLocation.getX();
        double zDifference = p.getLocation().getZ() - lastPlayerLocation.getZ();

        if(xDifference > 0.01 || xDifference < -0.01) {
            updateHashMaps(p, placed);
            return PASS;
        }

        if(zDifference > 0.01 || zDifference < -0.01) {
            updateHashMaps(p, placed);
            return PASS;
        }

        e.setCancelled(true);
        updateHashMaps(p, placed);
        return new CheckResult(NAME, true, p);

    }

    private void updateHashMaps(Player p, Block placed) {

        if(lastBlocks.get(p).size() > 15) {
            lastBlocks.get(p).clear();
        }
        if(lastLocations.get(p).size() > 15) {
            lastLocations.get(p).clear();
        }

        lastBlocks.get(p).add(placed);
        lastLocations.get(p).add(p.getLocation());
        lastDifference.replace(p, System.currentTimeMillis() - lastBlockTime.get(p));
        lastBlockTime.replace(p, System.currentTimeMillis());
    }


}
