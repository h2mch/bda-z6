package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BlockChainProcessorExecutor {


    private static Logger logger = LoggerFactory.getLogger(BlockChainProcessorExecutor.class);

    private final IBlockProcessor processor;

    public BlockChainProcessorExecutor(IBlockProcessor proccessor) {
        this.processor = proccessor;
    }


    public void processBlocks(List<File> files) {

        if (files.size() < 1) {
            throw new IllegalArgumentException("List of files is empty");
        }

        logger.info("Reading blocks from {} to {}", files.get(0).getName(), files.get(files.size() - 1).getName());
        logger.info("Step 1: Pre-processing");

        BlockFileLoader blockFileLoader = new BlockFileLoader(MainNetParams.get(), files);
        BlockChainTracker tracker = new BlockChainTracker();

        processBlocksInternal(blockFileLoader, block -> tracker.add(block));

        Map<Sha256Hash, Long> blockNoMap = tracker.getBlockNoMap();
        logger.info("Block No Map size: {}", blockNoMap.size());


        logger.info("Step 2: Processing");
        blockFileLoader = new BlockFileLoader(MainNetParams.get(), files);

        processor.onStart();
        processBlocksInternal(blockFileLoader, block -> {
            try {
                processor.process(blockNoMap.get(block.getHash()), block);
            } catch (IOException e) {
                logger.error("could not process block at time '{}' ", block.getTime(), e);
            }
        });
        processor.onEnd();
    }

    private void processBlocksInternal(Iterable<Block> blockFileLoader, Consumer<Block> consumer) {
        Instant startTime = Instant.now();
        logger.info("Start {}", startTime);

        Context.getOrCreate(MainNetParams.get());

        long blockCount = 0;
        long txCount = 0;
        Block lastBlock = null;
        try {
            long procStartTime = System.currentTimeMillis();


            for (Block block : blockFileLoader) {
                blockCount++;

                txCount += block.getTransactions().size();

                try {
                    consumer.accept(block);

                } catch (RuntimeException e) {
                    logger.error("Error processing block seq. no: " + blockCount);
                    logger.error("Block Hash: {}", block.getHash().toString(), e);
                }

                if (blockCount % 10000 == 0) {
                    long procTimeInMs = System.currentTimeMillis() - procStartTime;
                    logger.info("Block {}", blockCount);
                    logger.info("\t- TxCount: {}", txCount);
                    logger.info("\t- Tx/Time: {} Tx/s", (txCount / procTimeInMs * 1000.0));
                    logger.info("\t- Processing time: {}s", (procTimeInMs / 1000.0));
                    logger.info("\t- Date: {}", block.getTime());
                    logger.info("\t- Size: {}\n", block.getMessageSize());

                    procStartTime = System.currentTimeMillis();
                    txCount = 0;
                }


                lastBlock = block;
            }

            logger.info("Last block processed: No: {} - Date: {} - Hash: {}", blockCount - 1, lastBlock.getTime(), lastBlock.getHashAsString());
            logger.info("Total blocks processed: {}", blockCount);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Duration duration = Duration.between(startTime, Instant.now());

        logger.info("Total execution time: {}s", duration.getSeconds());
    }

}
