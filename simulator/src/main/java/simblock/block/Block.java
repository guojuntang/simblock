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

package simblock.block;

import simblock.node.Node;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * The representation of a block.
 */
public class Block {
  /**
   * The current height of the block.
   */
  private final int height;

  /**
   * The parent {@link Block}.
   */
  private final Block parent;

  /**
   * root Hash of merkle tree
   */
  private String rootHash;

  /**
   * current hash: SHA256(parentBlock.hash + minter.id + rootHash + time + id)
   */
  private String currentHash;

  /**
   * parent's hash
   */
  private String previousHash;

  /**
   * Transactions' list, stored as json string
   * (block body)
   */
  private List<Transaction> txnList;

  /**
   * The {@link Node} that minted the block.
   */
  private final Node minter;

  /**
   * Minting timestamp, absolute time since the beginning of the simulation.
   */
  private final long time;

  /**
   * Block unique id.
   */
  private final int id;

  /**
   * Latest known block id.
   */
  private static int latestId = 0;

  /**
   * Instantiates a new Block.
   *
   * @param parent
   * @param minter
   * @param time
   * @param txnlist
   */
  public Block(Block parent, Node minter, long time, List<Transaction> txnlist) {
    this.height = parent == null ? 0 : parent.getHeight() + 1;
    this.parent = parent;
    this.minter = minter;
    this.txnList = txnlist;
    this.rootHash = this.calRootHash();
    this.previousHash = (this.parent == null) ? "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f":this.parent.toString();
    this.time = time;
    this.id = latestId;
    latestId++;

    this.currentHash = this.calCurHash();
  }


  /**
   * Instantiates a new Block.
   * todo: remove latter
   *
   * @param parent the parent
   * @param minter the minter
   * @param time   the time
   */
  public Block(Block parent, Node minter, long time) {
    this.height = parent == null ? 0 : parent.getHeight() + 1;
    this.parent = parent;
    this.minter = minter;
    this.time = time;
    this.id = latestId;
    latestId++;
  }

  /**
   * Get height int.
   *
   * @return the int
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Get parent block.
   *
   * @return the block
   */
  public Block getParent() {
    return this.parent;
  }

  /**
   * get current hash
   * @return current hash
   */
  public String getCurrentHash(){
    return this.currentHash;
  }

  /**
   *  get previous hash
   * @return previous hash
   */
  public String getPreviousHash(){
    return  this.previousHash;
  }

  /**
   *  get txn list
   * @return txn list
   */
  public List<Transaction> getTxnList(){
    return  this.txnList;
  }


  /**
   * Get minter node.
   *
   * @return the node
   */
  @SuppressWarnings("unused")
  public Node getMinter() {
    return this.minter;
  }

  /**
   * Get time.
   *
   * @return the time
   */
  //TODO what format
  public long getTime() {
    return this.time;
  }

  /**
   * Gets the block id.
   *
   * @return the id
   */
  //TODO what format
  public int getId() {
    return this.id;
  }

  /**
   * Generates the genesis block. The parent is set to null and the time is set to 0
   *
   * @param minter the minter
   * @return the block
   */
  @SuppressWarnings("unused")
  public static Block genesisBlock(Node minter, List<Transaction> txnList) {
    return new Block(null, minter, 0, txnList);
  }

  /**
   * Recursively searches for the block at the provided height.
   *
   * @param height the height
   * @return the block with the provided height
   */
  public Block getBlockWithHeight(int height) {
    if (this.height == height) {
      return this;
    } else {
      return this.parent.getBlockWithHeight(height);
    }
  }

  /**
   * update the hash in block
   * (after adding new transactions to block)
   */
  private void updateHash(){
      this.rootHash = this.calRootHash();
      this.currentHash = this.calCurHash();
  }

  /**
   * add new transactions to block and update the hash value
   * @param list
   */
  public void appendTxn(List<Transaction> list){
    for (Transaction s: list) {
        this.txnList.add(s);
    }
    this.updateHash();
  }

  /**
   * calculate root hash
   * @return root hash
   */
  private String calRootHash(){

    ArrayList<String> new_list = new ArrayList<String>();
    for (Transaction s: this.getTxnList()) {
        new_list.add(s.toString());
    }
    MerkleTree m = new MerkleTree(new_list);
    m.merkle_tree();
    return m.getRoot();
  }

  /**
   *
   * current hash: SHA256(parentBloc.hash + minter.id + rootHash + time + id)
   *
   * @return current hash
   */

  private String calCurHash(){
    // String s = this.previousHash + this.minter.toString() + this.rootHash + this.time + this.id;
     String s = this.previousHash + ((this.minter == null)? "0" : this.minter.toString()) + this.rootHash + this.time + this.id;
    byte[] cipher_byte;

    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(s.getBytes());
      cipher_byte = md.digest();
      StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
      for (byte b : cipher_byte) {
        sb.append(String.format("%02x", b & 0xff));
      }
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";

  }

  @Override
  public String toString() {
    return this.currentHash;
  }

  /**
   *
   * @return root hash
   */
  public String getRootHash(){
      return this.rootHash;
  }

  /**
   * Checks if the provided block is on the same chain as self.
   *
   * @param block the block to be checked
   * @return true if block are on the same chain false otherwise
   */
  public boolean isOnSameChainAs(Block block) {
    if (block == null) {
      return false;
    } else if (this.height <= block.height) {
      return this.equals(block.getBlockWithHeight(this.height));
    } else {
      return this.getBlockWithHeight(block.height).equals(block);
    }
  }
}
