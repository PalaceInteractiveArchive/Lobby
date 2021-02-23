package network.palace.lobby.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import network.palace.core.Core;
import network.palace.core.utils.MiscUtil;
import network.palace.lobby.Lobby;
import org.bson.Document;
import org.bukkit.Location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ConfigUtil {
    private final MongoCollection<Document> serversCollection;
    private String serverPack = "WDW";
    private Location spawn, tutorialSpawn;

    private int lastParks = 0;
    private int lastCreative = 0;
    private int lastArcade = 0;
    private HashMap<String, Integer> lastHubs = new HashMap<>();

    public ConfigUtil() {
        try {
            File dir = new File("plugins/Lobby");
            if (!dir.exists()) dir.mkdirs();
            File config = new File("plugins/Lobby/config.json");
            if (!config.exists()) config.createNewFile();

            StringBuilder json = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(config));
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            JsonElement configElement = new Gson().fromJson(json.toString(), JsonElement.class);
            if (configElement == null) configElement = new JsonObject();

            if (configElement.isJsonObject()) {
                JsonObject configObject = configElement.getAsJsonObject();

                if (configObject.has("resource-pack")) {
                    serverPack = configObject.get("resource-pack").getAsString();
                } else {
                    serverPack = null;
                }

                spawn = MiscUtil.getLocation(configObject.getAsJsonObject("spawn"));
                tutorialSpawn = MiscUtil.getLocation(configObject.getAsJsonObject("tutorial-spawn"));
            }

            Core.logMessage("Lobby", "Loaded the Lobby config!");
        } catch (IOException e) {
            Core.logMessage("Lobby", "There was an error loading the Lobby config!");
            e.printStackTrace();
        }
        serversCollection = Core.getMongoHandler().getDatabase().getCollection("servers");
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            int parks = 0;
            int creative = 0;
            int arcade = 0;

            HashMap<String, Integer> hubs = new HashMap<>();
            for (Document doc : serversCollection.find(Filters.and(Filters.exists("playground", Core.isPlayground()), Filters.eq("online", true))).projection(new Document("name", true).append("count", true).append("park", true))) {
                if (doc == null) continue;
                String name = doc.getString("name");
                int count = doc.getInteger("count", 0);
                boolean park = doc.getBoolean("park");
                if (name.startsWith("Creative")) {
                    creative += count;
                } else if (name.startsWith("Arcade")) {
                    arcade += count;
                } else if (name.startsWith("Hub")) {
                    hubs.put(name, count);
                } else if (park) {
                    parks += count;
                }
            }

            if (parks == lastParks && creative == lastCreative && arcade == lastArcade) {
                boolean stop = !hubs.isEmpty() || !lastHubs.isEmpty();
                for (Map.Entry<String, Integer> entry : hubs.entrySet()) {
                    if (!lastHubs.containsKey(entry.getKey()) || !lastHubs.get(entry.getKey()).equals(entry.getValue())) {
                        stop = false;
                        break;
                    }
                }
                if (stop) return;
            }

            lastParks = parks;
            lastCreative = creative;
            lastArcade = arcade;
            lastHubs = hubs;

            Lobby.getInventoryNav().updateCounts(parks, creative, arcade);
            Lobby.getHubSelector().updateCounts(hubs);
        }, 20L, 100L);
    }

    public void setServerPack(String pack) {
        serverPack = pack;
        saveToFile();
    }

    public void setSpawn(Location loc) {
        spawn = loc.clone();
        saveToFile();
    }

    public void setTutorialSpawn(Location loc) {
        tutorialSpawn = loc.clone();
        saveToFile();
    }

    public void saveToFile() {
        JsonObject configObject = new JsonObject();
        if (serverPack != null) configObject.addProperty("resource-pack", serverPack);
        if (spawn != null) configObject.add("spawn", MiscUtil.getJson(spawn));
        if (tutorialSpawn != null) configObject.add("tutorial-spawn", MiscUtil.getJson(tutorialSpawn));
        try {
            Files.write(Paths.get(new File("plugins/Lobby/config.json").toURI()), Collections.singletonList(configObject.toString()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Core.logMessage("FoodManager", "There was an error writing to the FoodManager config!");
            e.printStackTrace();
        }
    }
}
