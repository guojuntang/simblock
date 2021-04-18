package simblock.block;

import org.bson.Document;

public class TXOutput {
    /**
     * output value
     */
    private int value;

    /**
     * use node_id as address
     */
    private int node_id;

    public TXOutput(int value, int node_id){
        this.value = value;
        this.node_id = node_id;
    }

    public Document getDocument(){
        return new Document("value", this.value)
                .append("node_id", this.node_id);
    }

    public int getNode_id() {
        return node_id;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{" +
                "\"value\":" + value +
                ",\"node_id\":" + node_id +
                '}';
    }
}
