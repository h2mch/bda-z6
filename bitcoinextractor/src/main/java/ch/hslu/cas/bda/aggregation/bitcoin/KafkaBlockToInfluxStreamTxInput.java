package ch.hslu.cas.bda.aggregation.bitcoin;

import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import ch.hslu.cas.bda.message.bitcoin.AvTransaction;
import ch.hslu.cas.bda.message.bitcoin.AvTxInput;
import ch.hslu.cas.bda.message.bitcoin.Input;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaBlockToInfluxStreamTxInput {


    private static Logger logger = LoggerFactory.getLogger(KafkaBlockToInfluxStreamTxInput.class);

    private final KStream<String, AvBlock> bitcoinblockStream;

    private final KafkaStreams streams;

    public static void main(String[] args) {

        KafkaBlockToInfluxStreamTxInput consumer = new KafkaBlockToInfluxStreamTxInput();
        consumer.start();

    }

    public KafkaBlockToInfluxStreamTxInput() {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.consumer.block.BitcoinBlockConsumerTxInput");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(StreamsConfig.CLIENT_ID_CONFIG, "BitcoinBlockConsumerTxInput");

        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        settings.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://docker:8081");

        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create an instance of StreamsConfig from the Properties instance
        StreamsConfig config = new StreamsConfig(settings);

        StreamsBuilder builder = new StreamsBuilder();
        bitcoinblockStream = builder.stream("bitcoin.block");

        bitcoinblockStream.flatMapValues(block -> toTxInputs(block)).to("bitcoin.influx.tx.input");

        Topology topology = builder.build();
        streams = new KafkaStreams(topology, config);
    }

    private List<AvTxInput> toTxInputs(AvBlock block) {
        if (block.getBlockNo() % 10000 == 0) {
            logger.info("Block: {} / {}", block.getBlockNo(), block.getBlockHash());
        }
        List<AvTxInput> inputList = new ArrayList<>();
        if (block.getTransactions() == null) {
            return new ArrayList<>(0);
        }
        for (AvTransaction avTransaction : block.getTransactions()) {
            if (avTransaction.getVin() == null) {
                //coinbase tx (rewarded)
                continue;
            }
            for (Input input : avTransaction.getVin()) {
                AvTxInput avInput = new AvTxInput();
                avInput.setTime(block.getTime());
                avInput.setTxid(input.getTxid());
                avInput.setSequence(input.getSequence());
                avInput.setVout(input.getVout());

                inputList.add(avInput);
            }
        }
        return inputList;


    }

    public void start() {

        // Always clean local state prior to starting the processing topology. Make it easier
        // for playing around.
        streams.cleanUp();
        streams.start();
        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

}