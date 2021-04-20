package simblock.simulator;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import simblock.block.Block;
import org.bson.conversions.Bson;
import simblock.node.Node;

import static simblock.settings.SimulationConfiguration.*;
import static simblock.simulator.Timer.getCurrentTime;


import java.util.*;

public class MongoDBJDBC {
    private MongoClient client;

    private MongoDatabase database;

    private MongoCollection<Document> collection;

    private String collection_name;

    /**
     * Construct the object to persist data
     * @param address database address
     * @param port database port
     */
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

    /**
     * Construct the object to access data
     * @param address database address
     * @param port database port
     * @param collection_name the collection name you access
     */
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


    /**
     * persist the block data
     * @param blockList
     * @param orphans
     */
    public void persist(List<Block> blockList, Set<Block> orphans){
        this.persistSimulatorInformation(blockList);
        Document document;
        for (Block b: blockList) {
            document = b.getDocument();
            // 1 for orphan, 0 for on chain
            document.append("orphan", (orphans.contains(b)? 1 : 0));
            this.collection.insertOne(document);
        }
    }

    /**
     * Persist information of simulator
     * Include block number, node number etc
     * @param blockList
     */
    private void persistSimulatorInformation(List<Block> blockList){
        Document document = new Document("title", "simulator_information")
                .append("time", getCurrentTime())
                .append("block_num", blockList.size())
                .append("node_total_num", NUM_OF_NODES)
                .append("malign_node_num", NUM_OF_MALIGN_NODE)
                .append("chains", this.chainList(blockList))
                .append("corrupted_blocks", this.getCorruptedBlocks(blockList))
                .append("corrupted_block_num", this.getCorruptedBlocks(blockList).size());

        this.collection.insertOne(document);
    }

    /**
     * get the block which was corrupted by the malign nodes
     * @param blockList
     * @return
     */
    private List<Integer> getCorruptedBlocks(List<Block> blockList){
        List<Integer> result = new ArrayList<>();
        for (Block b :blockList) {
            if (b.getRootHash().equals(MALIGN_HASH)){
                result.add(b.getId());
            }
        }
        return result;
    }

    private List<List<Integer>> chainList(List<Block> blockList){
        Set<Block> set = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();
        for (int i = blockList.size()-1; i >= 0; i--) {
            Block b = blockList.get(i);
            if (!set.contains(b)){
                List<Integer> chain = new ArrayList<>();
                while (b != null){
                    set.add(b);
                    chain.add(b.getId());
                    b = b.getParent();
                }
                result.add(chain);
            }
        }
        return result;
    }

    private List<String> query(Bson f, Bson p){
        List<String> result = new ArrayList<>();
        MongoCursor<Document> cursor = this.collection.find(f).projection(p).iterator();
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
        // get all blocks
        return this.query(Filters.exists("block_id"), null);
    }


    public String getOneBlock(int block_id){
        List<String> result = this.query(Filters.eq("block_id", block_id), null);
        return (!result.isEmpty())? result.get(0): "";
    }

    public List<String> getAllTransactions(){
        return this.query(Filters.exists("block_id"), Projections.include("transactions"));
    }

    public String getSimulatorInformation(){
        List<String> result = this.query(Filters.eq("title", "simulator_information"), null);
        return (!result.isEmpty())? result.get(0): "";
    }

    public String getTransactionOnBlock(int block_id){
        List<String> result = this.query(Filters.eq("block_id", block_id), Projections.include("transactions"));
        return (!result.isEmpty())? result.get(0): "";
    }

    public List<String> getBlocksOnChain(){
        return this.query(Filters.eq("orphan", 0), null);
    }

    public List<String> getOrphans(){
        return this.query(Filters.eq("orphan", 1), null);
    }

}
