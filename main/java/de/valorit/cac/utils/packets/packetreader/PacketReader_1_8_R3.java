package de.valorit.cac.utils.packets.packetreader;

import de.valorit.cac.Config;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.combat.Killaura;
import de.valorit.cac.checks.movement.Blink;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class PacketReader_1_8_R3 implements PacketReader{

    private Player player;
    private Channel channel;
    private Blink blink;

    private String injectorName;

    private static Packet<?> packet;
    private ArrayList<String> packets = new ArrayList<>();
    private int attacksCount = 0;


    @Override
    public void inject(Player p) {
        this.player = p;
        injectorName = p.getName();
        this.blink = new Blink();

        CraftPlayer cPlayer = (CraftPlayer) player;
        channel = cPlayer.getHandle().playerConnection.networkManager.channel;
        channel.pipeline().addAfter("decoder", injectorName, new MessageToMessageDecoder<Packet<?>>() {

            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, List<Object> list) throws Exception {
                list.add(packet);
                PacketReader_1_8_R3.packet = packet;
                readPacket();
            }

        });

    }

    long lastClear = System.currentTimeMillis();
    @Override
    public void readPacket() {
        String name = packet.getClass().getSimpleName();

        packets.add(name);

        if(System.currentTimeMillis() - lastClear >= 1000) {
            if(packets.size() > Config.getMaxPackets()) {
                //Player is hacking
                if(!player.hasPermission(Permissions.BYPASS)) {
                    Utils.broadCastWarning("The player §c" + player.getName() + " §e is sending to many packets (" + packets.size() + ")!");
                }
            }
            if(attacksCount >= 23) {
                if(!player.hasPermission(Permissions.BYPASS)) {
                    Utils.broadCastWarning("The player §c " + player.getName() + " §e is attacking to many entities (" + attacksCount + ")!");
                    CheckResultsManager.getUser(player).setCanAttack(false);
                }
            } else {
                CheckResultsManager.getUser(player).setCanAttack(true);
            }
            packets.clear();
            lastClear = System.currentTimeMillis();
            attacksCount = 0;
        }

        if(packet instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity playInUseEntity = (PacketPlayInUseEntity) packet;
            int id = (int) getValue(playInUseEntity, "a");
            String use = getValue(playInUseEntity, "action").toString();

            if(use != null) {
                if(use.equalsIgnoreCase("ATTACK")) {
                    Killaura.performCheck(id, player);

                    attacksCount++;
                }
            }
        } else if(packet instanceof PacketPlayInCloseWindow) {
            CheckResultsManager.getUser(player).setInventoryOpen(false);
        } else if(packet instanceof PacketPlayInFlying.PacketPlayInPosition) {
            blink.performCheck(player);
        }
    }

    @Override
    public void uninject() {
        if(channel.pipeline().get(injectorName) != null) {
            channel.pipeline().remove(injectorName);
        }
    }

    @Override
    public void setValue(Object obj, Object value, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println(obj.getClass());
        }
    }

    @Override
    public Object getValue(Object obj, String name){
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            System.out.println(obj.getClass());
        }
        return null;
    }
}
