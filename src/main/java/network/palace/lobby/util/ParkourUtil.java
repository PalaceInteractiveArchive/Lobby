package network.palace.lobby.util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import network.palace.lobby.parkour.PlayerParkourUtil;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ParkourUtil {
    private final List<PlayerParkourUtil> playersInParkour = new ArrayList<>();
    private final Hologram holo;


    public ParkourUtil() {
        Core.logMessage("Parkour", "Parkour Util started and running");
        holo = HologramsAPI.createHologram(Lobby.getInstance(), new Location(Core.getWorld("hub"), -39, 66, -82));
        holo.appendTextLine("Loading");
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            Core.logMessage("Parkour", "Updating Leaderboard");
            holo.clearLines();
            holo.appendTextLine(ChatColor.AQUA + "" + ChatColor.ITALIC + "♽ Parkour Leaderboard ♽");
            holo.appendTextLine(ChatColor.GREEN + "The top 5 users on the set parkour based on time:");
            List<Document> docs = Lobby.getParkourMongoHandler().getTopFiveOnTime();
            int pos = 1;
            for (Document doc : docs) {
                long time = doc.getLong("time");
                Document player = Core.getMongoHandler().getPlayer(UUID.fromString(doc.getString("uuid")));
                String name = player.getString("username");
                holo.appendTextLine(ChatColor.LIGHT_PURPLE + "" + pos + ". " + name + " - " + String.format("%02d:%02d:%02d", time / 3600, (time / 60) % 60, time % 60));
                pos++;
            }
        },0, 1200);
    }

    public void addToParkour(CPlayer p) {
        if (!listContainsPlayer(p)) {
            playersInParkour.add(new PlayerParkourUtil(p.getUniqueId()));
            ItemStack checkpoint = new ItemStack(Material.IRON_PLATE);
            checkpoint.addUnsafeEnchantment(Enchantment.MENDING, 1);
            ItemMeta checkpointMeta = checkpoint.getItemMeta();
            checkpointMeta.setLore(Collections.singletonList(ChatColor.BLUE + "Used to teleport you back to the last checkpoint"));
            checkpointMeta.setLocalizedName(ChatColor.GREEN + "Return to last checkpoint");
            checkpoint.setItemMeta(checkpointMeta);
            p.getInventory().setItem(3, checkpoint);
            p.getInventory().setHeldItemSlot(3);
            ItemStack barrier = new ItemStack(Material.BARRIER);
            barrier.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            ItemMeta barrierMeta = barrier.getItemMeta();
            barrierMeta.setLocalizedName(ChatColor.RED + "Quit Parkour");
            barrierMeta.setLore(Collections.singletonList(ChatColor.RED + "Quit the Parkour"));
            barrier.setItemMeta(barrierMeta);
            p.getInventory().setItem(5, barrier);
        }
    }

    public boolean checkInParkour(CPlayer p) {
        return listContainsPlayer(p);
    }

    public void removeFromParkour(CPlayer p, Boolean legit) {
        Optional<PlayerParkourUtil> user = getPlayerUtil(p);
        user.ifPresent(playersInParkour::remove);
        p.getInventory().setItem(3, new ItemStack(Material.AIR));
        p.getInventory().setItem(5, new ItemStack(Material.AIR));
        if (user.isPresent() && legit) {
            long seconds = user.get().markEndTime();
            Lobby.getParkourMongoHandler().updateUserParkourTime(p, seconds);
            p.sendMessage(ChatColor.AQUA + "You finished the parkour with a time of " + ChatColor.BOLD + ChatColor.GREEN + String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60) + "!");
        }
    }

    public void setUserCheckpoint(CPlayer p, Location l) {
        Optional<PlayerParkourUtil> user = getPlayerUtil(p);
        user.ifPresent(playerParkourUtil -> playerParkourUtil.updateCheckpoint(l));
    }

    public Location getUserCheckpoint(CPlayer p) {
        Optional<PlayerParkourUtil> user = getPlayerUtil(p);
        if (user.isPresent()) {
            return user.get().getLastCheckpoint();
        } else {
            return p.getWorld().getSpawnLocation();
        }
    }

    private boolean listContainsPlayer(CPlayer p) {
        Optional<PlayerParkourUtil> user = playersInParkour.stream().filter(e -> e.getUserId().equals(p.getUniqueId())).findFirst();

        return user.isPresent();
    }

    private Optional<PlayerParkourUtil> getPlayerUtil(CPlayer p) {
        if (listContainsPlayer(p)) {
            return playersInParkour.stream().filter(e -> e.getUserId().equals(p.getUniqueId())).findFirst();
        } else {
            return Optional.empty();
        }
    }

}
