package ch.hslu.cas.bda.ingestion.bitcoin;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.StreamSupport;

public class BlockTransactionSizeExtractor {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BlockTransactionSizeExtractor.class);

    private static final String BITCOIN_BLOCKS = "D:\\docker-share\\bitcoin\\blocks\\";

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
            settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bitcoin-application");
            settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");

            Producer<String, String> producer = new KafkaProducer<>(settings, new StringSerializer(), new StringSerializer());

            for (Block block : blockFileLoader) {
                producer.send(new ProducerRecord<>("test", block.getHashAsString(), block.getTransactions().size() + ""));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
