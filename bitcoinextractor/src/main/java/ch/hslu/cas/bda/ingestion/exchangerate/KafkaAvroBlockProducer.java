package ch.hslu.cas.bda.ingestion.exchangerate;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.streams.StreamsConfig;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

import ch.hslu.cas.bda.ingestion.AvroProcessor;
import ch.hslu.cas.bda.ingestion.ElementExecutor;
import ch.hslu.cas.bda.message.bitcoin.AvExchangeRate;

import static ch.hslu.cas.bda.message.avro.AvroConverter.toExchangeRate;

public class KafkaAvroBlockProducer implements AvroProcessor<CoinbaseExchangeRate> {


    private static final String RATE_CVS_FILE = "src\\main\\resources\\bitcoin-historical-data\\coinbase.big.csv";

    private Producer<String, AvExchangeRate> exchangeProducer;

    public static void main(String[] args) throws IOException {

        try (Reader reader = Files.newBufferedReader(Paths.get(RATE_CVS_FILE))) {

            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CoinbaseExchangeRate.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<CoinbaseExchangeRate> exchangeRates = csvToBean.parse();

/*
        List<CoinbaseExchangeRate> exchangeRates =
                new CsvToBeanBuilder(new FileReader(RATE_CVS_FILE))
                        .withType(CoinbaseExchangeRate.class)
                        .build()
                        .parse();
*/
            KafkaAvroBlockProducer kafkaAvroBlockProducer = new KafkaAvroBlockProducer();
            ElementExecutor executor = new ElementExecutor(kafkaAvroBlockProducer);
            executor.process(exchangeRates);
        }
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
