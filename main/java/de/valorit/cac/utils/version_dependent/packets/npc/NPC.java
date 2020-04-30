package de.valorit.cac.utils.version_dependent.packets.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;


public interface NPC {

    Random random = new Random();

    void spawn(Player p, Location loc, String name);
    void destroy(Player p, String name);
    int getEntityID(String name);
    Player getNPC(String name);



}
