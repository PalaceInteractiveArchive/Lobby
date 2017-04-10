package network.palace.lobby;

import lombok.Getter;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.core.plugin.Plugin;
import network.palace.core.plugin.PluginInfo;
import network.palace.lobby.command.*;
import network.palace.lobby.listeners.*;
import network.palace.lobby.resourcepack.PackManager;
import network.palace.lobby.util.InventoryNav;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;

import java.io.File;

@PluginInfo(name = "Lobby", version = "1.1.1", depend = {"Core"}, canReload = true)
public class Lobby extends Plugin {

    @Getter private InventoryNav inventoryNav;

    @Getter private Location spawn;

    @Override
    public void onPluginEnable() {
        checkConfig();
        registerCommands();
        registerListeners();
        inventoryNav = new InventoryNav();

        spawn = new Location(Bukkit.getWorld(getConfig().getString("world")),
                getConfig().getInt("x"), getConfig().getInt("y"), getConfig().getInt("z"),
                getConfig().getInt("yaw"), getConfig().getInt("pitch"));

        for (CPlayer player : Core.getPlayerManager().getOnlinePlayers()) {
            player.getHeaderFooter().setHeader(ChatColor.GOLD + "Palace Network - A Family of Servers");
            player.getHeaderFooter().setFooter(ChatColor.LIGHT_PURPLE + "You're at the " + ChatColor.GOLD + getConfig().getString("serverName"));
            inventoryNav.giveNav(player);
            player.setGamemode(GameMode.ADVENTURE);
            if (getConfig().getBoolean("titleEnabled")) {
                player.getActionBar().show(ChatColor.LIGHT_PURPLE + "Use your Nether Star to navigate!");
            }
            if (player.getRank().getRankId() >= Rank.SPECIALGUEST.getRankId()) {
                player.setAllowFlight(true);
            }
            if (Lobby.getPlugin(Lobby.class).getConfig().getBoolean("flightForDonorsEnabled") && player.getRank().getRankId() >= Rank.DWELLER.getRankId()) {
                player.setAllowFlight(true);
            }
        }
    }

    @Override
    public void onPluginDisable() {
        for (CPlayer player : Core.getPlayerManager().getOnlinePlayers()) {
            player.getInventory().clear(4);
            player.closeInventory();
        }
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
        registerListener(new LaunchPad());
        registerListener(new PlayerDropItem());
        registerListener(new PlayerFood());
        registerListener(new PlayerInteract());
        registerListener(new PlayerLogin());
        registerListener(new PlayerMove());
        registerListener(new TntExplosion());
        registerListener(new PackManager());
    }
}
