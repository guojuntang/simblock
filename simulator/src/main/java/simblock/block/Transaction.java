package simblock.block;

import simblock.node.Node;
import simblock.util.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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

