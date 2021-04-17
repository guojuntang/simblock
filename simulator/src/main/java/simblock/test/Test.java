package simblock.test;

import simblock.block.Block;
import simblock.block.Transaction;
import simblock.node.Node;

import java.util.ArrayList;
import java.util.List;

import static simblock.settings.SimulationConfiguration.TABLE;
import static simblock.settings.SimulationConfiguration.ALGO;

public class Test {
    public static void main(String[] args) {
        Node n = new Node(0, 0, 0, 0, TABLE, ALGO,false,false);
        List<Transaction> tmp = new ArrayList<Transaction>();
        tmp.add(Transaction.rewardedTxn(n));
        Block b = simblock.block.Block.genesisBlock(n, tmp);
        System.out.println(b.getTxnList());
        try {
            Transaction s = Transaction.newUTXOTransaction(n.getNodeID(), 10, 10, b);
            System.out.println(s);
            tmp.add(s);
            Block b1 = simblock.block.Block.genesisBlock(n, tmp);
            Transaction s1 = Transaction.newUTXOTransaction(n.getNodeID(), 5,5 , b1);
            System.out.println(s1);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
