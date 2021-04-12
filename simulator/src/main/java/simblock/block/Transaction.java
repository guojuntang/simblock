package simblock.block;

public class Transaction {
    /**
     * from id(presenting for address)
     */
    private int fromId;

    /**
     * to id
     */
    private int toId;

    /**
     * transaction's value
     */
    private int value;

    public Transaction(int from, int to, int value){
        this.fromId = from;
        this.toId = to;
        this.value = value;
    }

    /**
     *
     * format string for calculating HASH
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", value=" + value +
                '}';
    }
}
