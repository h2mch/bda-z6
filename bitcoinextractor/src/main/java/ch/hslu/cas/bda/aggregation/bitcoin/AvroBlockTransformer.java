package ch.hslu.cas.bda.aggregation.bitcoin;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.UUID;

import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

public class AvroBlockTransformer {

    private final KStream<String, AvBlock> bitcoinBlockStream;
    private final StreamsConfig config;
    private final Topology topology;

    public static void main(String[] args) {
        AvroBlockTransformer consumer = new AvroBlockTransformer();
        consumer.start();
    }

    public AvroBlockTransformer() {
        Properties settings = new Properties();
        settings.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        settings.put(ConsumerConfig.CLIENT_ID_CONFIG, "BitcoinBlockConsumer");
        settings.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 104857600);
        settings.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        settings.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        settings.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        settings.put("schema.registry.url", "http://docker:8081");
        // Use Specific Record or else you get Avro GenericRecord.
        settings.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        // Create an instance of StreamsConfig from the Properties instance
        config = new StreamsConfig(settings);
        StreamsBuilder builder = new StreamsBuilder();
        bitcoinBlockStream = builder.stream("bitcoin.block");
        topology = builder.build();
    }

    public void configure(){

        //bitcoinBlockStream.to("influx-topic").


    }


    public void start() {
        KafkaStreams streams = new KafkaStreams(topology, config);

        // Always clean local state prior to starting the processing topology. Make it easier
        // for playing around.
        streams.cleanUp();
        streams.start();
        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

}
