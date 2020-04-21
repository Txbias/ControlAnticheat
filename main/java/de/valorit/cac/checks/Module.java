package de.valorit.cac.checks;

import java.util.ArrayList;
import java.util.List;

public enum Module {

    FLIGHT,
    GlIDE,
    SPEED,
    REACH,
    FASTBOW,
    KILLAURA,
    FUCKER,
    INVENTORYMOVE,
    NOFALL,
    BLINK,
    GHOSTHAND,
    WATERWALK,
    TOWER;

    public static List<Module> getModules() {
        List<Module> modules = new ArrayList<>();

        modules.add(FLIGHT);
        modules.add(GlIDE);
        modules.add(SPEED);
        modules.add(REACH);
        modules.add(FASTBOW);
        modules.add(FUCKER);
        modules.add(INVENTORYMOVE);
        modules.add(NOFALL);
        modules.add(BLINK);
        modules.add(GHOSTHAND);
        modules.add(WATERWALK);
        modules.add(TOWER);

        return modules;
    }
}
