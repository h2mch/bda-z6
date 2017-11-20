package ch.hslu.cas.bda.client.bitcoin;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class HelloKTable {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "docker:9092");
        props.put("group.id", "consumer-tutorial");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

    }
}
