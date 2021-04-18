package simblock.block;

import org.bson.Document;
import simblock.node.Node;
import simblock.util.Helper;

import java.util.*;

import static simblock.settings.SimulationConfiguration.COINBASE_ID;
import static simblock.settings.SimulationConfiguration.REWARD_COINS;
import static simblock.simulator.Timer.getCurrentTime;

/**
 * reference: https://github.com/wangweiX/blockchain-java/blob/part4-transaction1/src/main/java/one/wangwei/blockchain/transaction/Transaction.java
 */

public class Transaction {
    /**
     * transaction id(as hash)
     */
    private String tx_id;

    /**
     * transaction input list
     */
    private List<TXInput> inputs;

    /**
     * transaction output list
     */
    private List<TXOutput> outputs;

    /**
     * time
     */
    private long time;

    private void calId(){
        this.tx_id = Helper.calSHA256(inputs.toString() + outputs.toString());
    }

    public Transaction(List<TXInput> inputs,  List<TXOutput> outputs, long time){
        this.inputs = inputs;
        this.outputs = outputs;
        this.time = time;
        this.calId();
    }

    public String getTx_id(){
        return this.tx_id;
    }

    public List<TXInput> getInputs() {
        return inputs;
    }

    public List<TXOutput> getOutputs() {
        return outputs;
    }

    public Document getDocument(){
        Document document = new Document("tx_id", this.tx_id)
                .append("time", this.time);
        List<Document> inputList = new ArrayList<>();
        List<Document> outputList = new ArrayList<>();

        for (TXInput input: this.inputs) {
            inputList.add(input.getDocument());
        }

        for (TXOutput output: this.outputs) {
            outputList.add(output.getDocument());
        }
        document.append("inputs", inputList)
                .append("outputs", outputList);

        return document;
    }

    /**
     * check coinbase
     * @return true if coinbase
     */
    public boolean isCoinbase(){
        return (this.inputs.size() == 1)
                && (this.inputs.get(0).getId().length() == 0)
                && (this.inputs.get(0).getNode_id() == -1);
    }

    @Override
    public String toString() {
        return "{" +
                "\"tx_id\": \"" + tx_id + "\""+
                ", \"inputs\":" + inputs.toString() +
                ", \"outputs\":" + outputs.toString() +
                ", \"time\":" + time +
                '}';
    }

    private static int calReward(){
        return REWARD_COINS;
    }

    /**
     * create a transaction
     * @param from from address
     * @param to to address
     * @param amount amount
     * @param block current block
     * @return new Transaction
     * @throws Exception
     */
    public static Transaction newUTXOTransaction(int from, int to, int amount, Block block) throws Exception{
        SpendableOutputResult result = block.findSpendableOutputs(from, amount);
        int acc = result.getAcc();
        Map<String, List<Integer>> unspentOuts = result.getUnspentOuts();

        if (acc < amount){
            throw new Exception("not enough");
        }
        Iterator<Map.Entry<String, List<Integer>>> iterator = unspentOuts.entrySet().iterator();

        List<TXInput> txInputs = new ArrayList<TXInput>();
        while (iterator.hasNext()){
            Map.Entry<String, List<Integer>> entry = iterator.next();
            String txId = entry.getKey();
            List<Integer> outIds = entry.getValue();
            for (int outIndex: outIds) {
                txInputs.add(new TXInput(txId, outIndex, from));
            }
        }
        List<TXOutput> txOutputs = new ArrayList<TXOutput>();
        txOutputs.add(new TXOutput(amount, to));
        if (acc > amount){
            txOutputs.add(new TXOutput((acc - amount), from));
        }
        return new Transaction(txInputs, txOutputs, getCurrentTime());
    }

    /**
     * return coinbase transaction
     * @param minter
     * @return transaction string
     */
    public static Transaction rewardedTxn(Node minter){
        TXInput txInput = new TXInput(new String(), -1, -1);
        TXOutput txOutput = new TXOutput(calReward(), minter.getNodeID());
        List<TXInput> inputList =  new ArrayList<TXInput>();
        List<TXOutput> outputList =  new ArrayList<TXOutput>();
        inputList.add(txInput);
        outputList.add(txOutput);
        return new Transaction(inputList, outputList, getCurrentTime());
    }
}

