package ch.hslu.cas.bda.ingestion.bitcoin;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.StreamSupport;


@Deprecated
public class BlockTransactionExtractor {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BlockTransactionExtractor.class);

    //private static final String BITCOIN_BLOCKS = "D:\\docker-share\\bitcoin\\blocks\\";
    private static final String BITCOIN_BLOCKS = "src\\main\\resources";

    public static void main(String[] args) {
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

        try {
            Properties settings = new Properties();
//            settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.producer");
            settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");

            Producer<Long, Integer> prodTimeToTxAmount = new KafkaProducer<>(settings, new LongSerializer(), new IntegerSerializer());
            Producer<String, Long> prodTxTime = new KafkaProducer<>(settings, new StringSerializer(), new LongSerializer());
            Producer<String, Long> prodTxLockTime = new KafkaProducer<>(settings, new StringSerializer(), new LongSerializer());
            Producer<String, Long> prodTxFee = new KafkaProducer<>(settings, new StringSerializer(), new LongSerializer());
            Producer<String, String> prodTxMemo = new KafkaProducer<>(settings, new StringSerializer(), new StringSerializer());
            Producer<String, Long> prodTxInputSum = new KafkaProducer<>(settings, new StringSerializer(), new LongSerializer());

            for (Block block : blockFileLoader) {

                prodTimeToTxAmount.send(new ProducerRecord<>("bitcoin.TimeToTxAmount", block.getTimeSeconds(), block.getTransactions().size()));

                for (Transaction transaction : block.getTransactions()) {
                    prodTxTime.send(new ProducerRecord<>("bitcoin.TxToTime", transaction.getHashAsString(), transaction.getUpdateTime().getTime()));
                    prodTxLockTime.send(new ProducerRecord<>("bitcoin.TxToLockTime", transaction.getHashAsString(), transaction.getLockTime()));
                    prodTxFee.send(new ProducerRecord<>("bitcoin.TxToFee", transaction.getHashAsString(), transaction.getFee() != null ? transaction.getFee().value : 0));
                    prodTxMemo.send(new ProducerRecord<>("bitcoin.TxToMemo", transaction.getHashAsString(), transaction.getMemo()));
                    prodTxInputSum.send(new ProducerRecord<>("bitcoin.TxToInputSum", transaction.getHashAsString(), transaction.getInputSum() != null ? transaction.getInputSum().value : 0));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
