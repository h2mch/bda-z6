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

public class BlockConsumer {

    public static void main(final String[] args) throws Exception {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.consumer.block");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Create an instance of StreamsConfig from the Properties instance
        StreamsConfig config = new StreamsConfig(settings);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> bitcoinblockStream = builder.stream("bitcoin.TimeToTxAmount");


        CassandraConnector client = new CassandraConnector("docker", 9042);
        client.createKeyspace("bitcoin");

        String query = "CREATE TABLE IF NOT EXISTS bitcoin.TimeToTxAmount (id uuid PRIMARY KEY, time text, amount text);";
        client.execute(query);


        bitcoinblockStream.foreach((time, amount) -> {

            String insertQuery = "INSERT INTO TimeToTxAmount " +
                    "(id, time, amount) VALUES " +
                    "(" + UUID.randomUUID().toString() + ", '" + time.toString() + "', '" + amount + "');";
            client.execute(insertQuery);
        });


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
