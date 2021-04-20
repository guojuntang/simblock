package simblock.server;

import io.javalin.Javalin;
import simblock.simulator.MongoDBJDBC;
import static simblock.settings.SimulationConfiguration.*;

/**
 * api for access mongodb
 */
public class Server {
    // take place the collection_name with your own database
    private static final MongoDBJDBC client = new MongoDBJDBC(MONGODB_ADDRESS, MONGODB_PORT, "simblock_1618864795963" );

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.get("/", ctx -> ctx.result("Hello World"));

        /**
         * show all blocks
         */
        app.get("/blocks", ctx -> ctx.result(client.getAllBlocks().toString()));

        /**
         * show the orphan blocks
         */
        app.get("/orphans", ctx -> ctx.result(client.getOrphans().toString()));

        /**
         * show the canonical blockchain
         */
        app.get("/chain", ctx -> ctx.result(client.getBlocksOnChain().toString()));

        /**
         * show all transactions
         */
        app.get("/transactions", ctx -> ctx.result(client.getAllTransactions().toString()));

        /**
         * show the transaction in block_id
         */
        app.get("/transaction/:id", ctx -> ctx.result(client.getTransactionOnBlock(Integer.parseInt(ctx.pathParam("id")))));

        /**
         * show information of the simulator
         */
        app.get("/information", ctx -> ctx.result(client.getSimulatorInformation()));

        /**
         * show the block in block_id
         */
        app.get("/block/:id", ctx -> ctx.result(client.getOneBlock(Integer.parseInt(ctx.pathParam("id")))));

    }
}
