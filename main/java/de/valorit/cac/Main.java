package de.valorit.cac;

import de.valorit.cac.checks.CheckResultsManager;
import de.valorit.cac.commands.CacCommand;
import de.valorit.cac.commands.PingCommand;
import de.valorit.cac.config.Config;
import de.valorit.cac.events.*;
import de.valorit.cac.version_dependent.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    public static Main instance;

    CheckResultsManager resultsManager = new CheckResultsManager();
    PluginManager pluginManager = getServer().getPluginManager();

    VersionManager versionManager = new VersionManager();

    @Override
    public void onEnable() {
        super.onEnable();

        registerEvents();
        registerCommands();

        setInstance(this);

        if(versionManager.setupNPC()) {
            System.out.println("CAC is compatible!");
        } else {
            System.out.println("The server version is not compatible with CAC!");
            System.out.println("Shutting down plugin...");

            pluginManager.disablePlugin(this);
        }


        Config.loadConfig();
        saveDefaultConfig();
        resultsManager.handleCheckResults();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void registerEvents() {
        pluginManager.registerEvents(new MoveEvent(), this);
        pluginManager.registerEvents(new BreakEvent(), this);
        pluginManager.registerEvents(new PlaceEvent(), this);
        pluginManager.registerEvents(new JoinQuitEvent(), this);
        pluginManager.registerEvents(new CombatEvent(), this);
        pluginManager.registerEvents(new PlayerEvent(), this);
    }

    private void registerCommands() {
        getCommand("cac").setExecutor(new CacCommand());
        getCommand("ping").setExecutor(new PingCommand());
    }

    private void setInstance(Main mainInstance) {
        instance = mainInstance;
    }

    public static Main getInstance() {
        return instance;
    }

    public String getVersion() {
        try {
            return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }


}
