package de.valorit.cac.checks.combat;

import de.valorit.cac.Main;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.GameEvent;
import de.valorit.cac.utils.packets.PacketVersionManager;
import de.valorit.cac.utils.packets.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Killaura {

    private final static Module NAME = Module.KILLAURA;

    private final List<Player> CHECKED = new ArrayList<>();
    private final static HashMap<Player, String> SPAWNED_NPCs = new HashMap<>();
    private final static HashMap<Player, Long> PLAYERS = new HashMap<>();

    public static void performCheck(int entityID, Player p) {
        if(!SPAWNED_NPCs.containsKey(p)) {
            return;
        }

        String npcName = SPAWNED_NPCs.get(p);

        if(PacketVersionManager.getNPC().getEntityID(npcName) == entityID) {
            //Player is hacking
            GameEvent.addCheckResult(new CheckResult(NAME, true, p));
        }

    }

    public void enableChecks() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            for(Player p : PLAYERS.keySet()) {
                if(p.hasPermission("cac.bypass")) {
                    continue;
                }
                if(!CHECKED.contains(p)) {
                    if (System.currentTimeMillis() - PLAYERS.get(p) >= 10 * 1000) {
                        //Spawn NPCs every 10 seconds

                        NPC npc = PacketVersionManager.getNPC();

                        String name = generateName();

                        SPAWNED_NPCs.put(p, name);
                        npc.spawn(p, getBlockBehindPlayer(p), name);

                        PLAYERS.replace(p, System.currentTimeMillis());
                        CHECKED.add(p);
                    }
                } else {
                    String npcName = SPAWNED_NPCs.get(p);
                    PacketVersionManager.getNPC().destroy(p, npcName);

                    CHECKED.remove(p);
                }
            }
        }, 20 * 5, 20);


    }

    public static void addPlayer(Player p) {
        PLAYERS.put(p, System.currentTimeMillis());
    }

    public static void removePlayer(Player p) {
        PLAYERS.remove(p);
    }

    private String generateName() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 12; i++) {
            char c = (char) (random.nextInt('z' - 'a') + 'a');
            builder.append(c);
        }
        return builder.toString();
    }

    private Location getBlockBehindPlayer(Player p) {
        Location loc = p.getLocation();
        Vector vec = loc.getDirection().multiply(-3.5D);
        vec.setX(0.5);
        vec.setY(0.5);
        return loc.add(vec);
    }


}
