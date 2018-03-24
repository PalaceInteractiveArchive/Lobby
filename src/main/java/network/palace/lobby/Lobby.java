package network.palace.lobby;

import lombok.Getter;
import lombok.Setter;
import network.palace.core.Core;
import network.palace.core.command.CommandException;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.core.plugin.Plugin;
import network.palace.core.plugin.PluginInfo;
import network.palace.core.utils.ItemUtil;
import network.palace.lobby.command.LobbyCommand;
import network.palace.lobby.listeners.*;
import network.palace.lobby.resourcepack.PackManager;
import network.palace.lobby.util.HubSelector;
import network.palace.lobby.util.InventoryNav;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Arrays;

@PluginInfo(name = "Lobby", version = "1.1.2", depend = {"Core"}, canReload = true)
public class Lobby extends Plugin {
    @Getter private static Lobby instance;
    @Getter private InventoryNav inventoryNav;
    @Getter private HubSelector hubSelector;
    @Getter @Setter private boolean isHubSelectorEnabled = false;
    @Getter private Location spawn;
    @Getter @Setter private boolean packEnabled;
    @Getter @Setter private String packName;
    @Getter private static ItemStack cosmeticsItem = ItemUtil.create(Material.ENDER_CHEST, ChatColor.GREEN + "Cosmetics",
            Arrays.asList(ChatColor.GRAY + "Open Cosmetics Menu"));

    @Override
    public void onPluginEnable() {
        instance = this;
        checkConfig();
        registerListeners();
        registerCommands();
        inventoryNav = new InventoryNav();
        hubSelector = new HubSelector();

        isHubSelectorEnabled = getConfig().getBoolean("canSelectLobbies");

        spawn = new Location(Bukkit.getWorld(getConfig().getString("spawn.world")),
                getConfig().getInt("spawn.x"), getConfig().getInt("spawn.y"), getConfig().getInt("spawn.z"),
                getConfig().getInt("spawn.yaw"), getConfig().getInt("spawn.pitch"));

        packEnabled = getConfig().getBoolean("pack.send");
        packName = getConfig().getString("pack.name");

        resetPlayerHeaders();

        for (CPlayer player : Core.getPlayerManager().getOnlinePlayers()) {
            inventoryNav.giveNav(player);
            hubSelector.giveNav(player);
            player.getInventory().setItem(0, cosmeticsItem);

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

    public void resetPlayerHeaders() {
        Core.getPlayerManager().getOnlinePlayers().forEach(this::setPlayerHeader);
    }

    public void setPlayerHeader(CPlayer player) {
        String name = getConfig().getString("serverName");
        player.getHeaderFooter().setHeader(ChatColor.GOLD + "Palace Network - A Family of Servers");
        player.getHeaderFooter().setFooter(ChatColor.LIGHT_PURPLE + "You're at the " + ChatColor.GOLD + name);
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
        registerCommand(new LobbyCommand());
        registerCommand(new CoreCommand("test") {
            @Override
            protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
                CPlayer player = Core.getPlayerManager().getPlayer("Legobuilder0813");
                Inventory inv = player.getOpenInventory().get().getTopInventory();
                inv.setItem(0, new ItemStack(Material.STONE));
                Core.runTaskLater(() -> {
                    ItemStack i = new ItemStack(Material.STONE);
                    ItemMeta meta = i.getItemMeta();
                    meta.setDisplayName("HELLO");
                    i.setItemMeta(meta);
                    inv.setItem(0, i);
                }, 30L);
            }
        });
//        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.WINDOW_ITEMS) {
//            @Override
//            public void onPacketSending(PacketEvent event) {
//                PacketContainer packet = event.getPacket();
//                Bukkit.broadcastMessage(ChatColor.RED + "" + packet.getIntegers().read(0));
//                Bukkit.broadcastMessage(packet.toString());
//            }
//        });
    }

    private void registerListeners() {
        registerListener(new InventoryClick());
        registerListener(new LaunchPad());
        registerListener(new PacketListener());
        registerListener(new PlayerDropItem());
        registerListener(new PlayerFood());
        registerListener(new PlayerInteract());
        registerListener(new PlayerLogin());
        registerListener(new PlayerMove());
        registerListener(new TntExplosion());
        registerListener(new PackManager());
    }
}
