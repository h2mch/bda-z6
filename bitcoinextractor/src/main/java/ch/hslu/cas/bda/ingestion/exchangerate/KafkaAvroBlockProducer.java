package ch.hslu.cas.bda.ingestion.exchangerate;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.streams.StreamsConfig;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.stream.StreamSupport;

import ch.hslu.cas.bda.ingestion.AvroProcessor;
import ch.hslu.cas.bda.ingestion.ElementExecutor;
import ch.hslu.cas.bda.message.bitcoin.AvExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.hslu.cas.bda.message.avro.AvroConverter.toExchangeRate;

/**
 * Delete this topic:
 * $ docker exec -it kafka kafka-topics --zookeeper zookeeper:2181 --delete --topic bitcoin.exchange
 */
public class KafkaAvroBlockProducer implements AvroProcessor<CoinbaseExchangeRate> {

    private static Logger logger = LoggerFactory.getLogger( KafkaAvroBlockProducer.class );


    private static final String RATE_CVS_FILE = "/media/heinz/Elements/docker-share/exchange/splitted";

    private Producer<String, AvExchangeRate> exchangeProducer;

    public static void main(String[] args) throws IOException {

        Instant startTime = Instant.now();
        logger.info("Start {}", startTime);

        List<File> exchangeFiles = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(RATE_CVS_FILE))) {

            StreamSupport.stream(directoryStream.spliterator(), false)
                    .filter(Files::isRegularFile)
                    .forEach(path -> exchangeFiles.add(path.toFile()));
        }


        int totalFiles = exchangeFiles.size();
        logger.info("Processing {} files...", totalFiles);
        int count = 1;
        long totalRecords = 0;


        KafkaAvroBlockProducer kafkaAvroBlockProducer = new KafkaAvroBlockProducer();
        ElementExecutor executor = new ElementExecutor(kafkaAvroBlockProducer);

        for (File file : exchangeFiles) {
            try (Reader reader = Files.newBufferedReader(file.toPath())) {


                CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                        .withType(CoinbaseExchangeRate.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                List<CoinbaseExchangeRate> exchangeRates = csvToBean.parse();

                executor.process(exchangeRates);

                totalRecords+= exchangeRates.size();
                logger.info("\t{} of {} ({} records)", count++, totalFiles, totalRecords);
            }
        }


        Duration duration = Duration.between(startTime, Instant.now());
        logger.info("Total execution time: {} min {} sec", duration.getSeconds());
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
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.exchangerate");
        //settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "z6vm1.westeurope.cloudapp.azure.com:9092");
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
    public Future<RecordMetadata> process(long number, CoinbaseExchangeRate coinbase) {
        AvExchangeRate avExchangeRate = toExchangeRate(coinbase);
        ProducerRecord<String, AvExchangeRate> record = new ProducerRecord<>("bitcoin.exchange", avExchangeRate);
        return exchangeProducer.send(record);
    }
}
