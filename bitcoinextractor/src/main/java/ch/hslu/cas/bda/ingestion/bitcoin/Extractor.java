package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
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
        long transactionCount = 0;
        int blockcount = 0;
        try {
            Instant last = Instant.now();
            for (Block block : blockFileLoader) {
                blockcount++;
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
                int txSize = block.getTransactions().size();
                for (Transaction transaction : block.getTransactions()) {
                    transaction.getMemo();
                    transactionCount++;
                    List<TransactionInput> inputs = transaction.getInputs();
                    List<TransactionOutput> outputs = transaction.getOutputs();
                }
                monthlyTransactionCount.put(date, txSize + monthlyTransactionCount.get(date));
                monthlyBlockSize.put(date, block.getMessageSize() + monthlyBlockSize.get(date));

                if (blockcount % 50000 == 0) {
                    Instant current = Instant.now();
                    logger.info("{} Blocks - {} Transaction processed in {} sec", blockcount, transactionCount, Duration.between(last, current).getSeconds());
                    last = current;
                }
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
        logger.info("{} Blocks - {} Transaction", blockcount, transactionCount);
        logger.info("Blocks from {} to {}", blockChainFiles.get(0).getName(), blockChainFiles.get(blockChainFiles.size() - 1).getName());
        logger.info("Execution: {} sec", duration.getSeconds());
    }
}
