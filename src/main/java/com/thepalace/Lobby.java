package com.thepalace;

import com.thepalace.command.Spawn;
import com.thepalace.core.plugin.Plugin;
import com.thepalace.core.plugin.PluginInfo;
import com.thepalace.listeners.*;
import com.thepalace.resourcepack.PackManager;

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
        registerListener(new PackManager());

        registerCommand(new Spawn());
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
}
