package ch.hslu.cas.bda.ingestion.bitcoin;

import ch.hslu.cas.bda.message.avro.BlockConverter;
import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.bitcoinj.core.Block;

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

public class KafkaAvroBlockProducer implements IBlockProcessor {


    // private static final String BITCOIN_BLOCKS = "src\\main\\resources";
    private static final String BITCOIN_BLOCKS = "/Users/had/Library/Application Support/Bitcoin/blocks/";
    private Producer<String, AvBlock> blockProducer;

    public static void main(String[] args) throws IOException {

        List<File> blockChainFiles = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(BITCOIN_BLOCKS))) {

            StreamSupport.stream(directoryStream.spliterator(), false)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith("dat"))
                    .filter(path -> path.getFileName().toString().startsWith("blk0"))
                    .forEach(path -> blockChainFiles.add(path.toFile()));
        }
        BlockChainProcessorExecutor processor = new BlockChainProcessorExecutor(new KafkaAvroBlockProducer());
        processor.processBlocks(blockChainFiles);
    }


    @Override
    public void onStart() {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.producer");
        //settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "z6vm1.westeurope.cloudapp.azure.com:9092");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(ProducerConfig.ACKS_CONFIG, "0");
        settings.put(ProducerConfig.BATCH_SIZE_CONFIG, 100000);
        settings.put(ProducerConfig.CLIENT_ID_CONFIG, "BitcoinBlockProducer");

        settings.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        settings.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);

        settings.put("schema.registry.url", "http://docker:8081");

        settings.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 104857600);

        blockProducer = new KafkaProducer<>(settings);
    }

    @Override
    public void process(long blockCount, Block block) throws IOException {

        try {
            AvBlock avBlock = new BlockConverter().toAvBlock(block, blockCount);
            ProducerRecord<String, AvBlock> record = new ProducerRecord<>("bitcoin.block", avBlock);
            blockProducer.send(record).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnd() {
        try {
            blockProducer.flush();
        } finally {
            blockProducer.close();
        }
    }
}
