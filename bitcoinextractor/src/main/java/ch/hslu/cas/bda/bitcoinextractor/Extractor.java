package ch.hslu.cas.bda.bitcoinextractor;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class Extractor {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Extractor.class);

    private static final String BITCOIN_BLOCKS = "D:\\docker-share\\bitcoin\\blocks\\";

    public static void main(String[] args) {
        Instant startTime = Instant.now();

        // Configure Blockchain File Loader
        List<File> blockChainFiles = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(BITCOIN_BLOCKS))) {

            StreamSupport.stream(directoryStream.spliterator(), false)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith("dat"))
                    .filter(path -> path.getFileName().toString().startsWith("blk"))
                    .forEach(path -> blockChainFiles.add(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("'{}' Files found", blockChainFiles.size());

        NetworkParameters networkParameters = new MainNetParams();
        BlockFileLoader blockFileLoader = new BlockFileLoader(networkParameters, blockChainFiles);
        Context.getOrCreate(networkParameters);

        Map<String, Integer> monthlyTransactionCount = new HashMap<>();
        Map<String, Integer> monthlyBlockCount = new HashMap<>();
        Map<String, Integer> monthlyBlockSize = new HashMap<>();

        Block lastBlock = null;
        int blockcount = 0;
        try {
            Instant last = Instant.now();
            for (Block block : blockFileLoader) {
                blockcount++;
                if (blockcount % 100000 == 0) {
                    Instant current = Instant.now();
                    logger.info("'{}' Blocks processed in {} sec", blockcount, Duration.between(last, current).getSeconds());
                    last = current;
                }
                lastBlock = block;
                LocalDate localDate = block.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String date = localDate.format(DateTimeFormatter.ofPattern("yyyy.MM"));

                if (block.getTransactions() == null) {
                    logger.error("Could not process Blocknumber {} \n{}", blockcount, block.toString());
                    continue;
                }
                if (!monthlyBlockCount.containsKey(date)) {
                    monthlyBlockCount.put(date, 0);
                    monthlyTransactionCount.put(date, 0);
                    monthlyBlockSize.put(date, 0);
                }

                monthlyBlockCount.put(date, 1 + monthlyBlockCount.get(date));
                monthlyTransactionCount.put(date, block.getTransactions().size() + monthlyTransactionCount.get(date));
                monthlyBlockSize.put(date, block.getMessageSize() + monthlyBlockSize.get(date));
            }
        } catch (Exception ex) {

            LocalDate localDate = lastBlock.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String date = localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            logger.error("Error after {} and blocknumber '{}'", date, blockcount);
            logger.error(lastBlock.toString());
        }


        for (String month : monthlyBlockSize.keySet()) {
            float avgTx = (float) monthlyTransactionCount.get(month) / monthlyBlockSize.get(month);
            logger.info("{} => {}", month, avgTx);
        }

        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);

        logger.info("Blocks from {} to {}", blockChainFiles.get(0).getName(), blockChainFiles.get(blockChainFiles.size() - 1).getName());
        logger.info("Executiontime: {} sec", duration.getSeconds());
    }
}
