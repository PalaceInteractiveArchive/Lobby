package com.thepalace;

import com.thepalace.command.Spawn;
import com.thepalace.core.plugin.Plugin;
import com.thepalace.core.plugin.PluginInfo;
import com.thepalace.listeners.*;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.staticaccess.VanishNoPacket;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;

import java.io.File;

/**
 * Created by Innectic on 11/18/2016.
 */
@PluginInfo(name = "Lobby")
public class Lobby extends Plugin {

    public static Lobby instance;
    public VanishManager vanishManager;

    @Override
    public void onPluginEnable() {
        instance = this;

        try {
            vanishManager = VanishNoPacket.getManager();
        } catch (VanishNotLoadedException e) {
            e.printStackTrace();
        }

        checkConfig();

        registerListener(new PlayerMove());
        registerListener(new PlayerLogin());
        registerListener(new InventoryClick());
        registerListener(new PlayerInteract());
        registerListener(new PlayerDropItem());

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
