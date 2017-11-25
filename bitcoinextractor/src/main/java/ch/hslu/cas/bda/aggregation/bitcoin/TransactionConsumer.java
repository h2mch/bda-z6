package ch.hslu.cas.bda.aggregation.bitcoin;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.UUID;

import ch.hslu.cas.bda.CassandraConnector;

public class TransactionConsumer {

    public static void main(final String[] args) throws Exception {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.consumer.transaction");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass());

        // Create an instance of StreamsConfig from the Properties instance
        StreamsConfig config = new StreamsConfig(settings);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Long> bitcoinTxToTimeStream = builder.stream("bitcoin.TxToTime");


        try (CassandraConnector client = new CassandraConnector("docker", 9042)) {
            client.createKeyspace("bitcoin");
            String query = "CREATE TABLE IF NOT EXISTS bitcoin.TxToTime (id uuid PRIMARY KEY, unixtimestamp bigint, hash varchar);";
            client.execute(query);

            bitcoinTxToTimeStream.foreach((hash, time) -> {
                String insertQuery = "INSERT INTO bitcoin.TxToTime " +
                        "(id, unixtimestamp, hash) VALUES " +
                        "(" + UUID.randomUUID().toString() + ", " + time + ", '" + hash + "');";
                client.execute(insertQuery);
            });
        }

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
