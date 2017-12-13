package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Sha256Hash;

import java.util.HashMap;
import java.util.Map;

public class BlockChainTracker {

    private Map<Sha256Hash, Sha256Hash> previousBlockMap = new HashMap<>();
    private Sha256Hash firstBlockHash;


    public void add(Block block) {
        previousBlockMap.put(block.getPrevBlockHash(), block.getHash());
        if (firstBlockHash == null) {
            firstBlockHash = block.getHash();
        }
    }

    public Map<Sha256Hash, Long> getBlockNoMap() {
        Map<Sha256Hash, Long> blockNoMap = new HashMap<>();
        long blockCounter = 0;
        Sha256Hash currentBlockHash = firstBlockHash;
        do {
            blockNoMap.put(currentBlockHash, blockCounter);
            currentBlockHash = previousBlockMap.get(currentBlockHash);
            blockCounter++;
        }
        while (currentBlockHash != null);

        return blockNoMap;
    }
}
