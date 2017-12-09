package ch.hslu.cas.bda.aggregation.bitcoin;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.influxdb.dto.Point;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import ch.hslu.cas.bda.InfluxConnector;
import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

@Deprecated
public class KafkaBlockToInfluxConsumer {

    private final KafkaConsumer<String, AvBlock> consumer;
    InfluxConnector influxConnector;

    public static void main(String[] args) {

        KafkaBlockToInfluxConsumer consumer = new KafkaBlockToInfluxConsumer();
        consumer.start();

    }

    public KafkaBlockToInfluxConsumer() {
        Properties settings = new Properties();
        // settings.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "z6vm1.westeurope.cloudapp.azure.com:9092");
        settings.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        settings.put(ConsumerConfig.CLIENT_ID_CONFIG, "BitcoinBlockConsumer");
        settings.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 104857600);
        settings.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);


        settings.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        settings.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        settings.put("schema.registry.url", "http://docker:8081");

        // Use Specific Record or else you get Avro GenericRecord.
        settings.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        consumer = new KafkaConsumer<>(settings);

        influxConnector = new InfluxConnector("docker", 8086);
        influxConnector.createDB("bitcoin");
    }

    public void start() {

        consumer.subscribe(Arrays.asList("bitcoin.block"));

        while (true) {
            ConsumerRecords<String, AvBlock> records = consumer.poll(100L);
            for (ConsumerRecord<String, AvBlock> record : records) {
                try {

                    AvBlock block = record.value();
                    if (block.getBlockNo() % 10000 == 0) {
                        System.out.println(String.format("Record offset: %6d  Block: %6d  Hash: %s", record.offset(), block.getBlockNo(), block.getBlockHash()));
                    }

                    consume(block);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void consume(AvBlock block) {

        long txAmount = block.getTransactions().stream().flatMap(t -> t.getVout().stream()).mapToLong(o -> o.getValue()).sum();

        long difficulty = block.getDifficultyTarget();
        Point point1 = Point
                .measurement("BitcoinBlocks")
                .time(block.getTime(), TimeUnit.MILLISECONDS)
                .addField("difficulty", difficulty)
                .addField("txSize", block.getTransactions().size())
                .addField("txAmount", txAmount)
                .build();


        influxConnector.write(point1);
    }
}