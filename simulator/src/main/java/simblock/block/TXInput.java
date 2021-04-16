package simblock.block;

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

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + id + "\"" +
                ", \"TXOutputIndex\":" + TXOutputIndex +
                ", \"node_id\":" + node_id +
                '}';
    }
}

