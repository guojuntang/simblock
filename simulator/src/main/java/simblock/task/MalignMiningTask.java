package simblock.task;

import simblock.block.Transaction;
import simblock.node.Node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import simblock.block.ProofOfWorkBlock;

import static simblock.settings.SimulationConfiguration.MALIGN_HASH;
import static simblock.simulator.Timer.getCurrentTime;

public class MalignMiningTask extends AbstractMintingTask{
    private final BigInteger difficulty;

    /**
     * Instantiates a new Mining task.
     *
     * @param minter     the minter
     * @param interval   the interval
     * @param difficulty the difficulty
     */
    //TODO how is the difficulty expressed and used here?
    public MalignMiningTask(Node minter, long interval, BigInteger difficulty) {
        super(minter, interval);
        this.difficulty = difficulty;
    }

    @Override
    public void run() {
        List<Transaction> tmp = new ArrayList<Transaction>();
        tmp.add(Transaction.rewardedTxn(this.getMinter()));
        // move the transactions in buffer into transactions list of new block
        for (Transaction s: this.getMinter().getBuffer()) {
            tmp.add(s);
        }
        ProofOfWorkBlock createdBlock = new ProofOfWorkBlock(
                (ProofOfWorkBlock) this.getParent(), this.getMinter(), getCurrentTime(), tmp,
                this.difficulty
        );
        // the malign node's merkle hash will be set to 00000000000000000000000000000000
        createdBlock.setRootHash(MALIGN_HASH);
        this.getMinter().receiveBlock(createdBlock);
    }
}
