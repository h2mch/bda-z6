package ch.hslu.cas.bda.aggregation.bitcoin;

import ch.hslu.cas.bda.message.bitcoin.*;
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

public class KafkaBlockToInfluxStreamTxOutput {


    private static Logger logger = LoggerFactory.getLogger(KafkaBlockToInfluxStreamTxOutput.class);

    private final KStream<String, AvBlock> bitcoinblockStream;

    private final KafkaStreams streams;

    public static void main(String[] args) {

        KafkaBlockToInfluxStreamTxOutput consumer = new KafkaBlockToInfluxStreamTxOutput();
        consumer.start();

    }

    public KafkaBlockToInfluxStreamTxOutput() {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.consumer.block.BitcoinBlockConsumerTxOutput");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(StreamsConfig.CLIENT_ID_CONFIG, "BitcoinBlockConsumerTxOutput");

        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        settings.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000); //Prevent TimeoutException: Expiring 284 record(s) for bitcoin.influx.tx.output-0: 30050 ms has passed since last append
        settings.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://docker:8081");

        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create an instance of StreamsConfig from the Properties instance
        StreamsConfig config = new StreamsConfig(settings);

        StreamsBuilder builder = new StreamsBuilder();
        bitcoinblockStream = builder.stream("bitcoin.block");

        bitcoinblockStream.flatMapValues(block -> toTxInputs(block)).to("bitcoin.influx.tx.output");

        Topology topology = builder.build();
        streams = new KafkaStreams(topology, config);
    }

    private List<AvTxOutput> toTxInputs(AvBlock block) {
        if (block.getBlockNo() % 10000 == 0) {
            logger.info("Block: {} / {}", block.getBlockNo(), block.getBlockHash());
        }
        List<AvTxOutput> outputList = new ArrayList<>();
        for (AvTransaction avTransaction : block.getTransactions()) {
            for (Output output : avTransaction.getVout()) {
                AvTxOutput avOutput = new AvTxOutput();
                avOutput.setTime(block.getTime());
                avOutput.setAddress(output.getAddress());
                avOutput.setValue(output.getValue());

                outputList.add(avOutput);
            }
        }
        return outputList;


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