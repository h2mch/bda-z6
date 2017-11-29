package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class BlockChainProcessorExecutor {

    private final IBlockProcessor processor;

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

        BlockFileLoader blockFileLoader = new BlockFileLoader(MainNetParams.get(), files);

        Context.getOrCreate(MainNetParams.get());

        long blockCount = 0;
        long txCount = 0;
        try {
            long procStartTime = System.currentTimeMillis();

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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Duration duration = Duration.between(startTime, Instant.now());

        System.out.println("Blocks from " + files.get(0).getName() + " to " + files.get(files.size() - 1).getName());
        System.out.println("Total Executiontime: " + duration.getSeconds() + "s");
    }

}
