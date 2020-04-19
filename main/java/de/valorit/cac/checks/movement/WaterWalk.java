package de.valorit.cac.checks.movement;

import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.PlayerUtils;
import de.valorit.cac.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class WaterWalk {

    private final Module NAME = Module.WATERWALK;
    private final CheckResult PASS = new CheckResult();

    private final HashMap<Player, Block> LAST_BLOCK = new HashMap<>();


    public CheckResult performCheck(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        Block blockUnderPlayer = PlayerUtils.getBlockUnderPlayer(p);
        Material underPlayer = PlayerUtils.getBlockMaterialUnderPlayer(p);
        Location from = e.getFrom();
        Location to = e.getTo();
        double distance = to.distance(from);

        if(p.hasPermission("cac.bypass")) {
            return PASS;
        }

        if(!LAST_BLOCK.containsKey(p)) {
            LAST_BLOCK.put(p, PlayerUtils.getBlockUnderPlayer(p));
        }


        if(PlayerUtils.isLiquid(blockUnderPlayer) && !(underPlayer == Material.WATER) && !(underPlayer == Material.LAVA)) {
            if(!PlayerUtils.isInLiquid(p) && PlayerUtils.isLiquid(LAST_BLOCK.get(p)) && !(PlayerUtils.isLiquid(PlayerUtils.getBlockInFront(p)))) {
                if((distance > 0.27 && distance < 0.28) || Utils.round(distance) == 0.1483) {
                    p.teleport(from);
                    updateLastBlock(p);
                    return new CheckResult(NAME, true, p);
                }
            }
        }

        updateLastBlock(p);
        return PASS;
    }

    private void updateLastBlock(Player p) {
        Block b = PlayerUtils.getBlockUnderPlayer(p);
        if(LAST_BLOCK.containsKey(p)) {
            if(LAST_BLOCK.get(p) != b) {
                LAST_BLOCK.replace(p, b);
            }
        }
    }


}
