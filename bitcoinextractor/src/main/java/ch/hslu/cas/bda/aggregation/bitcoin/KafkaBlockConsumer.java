package ch.hslu.cas.bda.aggregation.bitcoin;

import ch.hslu.cas.bda.message.avro.AvroDeserializer;
import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

public class KafkaBlockConsumer {

    private final KafkaConsumer<String, byte[]> consumer;

    public static void main(String[] args) {

        KafkaBlockConsumer consumer = new KafkaBlockConsumer();
        consumer.start();

    }

    public KafkaBlockConsumer() {
        //consumer properties
        Properties settings = new Properties();
        // settings.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "z6vm1.westeurope.cloudapp.azure.com:9092");
        settings.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        settings.put(ConsumerConfig.CLIENT_ID_CONFIG, "BitcoinBlockConsumer");
        settings.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 104857600);
        settings.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // settings.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);
        // settings.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 2000);
        // settings.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 3000);
        // settings.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);
        // settings.put("enable.auto.commit", "true");

        settings.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        settings.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        consumer = new KafkaConsumer<>(settings);
    }

    public void start() {

        consumer.subscribe(Arrays.asList("bitcoin.block"));

        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(100L);
            for (ConsumerRecord<String, byte[]> record : records) {
                try {

                    AvBlock block = new AvroDeserializer<AvBlock>().fromByteArray(record.value(), AvBlock.class);
                    long mined = block.getTransactions().get(0).getVout().stream().mapToLong(o -> o.getValue()).sum();
                    if (block.getBlockNo() % 10000 == 0) {
                        System.out.println(String.format("Record offset: %6d  Block: %6d  Mined+Fees: %10d  Hash: %s", record.offset(), block.getBlockNo(), mined, block.getBlockHash()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}