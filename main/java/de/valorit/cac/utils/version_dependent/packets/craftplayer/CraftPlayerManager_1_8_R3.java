package de.valorit.cac.utils.version_dependent.packets.craftplayer;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CraftPlayerManager_1_8_R3 implements CraftPlayerManager {

    @Override
    public int getPing(Player p) {
        return ((CraftPlayer) p).getHandle().ping;
    }
}
