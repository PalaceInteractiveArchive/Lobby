package com.thepalace;

import com.thepalace.command.*;
import com.thepalace.core.Core;
import com.thepalace.core.plugin.Plugin;
import com.thepalace.core.plugin.PluginInfo;
import com.thepalace.listeners.*;
import com.thepalace.resourcepack.PackManager;
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

        if (doesPalaceCoreExist()) {
            registerListener(new PackManager());
            registerListener(new DonatorFlight());
            if (doesVanishExist()) {
                registerListener(new VanishJoinListener());
            }
        } else {
            Core.logMessage(getInfo().name(), ChatColor.RED + "PALACE CORE IS NOT LOADED");
        }

        registerCommand(new ServerName());
        registerCommand(new Spawn());
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

    private boolean doesPalaceCoreExist() {
        try  {
            Class.forName("com.palacemc.palacecore.PalaceCore");
            return true;
        }  catch (ClassNotFoundException e) {
            return false;
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
