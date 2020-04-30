package de.valorit.cac.utils.version_dependent;

import de.valorit.cac.Main;
import de.valorit.cac.utils.version_dependent.events.GlideEvent;
import de.valorit.cac.utils.version_dependent.packets.craftplayer.CraftPlayerManager;
import de.valorit.cac.utils.version_dependent.packets.craftplayer.CraftPlayerManager_1_12_R1;
import de.valorit.cac.utils.version_dependent.packets.craftplayer.CraftPlayerManager_1_8_R3;
import de.valorit.cac.utils.version_dependent.packets.npc.NPC;
import de.valorit.cac.utils.version_dependent.packets.npc.NPC_1_12_R1;
import de.valorit.cac.utils.version_dependent.packets.npc.NPC_1_8_R3;
import de.valorit.cac.utils.version_dependent.packets.packetreader.PacketReader;
import de.valorit.cac.utils.version_dependent.packets.packetreader.PacketReader_1_12_R1;
import de.valorit.cac.utils.version_dependent.packets.packetreader.PacketReader_1_8_R3;

public class VersionManager {

    private static NPC npc;
    private static PacketReader packetReader;
    private static CraftPlayerManager craftPlayerManager;

    private static String version;
    private static final String VERSION_1_8_R3 = "v1_8_R3";
    private static final String VERSION_1_12_R1 = "v1_12_R1";

    public boolean setupNPC() {
        version = Main.getInstance().getVersion();
        if(version.isEmpty()) {
            return false;
        }

        if(version.equals(VERSION_1_8_R3)) {
            npc = new NPC_1_8_R3();
            packetReader = new PacketReader_1_8_R3();
            craftPlayerManager = new CraftPlayerManager_1_8_R3();
        } else if(version.equals(VERSION_1_12_R1)) {
            npc = new NPC_1_12_R1();
            packetReader = new PacketReader_1_12_R1();
            craftPlayerManager = new CraftPlayerManager_1_12_R1();

            //Register Event
            Main.getInstance().getServer().getPluginManager().registerEvents(new GlideEvent(), Main.getInstance());
        }


        return npc != null && packetReader != null;
    }


    public static NPC getNPC() {
        return npc;
    }

    public static PacketReader getPacketReader() {
        if(version.equals(VERSION_1_8_R3)) {
            return new PacketReader_1_8_R3();
        } else if(version.equals(VERSION_1_12_R1)) {
            return new PacketReader_1_12_R1();
        }
        System.out.println("Invalid version!");
        return packetReader;
    }

    public static CraftPlayerManager getCraftPlayerManager() {
        return craftPlayerManager;
    }

    public static String getVersion() {
        return version;
    }

}
