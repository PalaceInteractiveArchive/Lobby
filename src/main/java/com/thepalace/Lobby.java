package com.thepalace;

import com.thepalace.commands.Spawn;
import com.thepalace.core.plugin.Plugin;
import com.thepalace.core.plugin.PluginInfo;
import com.thepalace.listeners.*;

import java.io.File;

@PluginInfo(name = "Lobby")
public class Lobby extends Plugin {

    public static Lobby instance;

    @Override
    public void onPluginEnable() {
        instance = this;
        checkConfig();
        registerListener(new PlayerLogin());
        registerListener(new PlayerMove());
        registerListener(new PlayerDrop());
        registerListener(new PlayerInteract());
        registerListener(new PlayerInventoryClick());
        registerCommand(new Spawn());
    }

    @Override
    public void onPluginDisable() {
        instance = null;
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
