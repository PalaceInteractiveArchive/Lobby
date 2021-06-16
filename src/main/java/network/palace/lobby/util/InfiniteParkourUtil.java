package network.palace.lobby.util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import network.palace.lobby.parkour.InfinitePlayerUtil;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.*;

@SuppressWarnings("deprecation")
public class InfiniteParkourUtil {
    private final List<InfinitePlayerUtil> playersInParkour = new ArrayList<>();
    private final Location bottomCorner = new Location(Core.getWorld("hub"), 24, 117, -14);
    private final Location topCorner = new Location(Core.getWorld("hub"), 145, 157, 113);
    private final Hologram holo;

    public InfiniteParkourUtil() {
        holo = HologramsAPI.createHologram(Lobby.getInstance(), new Location(Core.getWorld("hub"), -51, 66, -77));
        holo.appendTextLine("Loading");
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            Core.logMessage("Infinite Parkour", "Updating Leaderboard");
            holo.clearLines();
            holo.appendTextLine(ChatColor.AQUA + "" + ChatColor.ITALIC + "♽ Parkour Leaderboard ♽");
            holo.appendTextLine(ChatColor.GREEN + "The top 5 users on the infinite parkour based on score:");
            List<Document> docs = Lobby.getParkourMongoHandler().getTopFiveOnScore();
            int pos = 1;
            for (Document doc : docs) {
                int score = doc.getInteger("infinite");
                Document player = Core.getMongoHandler().getPlayer(UUID.fromString(doc.getString("uuid")));
                String name = player.getString("username");
                holo.appendTextLine(ChatColor.LIGHT_PURPLE + "" + pos + ". " + name + " - " + score);
                pos++;
            }
        },0, 1200);
    }

    public void startParkour(CPlayer p) {
        Random random = new Random();

        int colour = random.nextInt((15 - 1) + 1) + 1;
        Location startBlock = randomLocation();
        startBlock.getBlock().setType(Material.CONCRETE);
        startBlock.getBlock().setData((byte) colour);

        InfinitePlayerUtil user = new InfinitePlayerUtil(p.getUuid());
        user.setColour(colour);

        Location secondBlock = getNextLocation(startBlock);
        secondBlock.getBlock().setType(Material.CONCRETE);
        secondBlock.getBlock().setData((byte) user.getColour());

        Location thirdBlock = getNextLocation(secondBlock);
        thirdBlock.getBlock().setType(Material.CONCRETE);
        thirdBlock.getBlock().setData((byte) user.getColour());

        user.setBlockOne(startBlock);
        user.setBlockTwo(secondBlock);
        user.setBlockThree(thirdBlock);

        if (!inArea(startBlock, bottomCorner, topCorner) || !inArea(secondBlock, bottomCorner, topCorner) || !inArea(thirdBlock, bottomCorner, topCorner)) {
            startBlock.getBlock().setType(Material.AIR);
            secondBlock.getBlock().setType(Material.AIR);
            thirdBlock.getBlock().setType(Material.AIR);
            startParkour(p);
        } else {
            Location playerTp = startBlock.clone();
            playerTp.setY(startBlock.getY() + 1);
            p.teleport(playerTp);
            playersInParkour.add(user);
        }
    }

    public void endParkour(CPlayer p) {
        p.teleport(Lobby.getConfigUtil().getSpawn());
        Optional<InfinitePlayerUtil> user = getPlayerUtil(p);
        user.ifPresent(playersInParkour::remove);
        if (user.isPresent()) {
            user.get().getBlockOne().getBlock().setType(Material.AIR);
            user.get().getBlockTwo().getBlock().setType(Material.AIR);
            user.get().getBlockThree().getBlock().setType(Material.AIR);
            Lobby.getParkourMongoHandler().updateUserInfParkour(p, user.get().getScore());
            p.sendMessage(ChatColor.AQUA + "Thanks for playing the " + ChatColor.ITALIC + "Infinite Parkour!" + ChatColor.RESET + ChatColor.AQUA + " You finished with a score of: " + user.get().getScore());
        }
    }

    public boolean checkIfFailed(CPlayer p) {
        Optional<InfinitePlayerUtil> user = getPlayerUtil(p);
        if (user.isPresent()) {
            if (p.getLocation().getY() < user.get().getBlockTwo().getY() - 6) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean checkInParkour(CPlayer p) {
        return listContainsPlayer(p);
    }

    public void increaseScore(CPlayer p) {
        Optional<InfinitePlayerUtil> user = getPlayerUtil(p);
        user.ifPresent(InfinitePlayerUtil::increaseScore);
        user.ifPresent(infinitePlayerUtil -> p.getActionBar().show(ChatColor.GOLD + "Current Score: " + infinitePlayerUtil.getScore()));
        if (user.isPresent()) {
            if (user.get().getScore() % 50 == 0) {
                p.addBalance(5, "Parkour Multiple of 50");
                p.sendMessage(ChatColor.AQUA + "You earned $5 for hitting " + user.get().getScore());
            }
            if (user.get().getScore() % 200 == 0) {
                p.addTokens(5, "Parkour Multiple of 200");
                p.sendMessage(ChatColor.AQUA + "You earned 5 tokens for hitting " + user.get().getScore());

            }
        }
    }

    public void checkNextBlock(CPlayer p) {
        Optional<InfinitePlayerUtil> user = getPlayerUtil(p);
        if (user.isPresent()) {
            Location correctBlock = user.get().getBlockTwo();
            Location oldBlock = user.get().getBlockOne();
            double y = correctBlock.getY();
            double oldY = oldBlock.getY();
            Location newLoc = correctBlock.clone();
            Location oldLoc = oldBlock.clone();
            newLoc.setY(y + 1);
            oldLoc.setY(oldY + 1);

            if (p.getLocation().distance(newLoc) < 1 && p.getLocation().distance(oldBlock) > 1) updateLocations(user.get(), p);
        }
    }

    private void updateLocations(InfinitePlayerUtil util, CPlayer p) {
        Location old = util.getBlockOne();

        util.setBlockOne(util.getBlockTwo());
        util.setBlockTwo(util.getBlockThree());
        boolean inArena;
        Location newLoc;
        int i = 0;
        do {
            i++;
            newLoc = getNextLocation(util.getBlockThree());
            inArena = inArea(newLoc, bottomCorner, topCorner);
            if (inArena) {
                inArena = !newLoc.equals(util.getBlockOne());
                if (inArena) inArena = !newLoc.equals(util.getBlockTwo());
                if (inArena) {
                    Location bottomBlockOne = util.getBlockOne().clone();
                    Location topBlockOne = util.getBlockOne().clone();
                    bottomBlockOne.setY(util.getBlockOne().getY() - 3);
                    bottomBlockOne.setX(util.getBlockOne().getX() - 1);
                    bottomBlockOne.setZ(util.getBlockOne().getZ() - 1);
                    topBlockOne.setY(util.getBlockOne().getY() + 3);
                    topBlockOne.setX(util.getBlockOne().getX() + 1);
                    topBlockOne.setZ(util.getBlockOne().getZ() + 1);

                    inArena = !inArea(newLoc, bottomBlockOne, topBlockOne);

                    if (inArena) {
                        Location bottomBlockTwo = util.getBlockTwo().clone();
                        Location topBlockTwo = util.getBlockTwo().clone();
                        bottomBlockTwo.setY(util.getBlockTwo().getY() - 3);
                        bottomBlockTwo.setX(util.getBlockTwo().getX() - 1);
                        bottomBlockTwo.setZ(util.getBlockTwo().getZ() - 1);
                        topBlockTwo.setY(util.getBlockTwo().getY() + 3);
                        topBlockTwo.setX(util.getBlockTwo().getX() + 1);
                        topBlockTwo.setZ(util.getBlockTwo().getZ() + 1);

                        inArena = !inArea(newLoc, bottomBlockTwo, topBlockTwo);

                        if (inArena) {
                            if (newLoc.getBlock().getType() != Material.AIR) {
                                inArena = false;
                            }
                            old.getBlock().setType(Material.AIR);
                        }
                    }

                }
            }
            if (i == 20) {
                inArena = true;
                failedInArena(util, p);
            }
        } while (!inArena);

        newLoc.getBlock().setType(Material.CONCRETE);
        newLoc.getBlock().setData((byte) util.getColour());
        util.setBlockThree(newLoc);
        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 0);
        increaseScore(p);
    }

    private void failedInArena(InfinitePlayerUtil util, CPlayer p) {
        util.getBlockOne().getBlock().setType(Material.AIR);
        util.getBlockTwo().getBlock().setType(Material.AIR);
        util.getBlockThree().getBlock().setType(Material.AIR);

        Location startBlock = randomLocation();
        startBlock.getBlock().setType(Material.CONCRETE);
        startBlock.getBlock().setData((byte) util.getColour());

        Location secondBlock = startBlock.clone();
        secondBlock.getBlock().setType(Material.CONCRETE);
        secondBlock.getBlock().setData((byte) util.getColour());

        Location thirdBlock = getNextLocation(secondBlock);
        thirdBlock.getBlock().setType(Material.CONCRETE);
        thirdBlock.getBlock().setData((byte) util.getColour());

        util.setBlockOne(startBlock);
        util.setBlockTwo(secondBlock);
        util.setBlockThree(thirdBlock);

        if (!inArea(startBlock, bottomCorner, topCorner) || !inArea(secondBlock, bottomCorner, topCorner) || !inArea(thirdBlock, bottomCorner, topCorner)) {
            startBlock.getBlock().setType(Material.AIR);
            secondBlock.getBlock().setType(Material.AIR);
            thirdBlock.getBlock().setType(Material.AIR);
            failedInArena(util, p);
        } else {
            Location playerTp = startBlock.clone();
            playerTp.setY(startBlock.getY() + 1);
            p.teleport(playerTp);
        }
    }

    private Location getNextLocation(Location l) {
        Random random = new Random();
        double nextX;
        double nextZ;
        double xOrZ = random.nextInt(2);
        double minusOrPositive = random.nextInt(2);
        if (xOrZ == 1) {
            if (minusOrPositive == 1) {
                nextX = random.nextInt((3 - 2) + 1) + 2;
            } else {
                nextX = random.nextInt((-2 - -3) + 1) + -3;
            }
            nextZ = 0;
        } else {
            if (minusOrPositive == 1) {
                nextZ = random.nextInt((3 - 2) + 1) + 2;
            } else {
                nextZ = random.nextInt((-2 - -3) + 1) + -3;
            }
            nextX = 0;
        }
        double nextY = random.nextInt((1 - -1) + 1) + -1;

        return new Location(Core.getWorld("hub"), l.getX() + nextX, l.getY()+ nextY, l.getZ() + nextZ);
    }

    private boolean listContainsPlayer(CPlayer p) {
        Optional<InfinitePlayerUtil> user = playersInParkour.stream().filter(e -> e.getUserId().equals(p.getUniqueId())).findFirst();

        return user.isPresent();
    }

    private Optional<InfinitePlayerUtil> getPlayerUtil(CPlayer p) {
        if (listContainsPlayer(p)) {
            return playersInParkour.stream().filter(e -> e.getUserId().equals(p.getUniqueId())).findFirst();
        } else {
            return Optional.empty();
        }
    }

    private Location randomLocation() {
        Location range = new Location(bottomCorner.getWorld(), Math.abs(topCorner.getX() - bottomCorner.getX()), bottomCorner.getY(), Math.abs(topCorner.getZ() - bottomCorner.getZ()));
        return new Location(bottomCorner.getWorld(), (Math.random() * range.getX()) + (bottomCorner.getX() <= topCorner.getX() ? bottomCorner.getX() : topCorner.getX()), range.getY(), (Math.random() * range.getZ()) + (bottomCorner.getZ() <= topCorner.getZ() ? bottomCorner.getZ() : topCorner.getZ()));
    }

    private boolean inArea(Location targetLocation, Location inAreaLocation1, Location inAreaLocation2){
        if(inAreaLocation1.getWorld().getName().equals(inAreaLocation2.getWorld().getName())){
            if(targetLocation.getWorld().getName().equals(inAreaLocation1.getWorld().getName())){
                if((targetLocation.getBlockX() >= inAreaLocation1.getBlockX() && targetLocation.getBlockX() <= inAreaLocation2.getBlockX()) || (targetLocation.getBlockX() <= inAreaLocation1.getBlockX() && targetLocation.getBlockX() >= inAreaLocation2.getBlockX())){
                    if((targetLocation.getBlockZ() >= inAreaLocation1.getBlockZ() && targetLocation.getBlockZ() <= inAreaLocation2.getBlockZ()) || (targetLocation.getBlockZ() <= inAreaLocation1.getBlockZ() && targetLocation.getBlockZ() >= inAreaLocation2.getBlockZ())){
                        if((targetLocation.getBlockY() >= inAreaLocation1.getBlockY() && targetLocation.getBlockY() <= inAreaLocation2.getBlockY()) || (targetLocation.getBlockY() <= inAreaLocation1.getBlockY() && targetLocation.getBlockY() >= inAreaLocation2.getBlockY())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
