## Extended features
- UTXO model
- Merkle Tree of transactions 
- Hash integrity of block by SHA256
- Data persistence in database(MongoDB)
- API for retrieve the data

## How to build

Before building this project, please configure MongoDB correctly

See [User guide](./en/usage.md)

## Query data

After running the simulator, it will output the collection name of the data

Copy and paste to [API Server](../simulator/src/main/java/simblock/server/Server.java)

API List:

|API | Description |
|----|-----|
|/blocks	|Return all blocks data|
|/blocks/:id	|Return the block with :id|
|/orphans	|Return the orphan block in simulator|
|/chain	Return| the blocks in canonical chain|
|/transactions|	Return all the transactions in blocks|
|/transactions/:id|	Return the transaction in the block with :id|
|/information	|Return information of blockchain simulator|
