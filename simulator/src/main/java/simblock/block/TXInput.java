package simblock.block;

import org.bson.Document;

public class TXInput {
    /**
     * input id
     */
    private String id;

    /**
     * TXOutput index
     */
    private int TXOutputIndex;

    /**
     * use node_id as address
     */
    private int node_id;

    /**
     * constructor of TXInput
     * @param id
     * @param TXOutputIndex
     * @param node_id
     */
    public TXInput(String id, int TXOutputIndex, int node_id){
        this.id = id;
        this.TXOutputIndex = TXOutputIndex;
        this.node_id = node_id;
    }

    public Document getDocument(){
        return new Document("id", this.id)
                .append("output_index", this.TXOutputIndex)
                .append("node_id", this.node_id);
    }

    public int getNode_id() {
        return node_id;
    }

    public int getTXOutputIndex() {
        return TXOutputIndex;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + id + "\"" +
                ", \"TXOutputIndex\":" + TXOutputIndex +
                ", \"node_id\":" + node_id +
                '}';
    }
}

