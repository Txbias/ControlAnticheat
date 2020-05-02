package de.valorit.cac.checks.combat;

import de.valorit.cac.Config;
import de.valorit.cac.Main;
import de.valorit.cac.User;
import de.valorit.cac.checks.CheckResult;
import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.checks.Module;
import de.valorit.cac.utils.GameEvent;
import de.valorit.cac.utils.Permissions;
import de.valorit.cac.utils.PlayerUtils;
import de.valorit.cac.utils.version_dependent.VersionManager;
import de.valorit.cac.utils.version_dependent.packets.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;


public class Killaura {

    private final static Module NAME = Module.KILLAURA;

    private final static HashMap<Player, String> SPAWNED_NPCs = new HashMap<>();
    private final static HashMap<Player, Long> LAST_CHECK = new HashMap<>();

    public static void performCheck(int entityID, Player p) {
        if(!SPAWNED_NPCs.containsKey(p)) {
            return;
        }

        String npcName = SPAWNED_NPCs.get(p);

        if(VersionManager.getNPC().getEntityID(npcName) == entityID) {
            //Player is hacking
            GameEvent.addCheckResult(new CheckResult(NAME, true, p));
        }

    }

    public static void spawnNPC(Player p) {
        if(!LAST_CHECK.containsKey(p)) {
            LAST_CHECK.put(p, 0L);
            return;
        }

        if(p.hasPermission(Permissions.BYPASS)) {
            return;
        } else if(VersionManager.getCraftPlayerManager().getPing(p) >= Config.getMaxPing()) {
            return;
        }

        User user = CheckResultsManager.getUser(p);
        int timeBetweenSpawns = 10_000;
        if(user.getLevel(Module.KILLAURA) > 1) {
            timeBetweenSpawns = 9_000;
        }
        if(user.getLevel(Module.KILLAURA) > 8) {
            timeBetweenSpawns = 8_000;
        }

        //NPCs are allowed to spawn every 10 seconds at most
        if(System.currentTimeMillis() - LAST_CHECK.get(p) >= timeBetweenSpawns) {
            NPC npc = VersionManager.getNPC();
            String name = generateName();

            SPAWNED_NPCs.put(p, name);
            npc.spawn(p, PlayerUtils.getNPCLocation(p), name);
            LAST_CHECK.replace(p, System.currentTimeMillis());
            despawnNPC(p);
        }
    }

    private static void despawnNPC(Player p) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            String npcName = SPAWNED_NPCs.get(p);
            VersionManager.getNPC().destroy(p, npcName);
        }, 20);
    }

    private static String generateName() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 12; i++) {
            char c = (char) (random.nextInt('z' - 'a') + 'a');
            builder.append(c);
        }
        return builder.toString();
    }

}
