/*
 * Copyright 2019 Distributed Systems Group
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simblock.task;

import static simblock.simulator.Timer.getCurrentTime;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import simblock.block.ProofOfWorkBlock;
import simblock.block.Transaction;
import simblock.node.Node;
import simblock.node.consensus.ProofOfWork;

/**
 * The type Mining task.
 */
public class MiningTask extends AbstractMintingTask {
  private final BigInteger difficulty;

  /**
   * Instantiates a new Mining task.
   *
   * @param minter     the minter
   * @param interval   the interval
   * @param difficulty the difficulty
   */
  //TODO how is the difficulty expressed and used here?
  public MiningTask(Node minter, long interval, BigInteger difficulty) {
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
    this.getMinter().receiveBlock(createdBlock);
  }
}
