package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockChainProcessorExecutor {

    private final IBlockProcessor processor;
    private final Map<Sha256Hash, Block> blockMap = new HashMap<>();

    public BlockChainProcessorExecutor(IBlockProcessor proccessor) {
        this.processor = proccessor;
    }


    public void processBlocks(List<File> files) {
        if (files.size() < 1) {
            throw new IllegalArgumentException("List of files is empty");
        }

        System.out.println("Start" + new Date().toString());
        Instant startTime = Instant.now();


        System.out.println("Reading blocks from " + files.get(0).getName() + " to " + files.get(files.size() - 1).getName());

        BlockTrackerFileLoader blockFileLoader =
                new BlockTrackerFileLoader(new BlockFileLoader(MainNetParams.get(), files));

        Context.getOrCreate(MainNetParams.get());

        long blockCount = 0;
        long txCount = 0;
        Block lastBlock = null;
        try {
            long procStartTime = System.currentTimeMillis();

            processor.onStart();

            for (Block block : blockFileLoader) {
                blockCount++;

                txCount += block.getTransactions().size();

                try {
                    processor.process(blockCount - 1, block);
                } catch (Exception e) {
                    System.err.println("Error processing block " + blockCount);
                    System.err.println("Block Hash: " + block.getHash().toString());
                    e.printStackTrace();
                }

                if (blockCount % 10000 == 0) {
                    long procTimeInMs = System.currentTimeMillis() - procStartTime;
                    System.out.print(String.format("Block %6d", blockCount));
                    System.out.print(String.format(" - TxCount: %7d", txCount));
                    System.out.print(String.format(" - Tx/Time: %6.0f Tx/s", (txCount / procTimeInMs * 1000.0)));
                    System.out.print(String.format(" - Processing time: %3.3fs", (procTimeInMs / 1000.0)));
                    System.out.print(String.format(" - Date: %tF", block.getTime()));
                    System.out.println();

                    procStartTime = System.currentTimeMillis();
                    txCount = 0;
                }

                /*
                if (lastBlock != null && !lastBlock.getHash().equals(block.getPrevBlockHash())) {
                    System.out.println(String.format("Block processed does not reference previous block: No: %6d - Date: %tF %tT - Hash: %s", blockCount - 1, lastBlock.getTime(), lastBlock.getTime(), lastBlock.getHashAsString()));
                }
                */

                lastBlock = block;
            }

            Map<Sha256Hash, Long> blockNoMap = blockFileLoader.getBlockNoMap();
            System.out.println("Block No Map size: " + blockNoMap.size());
            System.out.println(String.format("Last block processed: No: %6d - Date: %tF %tT - Hash: %s", blockCount - 1, lastBlock.getTime(), lastBlock.getTime(), lastBlock.getHashAsString()));
            System.out.println(String.format("Total blocks processed: %6d", blockCount));

            processor.onEnd();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Duration duration = Duration.between(startTime, Instant.now());

        System.out.println("Blocks from " + files.get(0).getName() + " to " + files.get(files.size() - 1).getName());
        System.out.println("Total execution time: " + duration.getSeconds() + "s");
    }

}
