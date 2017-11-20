package ch.hslu.cas.bda.aggregation.bitcoin;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class TransactionConsumer {

    public static void main(final String[] args) throws Exception {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.consumer.transaction");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Create an instance of StreamsConfig from the Properties instance
        StreamsConfig config = new StreamsConfig(settings);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Long> bitcoinblockStream = builder.stream("bitcoinblock");


        bitcoinblockStream
                .groupBy((key, value) -> key)
                .count(Materialized.as("value-store"))
                .toStream().to("value-stream", Produced.with(Serdes.String(), Serdes.Long()));

        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, config);

        // Always clean local state prior to starting the processing topology. Make it easier
        // for playing around.
        streams.cleanUp();
        streams.start();
        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }


}
