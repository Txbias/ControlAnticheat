package de.valorit.cac.utils.packets.npc;


import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class NPC_1_8_R3 implements NPC{

    private ArrayList<Player> npcPlayers = new ArrayList<>();

    @Override
    public void spawn(Player p, Location loc, String name) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) p.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);

        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
        Player npcPlayer = npc.getBukkitEntity().getPlayer();

        if(random.nextDouble() > 0.5) {
            npcPlayer.setPlayerListName(name);
        } else {
          npcPlayer.setPlayerListName("");
        }

        npcPlayers.add(npcPlayer);

        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));

        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
    }

    @Override
    public void destroy(Player p, String name) {
        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        Player npc = getNPC(name);
        if(npc == null) {
            System.out.println("NPC not found!");
            return;
        }

        npcPlayers.remove(npc);

        connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getEntityId()));
    }

    @Override
    public int getEntityID(String name) {
        for(Player npc : npcPlayers) {
            if(npc.getName().equals(name)) {
                return npc.getEntityId();
            }
        }
        return -1;
    }

    @Override
    public Player getNPC(String name) {
        for(Player npc : npcPlayers) {
            if(npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
    }
}
