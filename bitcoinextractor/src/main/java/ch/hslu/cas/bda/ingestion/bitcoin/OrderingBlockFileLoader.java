package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.utils.BlockFileLoader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OrderingBlockFileLoader implements Iterator<Block>, Iterable<Block> {

    private BlockFileLoader blockLoader;
    private int bufferSize = 100000;
    private Map<Sha256Hash, Block> previousBlockMap = new HashMap<>();
    private Block lastBlock = null;
    private long nextCounter = 0;


    public OrderingBlockFileLoader(BlockFileLoader loader) {
        blockLoader = loader;
    }

    @Override
    public boolean hasNext() {
        return blockLoader.hasNext() || !previousBlockMap.isEmpty();
    }

    @Override
    public Block next() {
        nextCounter++;
        if (lastBlock == null) {
            lastBlock = blockLoader.next();
            return lastBlock;
        }

        Block nextBlock = previousBlockMap.get(lastBlock.getHash());
        if (nextBlock != null) {
            previousBlockMap.remove(lastBlock.getPrevBlockHash());
            lastBlock = nextBlock;
            return nextBlock;
        }

        while (blockLoader.hasNext() && previousBlockMap.size() <= bufferSize) {
            nextBlock = blockLoader.next();
            if (lastBlock.getHash().equals(nextBlock.getPrevBlockHash())) {
                lastBlock = nextBlock;
                return nextBlock;
            }

            previousBlockMap.put(nextBlock.getPrevBlockHash(), nextBlock);

        }
        throw new IllegalStateException("Next call no " + nextCounter + " - previous block not found with hash: " + lastBlock.getPrevBlockHash());
    }

    @Override
    public Iterator<Block> iterator() {
        return this;
    }
}
