package ch.hslu.cas.bda.ingestion.exchangerate;

import ch.hslu.cas.bda.ingestion.AvroProcessor;
import ch.hslu.cas.bda.ingestion.ElementExecutor;
import ch.hslu.cas.bda.message.bitcoin.AvExchangeRateGDAX;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

import static ch.hslu.cas.bda.message.avro.AvExchangeRateConverter.toExchangeRate;

/**
 * Create this connector
 * By CURL / Postman
 * <p>
 * <code>curl -X POST  http://docker:8083/connectors \
 * -H 'Content-Type: application/json' \
 * -d '{
 * "name": "influxdb-sink-exchange.gdax",
 * "config": {
 * "connector.class": "com.datamountaineer.streamreactor.connect.influx.InfluxSinkConnector",
 * "tasks.max": "1",
 * "topics": "bitcoin.exchange.gdax",
 * "connect.influx.kcql": "INSERT INTO exchange SELECT * FROM bitcoin.exchange.gdax WITHTIMESTAMP date_nr",
 * "connect.influx.url": "http://influx:8086",
 * "connect.influx.db": "bitcoin",
 * "connect.influx.username": "root",
 * "connect.influx.password": "root"
 * }
 * }'</code>
 * <p>
 * Delete this topic:
 * $ docker exec -it kafka kafka-topics --zookeeper zookeeper:2181 --delete --topic bitcoin.exchange.gdax
 */
public class KafkaAvroBlockProducerGdax implements AvroProcessor<GdaxExchangeRate> {

    private static Logger logger = LoggerFactory.getLogger(KafkaAvroBlockProducerGdax.class);

    private static final String RATE_CVS_FILE = "/Users/had/projects/CASBigData/bda-z6/bitcoinextractor/src/main/resources/exchangerate/";

    private Producer<String, AvExchangeRateGDAX> exchangeProducer;

    public static void main(String[] args) throws IOException {

        Instant startTime = Instant.now();
        logger.info("Start {}", startTime);

        File exchangeDir = Paths.get(RATE_CVS_FILE).toFile();

        File[] files = exchangeDir.listFiles((dir, name) -> name.endsWith(".csv"));

        int totalFiles = files.length;
        logger.info("Processing {} files...", totalFiles);
        int count = 1;


        KafkaAvroBlockProducerGdax kafkaAvroBlockProducer = new KafkaAvroBlockProducerGdax();
        ElementExecutor executor = new ElementExecutor(kafkaAvroBlockProducer);
        List<GdaxExchangeRate> gdaxExchangeRates = new ArrayList<>();

        for (File file : files) {
            try (Reader reader = Files.newBufferedReader(file.toPath())) {


                CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                        .withType(GdaxExchangeRate.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSkipLines(1)
                        .build();

                List parse = csvToBean.parse();
                gdaxExchangeRates.addAll(parse);
                logger.info("\t{} of {} file parsed ({} records)", count++, totalFiles, parse.size());
            }
        }
        logger.info("{} files parsed", files.length);


        logger.info("sending {} records to kafka", gdaxExchangeRates.size());
        executor.process(gdaxExchangeRates);


        Duration duration = Duration.between(startTime, Instant.now());
        logger.info("Total execution time {}sec for {} records", duration.getSeconds(), gdaxExchangeRates.size());
    }

    @Override
    public void onEnd() {
        try {
            exchangeProducer.flush();
        } finally {
            exchangeProducer.close();
        }
    }

    @Override
    public void onStart() {
        Properties settings = new Properties();
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(ProducerConfig.ACKS_CONFIG, "0");
        settings.put(ProducerConfig.BATCH_SIZE_CONFIG, 100000);
        settings.put(ProducerConfig.CLIENT_ID_CONFIG, "ExchangeRateBlockProducer");

        settings.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        settings.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);

        settings.put("schema.registry.url", "http://docker:8081");

        settings.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 104857600);

        exchangeProducer = new KafkaProducer<>(settings);
    }


    @Override
    public Future<RecordMetadata> process(long number, GdaxExchangeRate gdaxExchangeRate) {
        AvExchangeRateGDAX avExchangeRateGDAX = toExchangeRate(gdaxExchangeRate);
        ProducerRecord<String, AvExchangeRateGDAX> record = new ProducerRecord<>("bitcoin.exchange.gdax", avExchangeRateGDAX);
        return exchangeProducer.send(record);
    }

}
