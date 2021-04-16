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

import simblock.block.Block;
import simblock.block.Transaction;
import simblock.node.Node;

import static simblock.simulator.Main.OUT_JSON_FILE;
import static simblock.simulator.Timer.getCurrentTime;

/**
 * The type GetBlockTxn message task.
 */
// Compact block relay protocol Wiki: https://github.com/bitcoin/bips/blob/master/bip-0152.mediawiki
public class GetBlockTxnMessageTask extends AbstractMessageTask {
	/**
     * The {@link Block} that is sent by from as compact block.
     */
	private Block block;

	private Transaction transaction;

	public GetBlockTxnMessageTask(Node from, Node to, Transaction transaction, Block block) {
		super(from, to);
		this.transaction = transaction;
		this.block = block;
	}
    
    /**
     * Get block.
     *
     * @return the block
     */
	public Block getBlock(){
		return this.block;
	}

	public Transaction getTransaction(){
		return this.transaction;
	}

	@Override
	public void run() {

		super.run();
	}
}