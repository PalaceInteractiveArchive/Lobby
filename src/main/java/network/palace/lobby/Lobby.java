package network.palace.lobby;

import lombok.Getter;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.core.plugin.Plugin;
import network.palace.core.plugin.PluginInfo;
import network.palace.lobby.command.LobbyCommand;
import network.palace.lobby.listeners.*;
import network.palace.lobby.resourcepack.PackManager;
import network.palace.lobby.util.*;
import org.bukkit.GameMode;

@PluginInfo(name = "Lobby", version = "1.1.9", depend = {"Core"}, canReload = true, apiversion = "1.13")
public class Lobby extends Plugin {
    @Getter private static Lobby instance;
    @Getter private static InventoryNav inventoryNav;
    @Getter private static HubSelector hubSelector;
    @Getter private static ConfigUtil configUtil;
    @Getter private static InventoryUtil inventoryUtil;
    @Getter private static ParkourUtil parkourUtil;

    @Override
    public void onPluginEnable() {
        instance = this;
        registerListeners();
        registerCommands();
        configUtil = new ConfigUtil();
        inventoryUtil = new InventoryUtil();
        inventoryNav = new InventoryNav();
        hubSelector = new HubSelector();
        parkourUtil = new ParkourUtil();

        for (CPlayer player : Core.getPlayerManager().getOnlinePlayers()) {
            player.resetPlayer();
            player.setGamemode(GameMode.ADVENTURE);
            inventoryUtil.handleJoin(player);

            if (player.getRank().getRankId() >= Rank.SHAREHOLDER.getRankId()) player.setAllowFlight(true);
        }
    }

    @Override
    public void onPluginDisable() {
        for (CPlayer player : Core.getPlayerManager().getOnlinePlayers()) {
            player.closeInventory();
        }
    }

    private void registerCommands() {
        registerCommand(new LobbyCommand());
    }

    private void registerListeners() {
//        registerListener(new CosmeticListener());
        registerListener(new InventoryClick());
        registerListener(new PlayerDropItem());
        registerListener(new PlayerFood());
        registerListener(new PlayerInteract());
        registerListener(new PlayerLogin());
        registerListener(new PlayerMove());
        registerListener(new TntExplosion());
        registerListener(new PackManager());
    }
}
