package de.valorit.cac.version_dependent.packets.packetreader;

import org.bukkit.entity.Player;

public interface PacketReader {

    void inject(Player p);
    void uninject();
    void setValue(Object obj, Object value, String name);
    void readPacket();
    Object getValue(Object obj, String name);

}
