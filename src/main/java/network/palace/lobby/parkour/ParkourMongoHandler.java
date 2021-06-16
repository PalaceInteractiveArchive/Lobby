package network.palace.lobby.parkour;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class ParkourMongoHandler {
    private MongoDatabase db = Core.getMongoHandler().getDatabase();
    private MongoCollection<Document> collection;

    public ParkourMongoHandler() {
        this.collection = db.getCollection("parkour");
    }

    public void updateUserParkourTime(CPlayer p, long seconds) {
        Document user = collection.find(eq("uuid", p.getUniqueId().toString())).first();
        if (user == null) {
            collection.insertOne(new Document("uuid", p.getUniqueId().toString()));
            collection.updateOne(eq("uuid", p.getUniqueId().toString()), new Document("$set", new Document("time", seconds)));
        } else {
            try {
                if (user.getLong("time") > seconds) {
                    collection.updateOne(eq("uuid", p.getUniqueId().toString()), new Document("$set", new Document("time", seconds)));
                }
            } catch(Exception e) {
                collection.updateOne(eq("uuid", p.getUniqueId().toString()), new Document("$set", new Document("time", seconds)));
            }
        }
    }

    public void updateUserInfParkour(CPlayer p, int score) {
        Document user = collection.find(eq("uuid", p.getUniqueId().toString())).first();
        if (user == null) {
            collection.insertOne(new Document("uuid", p.getUniqueId().toString()));
            collection.updateOne(eq("uuid", p.getUniqueId().toString()), new Document("$set", new Document("infinite", score)));
        } else {
            try {
                if (user.getInteger("infinite") < score) {
                    collection.updateOne(eq("uuid", p.getUniqueId().toString()), new Document("$set", new Document("infinite", score)));
                }
            } catch (Exception e) {
                collection.updateOne(eq("uuid", p.getUniqueId().toString()), new Document("$set", new Document("infinite", score)));
            }
        }
    }

    public List<Document> getTopFiveOnTime() {
        Bson sort = ascending("time");
        FindIterable<Document> topFive = collection.find(gte("time", 0)).sort(sort);
        topFive.limit(5);
        List<Document> docs = new ArrayList<>();
        for (Document doc : topFive) {
            docs.add(doc);
        }
        return docs;
    }

    public List<Document> getTopFiveOnScore() {
        Bson sort = descending("infinite");
        FindIterable<Document> topFive = collection.find(gte("infinite", 0)).sort(sort);
        topFive.limit(5);
        List<Document> docs = new ArrayList<>();
        for (Document doc : topFive) {
            docs.add(doc);
        }
        return docs;
    }
}
