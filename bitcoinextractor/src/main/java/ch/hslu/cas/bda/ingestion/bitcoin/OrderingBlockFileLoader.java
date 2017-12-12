package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.utils.BlockFileLoader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OrderingBlockFileLoader implements Iterator<Block>, Iterable<Block> {

    private BlockFileLoader blockLoader;
    private int bufferSize = 2000;
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
            return initializeAndReturnFirst();
        }

        if (blockLoader.hasNext()) {
            Block block = blockLoader.next();
            previousBlockMap.put(block.getPrevBlockHash(), block);
        }

        Block nextBlock = previousBlockMap.get(lastBlock.getHash());
        if (nextBlock == null) {
            throw new IllegalStateException("Next call no " + nextCounter + " - previous block not found with hash: " + lastBlock.getPrevBlockHash());
        }
        previousBlockMap.remove(lastBlock.getPrevBlockHash());
        lastBlock = nextBlock;
        return nextBlock;
    }

    @Override
    public Iterator<Block> iterator() {
        return this;
    }

    private Block initializeAndReturnFirst() {
        lastBlock = blockLoader.next();
        int i = 0;
        while ((i < bufferSize) && blockLoader.hasNext()) {
            Block block = blockLoader.next();
            previousBlockMap.put(block.getPrevBlockHash(), block);
            i++;
        }
        return lastBlock;
    }
}
