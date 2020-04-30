package de.valorit.cac.utils.version_dependent.packets.craftplayer;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CraftPlayerManager_1_12_R1 implements CraftPlayerManager{

    @Override
    public int getPing(Player p) {
        return ((CraftPlayer) p).getHandle().ping;
    }
}
