package simblock.block;

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

    @Override
    public String toString() {
        return "{" +
                "\"value\":" + value +
                ",\"node_id\":" + node_id +
                '}';
    }
}
