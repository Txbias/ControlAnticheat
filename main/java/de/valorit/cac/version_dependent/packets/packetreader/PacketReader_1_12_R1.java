package de.valorit.cac.version_dependent.packets.packetreader;

import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.combat.Killaura;
import de.valorit.cac.checks.movement.Blink;
import de.valorit.cac.config.Config;
import de.valorit.cac.config.Messages;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInCloseWindow;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PacketReader_1_12_R1  implements PacketReader{

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
                PacketReader_1_12_R1.packet = packet;
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
                    Utils.broadCastWarning(Messages.getToManyPackets(player, packets.size()));
                }
            }
            if(attacksCount >= 23) {
                if(!player.hasPermission(Permissions.BYPASS)) {
                    Utils.broadCastWarning(Messages.getToManyAttacks(player, attacksCount));
                    CheckResultsManager.getUser(player).setCanAttack(false);
                }
            } else {
                CheckResultsManager.getUser(player).setCanAttack(true);
            }
            attacksCount = 0;
            packets.clear();
            lastClear = System.currentTimeMillis();
        }


        if(packet instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
            int id = (int) getValue(packetPlayInUseEntity, "a");
            String use = getValue(packetPlayInUseEntity, "action").toString();

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
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Object getValue(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
