package network.palace;

import lombok.Getter;
import network.palace.command.*;
import network.palace.core.plugin.Plugin;
import network.palace.core.plugin.PluginInfo;
import network.palace.listeners.*;
import network.palace.resourcepack.PackManager;
import network.palace.util.InventoryNav;

import java.io.File;

@PluginInfo(name = "Lobby", version = "1.0.0", depend = {"Core"})
public class Lobby extends Plugin {

    @Getter private InventoryNav inventoryNav;

    @Override
    public void onPluginEnable() {
        checkConfig();
        registerCommands();
        registerListeners();
        inventoryNav = new InventoryNav();
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

    private void registerCommands() {
        registerCommand(new SetServerName());
        registerCommand(new SetSpawn());
        registerCommand(new ToggleDonatorFly());
        registerCommand(new TogglePack());
        registerCommand(new ToggleTitle());
    }

    private void registerListeners() {
        registerListener(new PlayerLogin());
        registerListener(new PlayerInteract());
        registerListener(new PlayerDropItem());
        registerListener(new PlayerMove());
        registerListener(new PlayerInteract());
        registerListener(new TntExplosion());
        registerListener(new PackManager());
        registerListener(new DonatorFlight());
    }
}
