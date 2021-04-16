package simblock.test;

import simblock.block.Block;
import simblock.block.Transaction;

import java.util.ArrayList;
import java.util.List;

public class BlockTest {
    /**
     * temp test
     */
    public static void main(String[] args) {
        List<Transaction> tmp = new ArrayList<Transaction>();
        tmp.add(new Transaction(0,0, 1));

        Block b1 = new Block(null, null,1, tmp);
        Block b2 = new Block(b1, null, 2,tmp);

        System.out.println("b1 root hash:" + b1.getRootHash());
        System.out.println("b1 pre hash:" + b1.getPreviousHash());
        System.out.println("b1 cur hash:" + b1.getCurrentHash());

        System.out.println("b2 root hash:" + b2.getRootHash());
        System.out.println("b2 pre hash:" + b2.getPreviousHash());
        System.out.println("b2 cur hash:" + b2.getCurrentHash());

        System.out.println(b2.getTxnList().toString());
    }
}
