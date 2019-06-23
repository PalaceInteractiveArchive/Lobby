package network.palace.lobby.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import network.palace.core.Core;
import network.palace.core.utils.MiscUtil;
import org.bukkit.Location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

@Getter
public class ConfigUtil {
    private String serverPack = "WDW";
    private Location spawn;

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
            }

            Core.logMessage("Lobby", "Loaded the Lobby config!");
        } catch (IOException e) {
            Core.logMessage("Lobby", "There was an error loading the Lobby config!");
            e.printStackTrace();
        }
    }

    public void setServerPack(String pack) {
        serverPack = pack;
        saveToFile();
    }

    public void setSpawn(Location loc) {
        spawn = loc.clone();
        saveToFile();
    }

    public void saveToFile() {
        JsonObject configObject = new JsonObject();
        if (serverPack != null) configObject.addProperty("resource-pack", serverPack);
        if (spawn != null) configObject.add("spawn", MiscUtil.getJson(spawn));
        try {
            Files.write(Paths.get(new File("plugins/Lobby/config.json").toURI()), Collections.singletonList(configObject.toString()), Charset.forName("UTF-8"));
        } catch (IOException e) {
            Core.logMessage("FoodManager", "There was an error writing to the FoodManager config!");
            e.printStackTrace();
        }
    }
}
