package com.thepalace;

import com.thepalace.commands.Spawn;
import com.thepalace.core.plugin.Plugin;
import com.thepalace.core.plugin.PluginInfo;
import com.thepalace.listeners.PlayerLogin;
import com.thepalace.listeners.PlayerMove;

import java.io.File;

/**
 * Created by Innectic on 11/18/2016.
 */
@PluginInfo(name = "Lobby")
public class Lobby extends Plugin {

    public static Lobby instance;

    @Override
    public void onPluginEnable() {
        instance = this;

        checkConfig();

        registerListener(new PlayerMove());
        registerListener(new PlayerLogin());
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
