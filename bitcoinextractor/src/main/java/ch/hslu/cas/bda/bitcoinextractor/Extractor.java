package ch.hslu.cas.bda.bitcoinextractor;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Extractor {


    public static void main(String[] args) {
        // Configure Blockchain File Loader
        NetworkParameters networkParameters = new MainNetParams();
        List<File> blockChainFiles = new ArrayList<>();
        blockChainFiles.add(new File("D:\\docker-share\\bitcoin\\blocks\\blk00000.dat"));
        BlockFileLoader blockFileLoader = new BlockFileLoader(networkParameters, blockChainFiles);
        Context context = Context.getOrCreate(networkParameters);

        Map<String, Integer> monthlyTransactionCount = new HashMap<>();
        Map<String, Integer> monthlyBlockCount = new HashMap<>();
        Map<String, Integer> monthlyBlockSize = new HashMap<>();

        for (Block block : blockFileLoader) {

            LocalDate localDate = block.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String month = localDate.format(DateTimeFormatter.ofPattern("yyyy.MM"));

            if (!monthlyBlockCount.containsKey(month)) {
                monthlyBlockCount.put(month, 0);
                monthlyTransactionCount.put(month, 0);
                monthlyBlockSize.put(month, 0);
            }

            monthlyBlockCount.put(month, 1 + monthlyBlockCount.get(month));
            monthlyTransactionCount.put(month, block.getTransactions().size() + monthlyTransactionCount.get(month));
            monthlyBlockSize.put(month, block.getMessageSize() + monthlyBlockSize.get(month));

        }


        for (String month : monthlyBlockSize.keySet()) {
            float avgTx = (float) monthlyTransactionCount.get(month) / monthlyBlockSize.get(month);
            System.out.println(month + " => " + avgTx);
        }
    }
}
