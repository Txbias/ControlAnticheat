package de.valorit.cac;

import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.Settings;
import de.valorit.cac.utils.version_dependent.VersionManager;
import de.valorit.cac.utils.version_dependent.packets.packetreader.PacketReader;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class User {

    private final Player p;
    private LinkedHashMap<Module, Integer> levels = new LinkedHashMap<>();
    private final PacketReader reader;
    private boolean eating;
    private boolean usingBow;
    private boolean inventoryOpen = false;
    private boolean attack = true;
    private boolean receivesNotifications = false;
    private boolean pushed = false;
    private boolean usingElytra = false;

    private long lastDeath;
    private long bowStarted = -1;
    private final long JOIN_TIME;

    public User(Player p) {
        this.p = p;

        loadModules();

        JOIN_TIME = System.currentTimeMillis();

        eating = false;
        usingBow = false;
        lastDeath = System.currentTimeMillis();

        if(p.hasPermission(Permissions.NOTIFY) && Settings.isReceivingNotifications(p)) {
            receivesNotifications = true;
        }

        reader = VersionManager.getPacketReader();
        reader.inject(p);
    }

    public Player getPlayer() {
        return p;
    }

    public boolean isInventoryOpen() {
        return inventoryOpen;
    }

    public boolean canAttack() {
        return attack;
    }

    public boolean isReceivesNotifications() {
        return receivesNotifications;
    }

    public boolean isPushed() {
        return pushed;
    }

    public boolean isUsingElytra() {
        return usingElytra;
    }

    public void setUsingBow(boolean using) {
        this.usingBow = using;
        if(using) {
            bowStarted = System.currentTimeMillis();
        }
    }

    public void setInventoryOpen(boolean inventoryOpen) {
        this.inventoryOpen = inventoryOpen;
    }

    public void setCanAttack(boolean value) {
        this.attack = value;
    }

    public void setReceivesNotifications(boolean value) {
        Settings.setReceivesNotifications(p, value);
        this.receivesNotifications = value;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

    public void setUsingElytra(boolean using) {
        usingElytra = using;
    }

    public void setLastDeath(long lastDeath) {
        this.lastDeath = lastDeath;
    }

    public void incrementLevel(Module module) {
        levels.replace(module, levels.get(module) + 1);
    }

    public int getLevel(Module module) {
        return levels.get(module);
    }

    public int getLevel(int index) {
        return (new ArrayList<>(levels.values())).get(index);
    }

    public Module getModule(int index) {
        return (new ArrayList<>(levels.keySet())).get(index);
    }

    public long getBowStarted() {
        return bowStarted;
    }

    public long getLastDeath() {
        return lastDeath;
    }

    public long getJoinTime() {
        return JOIN_TIME;
    }

    public LinkedHashMap<Module, Integer> getLevels() {
        return levels;
    }

    public PacketReader getReader() {
        return reader;
    }

    private void loadModules() {
        levels.put(Module.FLIGHT, 0);
        levels.put(Module.GlIDE, 0);
        levels.put(Module.SPEED, 0);
        levels.put(Module.REACH, 0);
        levels.put(Module.FASTBOW, 0);
        levels.put(Module.KILLAURA, 0);
        levels.put(Module.FUCKER, 0);
        levels.put(Module.INVENTORYMOVE, 0);
        levels.put(Module.NOFALL, 0);
        levels.put(Module.BLINK, 0);
        levels.put(Module.GHOSTHAND, 0);
        levels.put(Module.WATERWALK, 0);
        levels.put(Module.TOWER, 0);
    }


}
