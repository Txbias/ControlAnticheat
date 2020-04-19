package de.valorit.cac.checks;

import org.bukkit.entity.Player;

public class CheckResult {

    private Module module;
    public boolean isHacking = false;
    private Player p;

    public CheckResult(Module module, boolean isHacking, Player p) {
        this.module = module;
        this.isHacking = isHacking;
        this.p = p;
    }

    public CheckResult() {

    }

    public Module getModule() {
        return module;
    }

    public Player getPlayer() {
        return p;
    }
}
