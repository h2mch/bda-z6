package ch.hslu.cas.bda.aggregation.bitcoin;

import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import ch.hslu.cas.bda.message.bitcoin.AvBlockDatapoint;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.bitcoinj.core.Utils;

import java.math.BigInteger;
import java.util.Properties;

public class KafkaBlockToInfluxStream {

    private static BigInteger MAX_DIFFICULTY_TARGET = Utils.decodeCompactBits(0x1d00ffffL);

    private final KStream<String, AvBlock> bitcoinblockStream;

    private final KafkaStreams streams;

    public static void main(String[] args) {

        KafkaBlockToInfluxStream consumer = new KafkaBlockToInfluxStream();
        consumer.start();

    }

    public KafkaBlockToInfluxStream() {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "bda-z6.bitcoin.consumer.block");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "docker:9092");
        settings.put(StreamsConfig.CLIENT_ID_CONFIG, "BitcoinBlockConsumer");

        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        settings.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://docker:8081");

        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create an instance of StreamsConfig from the Properties instance
        StreamsConfig config = new StreamsConfig(settings);

        StreamsBuilder builder = new StreamsBuilder();
        bitcoinblockStream = builder.stream("bitcoin.block");

        bitcoinblockStream.map((key, block) -> KeyValue.pair(key, toBlockDatapoint(block))).to("bitcoin.influx.block");

        Topology topology = builder.build();
        streams = new KafkaStreams(topology, config);
    }

    public void start() {

        // Always clean local state prior to starting the processing topology. Make it easier
        // for playing around.
        streams.cleanUp();
        streams.start();
        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private AvBlockDatapoint toBlockDatapoint(AvBlock block) {
        if (block.getBlockNo() % 10000 == 0) {
            System.out.println("Block: " + block.getBlockNo() + " / " + block.getBlockHash());
        }

        AvBlockDatapoint datapoint = new AvBlockDatapoint();
        datapoint.setBlockNo(block.getBlockNo());
        datapoint.setBlockHash(block.getBlockHash());
        datapoint.setPreviousBlockHash(block.getPreviousBlockHash());
        datapoint.setDifficulty(calculateDifficulty(block.getDifficultyTarget()));
        datapoint.setMined(calculcateMined(block.getBlockNo()));
        datapoint.setTime(block.getTime());
        datapoint.setTxCount(block.getTransactions().size());
        datapoint.setTxFees(calculateTxFees(block));
        datapoint.setTxVolume(calculateTxVolume(block));
        return datapoint;
    }

    private Long calculateTxFees(AvBlock block) {
        long miningReward = calculcateMined(block.getBlockNo());
        long firstTxSum = block.getTransactions().get(0).getVout().stream().mapToLong(o -> o.getValue()).sum();
        return firstTxSum - miningReward;
    }

    private Long calculateDifficulty(Long compactedDifficultyTarget) {
        // The difficulty is calculated by normalizing the difficulty target with the max difficulty value.
        // As a result the difficulty starts out with 1 for block 0 and increases as mining power is building up.
        return MAX_DIFFICULTY_TARGET.divide(Utils.decodeCompactBits(compactedDifficultyTarget)).longValue();

    }

    private Long calculcateMined(Long blockNo) {
        long era = blockNo / 210000;
        return (long) (5000000000.0 / Math.pow(2L, era));
    }

    private Long calculateTxVolume(AvBlock block) {
        return block.getTransactions().stream().flatMap(t -> t.getVout().stream()).mapToLong(o -> o.getValue()).sum();
    }
}