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

        NetworkParameters networkParameters = new MainNetParams();
        BlockFileLoader blockFileLoader = new BlockFileLoader(networkParameters, blockChainFiles);
        Context.getOrCreate(networkParameters);

        Map<String, Integer> monthlyTransactionCount = new HashMap<>();
        Map<String, Integer> monthlyBlockCount = new HashMap<>();
        Map<String, Integer> monthlyBlockSize = new HashMap<>();

        Block lastBlock = null;
        try {
            for (Block block : blockFileLoader) {
                lastBlock = block;
                LocalDate localDate = block.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String date = localDate.format(DateTimeFormatter.ofPattern("yyyy.MM"));

                if (block.getTransactions() == null) {
                    System.err.println("Could not process Block " + block.toString());
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
            System.out.println("Error after following block");
            System.out.println(date);
            System.out.println(lastBlock.toString());
        }


        for (String month : monthlyBlockSize.keySet()) {
            float avgTx = (float) monthlyTransactionCount.get(month) / monthlyBlockSize.get(month);
            System.out.println(month + " => " + avgTx);
        }

        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);

        System.out.println("Blocks from " + blockChainFiles.get(0).getName() + " to " + blockChainFiles.get(blockChainFiles.size() - 1).getName());
        System.out.println("Executiontime: " + duration.getSeconds() + "sec");
    }
}
