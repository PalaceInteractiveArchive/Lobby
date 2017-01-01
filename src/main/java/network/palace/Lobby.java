package network.palace;

import network.palace.core.Core;
import network.palace.core.plugin.Plugin;
import network.palace.core.plugin.PluginInfo;
import network.palace.command.*;
import network.palace.listeners.*;
import network.palace.resourcepack.PackManager;
import org.bukkit.ChatColor;

import java.io.File;

@PluginInfo(name = "Lobby")
public class Lobby extends Plugin {

    @Override
    public void onPluginEnable() {
        checkConfig();

        registerListener(new PlayerLogin());
        registerListener(new InventoryClick());
        registerListener(new PlayerInteract());
        registerListener(new PlayerDropItem());
        registerListener(new PlayerMove());
        registerListener(new PlayerInteract());
        registerListener(new TntExplosion());

        registerListener(new PackManager());
        registerListener(new DonatorFlight());
        if (doesVanishExist()) {
            registerListener(new VanishJoinListener());
        } else {
            Core.logMessage(getInfo().name(), ChatColor.RED + "VANISH IS NOT LOADED");
        }

        registerCommand(new SetServerName());
        registerCommand(new SetSpawn());
        registerCommand(new ToggleDonatorFly());
        registerCommand(new TogglePack());
        registerCommand(new ToggleTitle());
    }

    @Override
    public void onPluginDisable() {
    }

    private void checkConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean doesVanishExist() {
        try  {
            Class.forName("org.kitteh.vanish.VanishManager");
            return true;
        }  catch (ClassNotFoundException e) {
            return false;
        }
    }
}
