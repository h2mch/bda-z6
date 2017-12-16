package ch.hslu.cas.bda.aggregation.bitcoin;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

public class StreamToAvroDumper implements Runnable {

    private final KafkaConsumer<String, SpecificRecord> consumer;

    private Class avroClass;

    private File file;

    private String topic;

    public static void main(String[] args) throws ClassNotFoundException {

        if (args.length != 3) {
            System.out.println("Missing arguments");
            return;
        }
        String topic = args[0];
        String classname = args[1];
        String filename = args[2];

        System.out.println("Topic: " + topic);
        System.out.println("Classname: " + classname);
        System.out.println("Filename: " + filename);


        StreamToAvroDumper consumer = new StreamToAvroDumper(topic, classname, filename);
        Thread consumerThread = new Thread(consumer);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                consumerThread.interrupt();
                consumerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }));

        consumerThread.start();


    }

    public StreamToAvroDumper(String topic, String classname, String filename) throws ClassNotFoundException {
        this.topic = topic;
        avroClass = Class.forName(classname);
        this.file = new File(filename);

        Properties settings = new Properties();
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
    }

    @Override
    public void run() {

        System.out.println("Start consumer");
        consumer.subscribe(Arrays.asList(topic));
        DatumWriter datumWriter = new SpecificDatumWriter(avroClass);
        DataFileWriter dataFileWriter = new DataFileWriter(datumWriter);
        Schema schema = ReflectData.get().getSchema(avroClass);

        int count = 0;
        try {

            dataFileWriter.create(schema, file);

            while (true) {

                ConsumerRecords<String, SpecificRecord> records = consumer.poll(100L);
                for (ConsumerRecord<String, SpecificRecord> record : records) {
                    count++;
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    SpecificRecord specificRecord = record.value();
                    dataFileWriter.append(specificRecord);
                    if (count % 10000 == 0) {
                        System.out.println("Record: " + count);
                    }
                }
            }
        } catch (InterruptedException | org.apache.kafka.common.errors.InterruptException e) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dataFileWriter.close();
                System.out.println("File " + file + " closed");
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Consumer stopped");
        }
    }
}