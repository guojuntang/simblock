package simblock.simulator;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import simblock.block.Block;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Set;

import static simblock.settings.SimulationConfiguration.DB_NAME;

public class MongoDBJDBC {
    private MongoClient client;

    private MongoDatabase database;

    private MongoCollection<Document> collection;

    private String collection_name;

    public MongoDBJDBC(String address, int port){
        try {
            this.client = new MongoClient(address, port);
            this.database = this.client.getDatabase(DB_NAME);
            this.collection_name = "simblock_" + System.currentTimeMillis();
            this.database.createCollection(this.collection_name);
            this.collection = database.getCollection(this.collection_name);
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public MongoDBJDBC(String address, int port, String collection_name){
        try {
            this.client = new MongoClient(address, port);
            this.database = this.client.getDatabase(DB_NAME);
            this.collection_name = collection_name;
            this.collection = database.getCollection(this.collection_name);
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void persist(List<Block> blockList, Set<Block> orphans){
        Document document;
        for (Block b: blockList) {
            document = b.getDocument();
            // 1 for orphan, 0 for on chain
            document.append("orphan", (orphans.contains(b)? 1 : 0));
            this.collection.insertOne(document);
        }
    }

    private List<String> query(Bson f){
        List<String> result = new ArrayList<>();
        MongoCursor<Document> cursor = this.collection.find(f).iterator();
        try {
            while (cursor.hasNext()) {
                result.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    public List<String> getAllBlocks(){
        // identically-true to get all blocks
        return this.query(Filters.exists("_id"));
    }

    public List<String> getBlocksOnChain(){
        return this.query(Filters.eq("orphan", 0));
    }

    public List<String> getOrphans(){
        return this.query(Filters.eq("orphan", 0));
    }

}
