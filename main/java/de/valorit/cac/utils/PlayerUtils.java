package de.valorit.cac.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerUtils {

    private static final List<Material> materials = new ArrayList<>(Arrays.asList(Material.LAVA, Material.AIR, Material.WATER, Material.STATIONARY_WATER));

    public static boolean isInAir(Player p) {
        return getBlockMaterialUnderPlayer(p) == Material.AIR;
    }

    public static Material getBlockMaterialUnderPlayer(Player p) {
        return getBlockUnderPlayer(p).getType();
    }

    public static Block getBlockUnderPlayer(Player p) {
        return p.getLocation().getBlock().getRelative(BlockFace.DOWN);
    }

    public static Block getBlockInFront(Player p) {
        int yaw = (int) p.getLocation().getYaw() + 45;
        while (yaw < 0) {
            yaw += 360;
        }
        while (yaw > 360) {
            yaw -= 360;
        }

        int direction = yaw / 90;
        Block b = p.getLocation().getBlock();
        switch (direction) {
            case 0: b = b.getRelative(BlockFace.SOUTH);
                    break;
            case 1: b = b.getRelative(BlockFace.WEST);
                    break;
            case 2: b = b.getRelative(BlockFace.NORTH);
                    break;
            case 3: b = b.getRelative(BlockFace.EAST);
                    break;
        }
        return b;
    }

    public static void movePlayerDown(Player p) {
        while (materials.contains(getBlockMaterialUnderPlayer(p))) {
            p.teleport(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ()));
        }
    }

    public static boolean isInCobweb(Player p) {
        if((p.getLocation().getBlock() != null && p.getLocation().getBlock().getType() == Material.WEB) ||
                (p.getLocation().getBlock().getRelative(BlockFace.UP) != null && p.getLocation().getBlock().getRelative(BlockFace.UP).getType()
                == Material.WEB)) {
            return true;
        }
        return false;
    }

    public static boolean isInLiquid(Player p) {
        Material m = p.getLocation().getBlock().getType();
        return m == Material.STATIONARY_WATER || m == Material.WATER || m == Material.STATIONARY_LAVA || m == Material.LAVA;
    }

    public static boolean blockOverPlayer(Player p) {
        Block b = p.getEyeLocation().add(0, 1, 0).getBlock();
        return b.getType() != Material.AIR;
    }

    public static boolean isLiquid(Block b) {
        ArrayList<Material> liquids = new ArrayList<>(Arrays.asList(Material.WATER, Material.LAVA, Material.STATIONARY_LAVA, Material.STATIONARY_WATER));
        Material m = b.getType();

        return liquids.contains(m);
    }

    /**
     * Checks if there are any block in a 3x3 square below the player
     * @return Returns true if blocks were found
     */
    public static boolean isPlatformBelow(Player p ) {
        Block belowPlayer = getBlockUnderPlayer(p);
        World world = p.getLocation().getWorld();

        if(belowPlayer.getType() != Material.AIR && !belowPlayer.isLiquid()) {
            return true;
        }

        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                Block b = world.getBlockAt(belowPlayer.getLocation().add(x, 0, z));
                if(b.getType() != Material.AIR && !b.isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Location getNPCLocation(Player p) {
        Location loc = p.getLocation();
        Vector vec = loc.getDirection();
        vec.multiply(-1.25D);
        loc.add(vec);
        loc.setX(loc.getX() + 1);
        loc.setZ(loc.getZ() + 1);
        return loc;
    }

}
