package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.utils.BlockFileLoader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BlockTrackerFileLoader implements Iterator<Block>, Iterable<Block> {

    private BlockFileLoader blockLoader;
    private Map<Sha256Hash, Sha256Hash> previousBlockMap = new HashMap<>();
    private Sha256Hash firstBlockHash;
    private long nextCounter = 0;


    public BlockTrackerFileLoader(BlockFileLoader loader) {
        blockLoader = loader;
    }

    @Override
    public boolean hasNext() {
        return blockLoader.hasNext();
    }

    @Override
    public Block next() {

        nextCounter++;
        Block block = blockLoader.next();
        previousBlockMap.put(block.getPrevBlockHash(), block.getHash());
        if (firstBlockHash == null) {
            firstBlockHash = block.getHash();
        }
        return block;
    }

    @Override
    public Iterator<Block> iterator() {
        return this;
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
