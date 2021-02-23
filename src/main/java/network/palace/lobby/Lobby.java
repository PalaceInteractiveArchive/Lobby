package network.palace.lobby;

import lombok.Getter;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.plugin.Plugin;
import network.palace.core.plugin.PluginInfo;
import network.palace.lobby.command.LobbyCommand;
import network.palace.lobby.command.SpawnCommand;
import network.palace.lobby.command.TutorialCommand;
import network.palace.lobby.listeners.*;
import network.palace.lobby.resourcepack.PackManager;
import network.palace.lobby.scoreboard.ScoreboardManager;
import network.palace.lobby.tutorial.TutorialManager;
import network.palace.lobby.util.*;

@PluginInfo(name = "Lobby", version = "1.1.8", depend = {"Core"}, canReload = true, apiversion = "1.13")
public class Lobby extends Plugin {
    @Getter private static Lobby instance;
    @Getter private static InventoryNav inventoryNav;
    @Getter private static HubSelector hubSelector;
    @Getter private static ConfigUtil configUtil;
    @Getter private static InventoryUtil inventoryUtil;
    @Getter private static WorldUtil worldUtil;
    @Getter private static TutorialManager tutorialManager;
    @Getter private static ScoreboardManager scoreboardManager;

    @Override
    public void onPluginEnable() {
        instance = this;
        registerListeners();
        registerCommands();
        inventoryNav = new InventoryNav();
        hubSelector = new HubSelector();
        configUtil = new ConfigUtil();
        inventoryUtil = new InventoryUtil();
        worldUtil = new WorldUtil();
        tutorialManager = new TutorialManager();
        scoreboardManager = new ScoreboardManager();

        for (CPlayer player : Core.getPlayerManager().getOnlinePlayers()) {
            PlayerLogin.handleJoin(player);
        }
    }

    @Override
    public void onPluginDisable() {
        for (CPlayer player : Core.getPlayerManager().getOnlinePlayers()) {
            player.closeInventory();
        }
        worldUtil.shutdown();
    }

    private void registerCommands() {
        registerCommand(new LobbyCommand());
        registerCommand(new SpawnCommand());
        registerCommand(new TutorialCommand());
    }

    private void registerListeners() {
//        registerListener(new CosmeticListener());
        registerListener(new InventoryClick());
        registerListener(new PacketListener());
        registerListener(new PlayerChat());
        registerListener(new PlayerDropItem());
        registerListener(new PlayerFood());
        registerListener(new PlayerInteract());
        registerListener(new PlayerLogin());
        registerListener(new PlayerMove());
        registerListener(new TntExplosion());
        registerListener(new PackManager());
    }
}
