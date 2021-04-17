package simblock.block;

import java.util.List;
import java.util.Map;

public class SpendableOutputResult {
    private int acc;

    private Map<String, List<Integer>> unspentOuts;

    public int getAcc() {
        return acc;
    }

    public Map<String, List<Integer>> getUnspentOuts() {
        return unspentOuts;
    }

    public SpendableOutputResult(int acc, Map<String, List<Integer>> unspentOuts) {
        this.acc = acc;
        this.unspentOuts = unspentOuts;
    }
}
