package ch.hslu.cas.bda.ingestion;

import ch.hslu.cas.bda.ingestion.bitcoin.KafkaAvroBlockProducer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CSVInfluxDB {


    private static Logger logger = LoggerFactory.getLogger(KafkaAvroBlockProducer.class);

    private static final String FILE_NAME = "exch_rates_pred.csv";


    public static void main(String[] args) throws IOException {

        logger.info("Reading file");
        List<ExchangeRateCSV> parse;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String rate_cvs_file = classLoader.getResource(FILE_NAME).getPath();

        try (Reader reader = Files.newBufferedReader(Paths.get(rate_cvs_file))) {


            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(ExchangeRateCSV.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build();

            parse = csvToBean.parse();
            logger.info("{} parsed", parse.size());

        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        InfluxDB influxDB = InfluxDBFactory.connect("http://docker:8086", "root", "root");
        Pong response = influxDB.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            logger.error("Error pinging server.");
            return;
        }

        String dbName = "exchange";
        influxDB.createDatabase(dbName);
        influxDB.setDatabase(dbName);

        for (ExchangeRateCSV exchangeRateCSV : parse) {

            LocalDate date = LocalDate.parse(exchangeRateCSV.getDate_nr(), formatter);
            long unixTimeInSeconds = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();


            influxDB.write(Point.measurement("exchange")
                    .time(unixTimeInSeconds, TimeUnit.SECONDS)
                    .addField("gold", new BigDecimal(exchangeRateCSV.getXau()).movePointRight(4))
                    .addField("silber", new BigDecimal(exchangeRateCSV.getXag()).movePointRight(4))
                    .addField("platin", new BigDecimal(exchangeRateCSV.getXpt()).movePointRight(4))
                    .addField("eur", new BigDecimal(exchangeRateCSV.getEur()).movePointRight(4))
                    .addField("cad", new BigDecimal(exchangeRateCSV.getCad()).movePointRight(4))
                    .addField("jpy", new BigDecimal(exchangeRateCSV.getJpy()).movePointRight(4))
                    .addField("gbp", new BigDecimal(exchangeRateCSV.getGbp()).movePointRight(4))
                    .addField("chf", new BigDecimal(exchangeRateCSV.getChf()).movePointRight(4))
                    .addField("aud", new BigDecimal(exchangeRateCSV.getAud()).movePointRight(4))
                    .addField("hkd", new BigDecimal(exchangeRateCSV.getHkd()).movePointRight(4))
                    .addField("nzd", new BigDecimal(exchangeRateCSV.getNzd()).movePointRight(4))
                    .addField("krw", new BigDecimal(exchangeRateCSV.getKrw()).movePointRight(4))
                    .addField("mxn", new BigDecimal(exchangeRateCSV.getMxn()).movePointRight(4))
                    .addField("btc_close", new BigDecimal(exchangeRateCSV.getBtc_close()).movePointRight(4))
                    .addField("btc_volume", new BigDecimal(exchangeRateCSV.getBtc_volume()).movePointRight(4))
                    .addField("eth_close", new BigDecimal(exchangeRateCSV.getEth_close()).movePointRight(4))
                    .addField("eth_volume", new BigDecimal(exchangeRateCSV.getEth_volume()).movePointRight(4))
                    .addField("ltc_close", new BigDecimal(exchangeRateCSV.getLtc_close()).movePointRight(4))
                    .addField("ltc_volume", new BigDecimal(exchangeRateCSV.getLtc_volume()).movePointRight(4))
                    .addField("event_CHN", new BigDecimal(exchangeRateCSV.getCHN()).movePointRight(4))
                    .addField("event_RUS", new BigDecimal(exchangeRateCSV.getRUS()).movePointRight(4))
                    .addField("event_KOR", new BigDecimal(exchangeRateCSV.getKOR()).movePointRight(4))
                    .addField("event_NGA", new BigDecimal(exchangeRateCSV.getNGA()).movePointRight(4))
                    .addField("event_AF", new BigDecimal(exchangeRateCSV.getAF()).movePointRight(4))
                    .addField("event_AN", new BigDecimal(exchangeRateCSV.getAN()).movePointRight(4))
                    .addField("event_AS", new BigDecimal(exchangeRateCSV.getAS()).movePointRight(4))
                    .addField("event_EU", new BigDecimal(exchangeRateCSV.getEU()).movePointRight(4))
                    .addField("event_NA", new BigDecimal(exchangeRateCSV.getNA()).movePointRight(4))
                    .addField("event_OC", new BigDecimal(exchangeRateCSV.getOC()).movePointRight(4))
                    .addField("event_SA", new BigDecimal(exchangeRateCSV.getSA()).movePointRight(4))
                    .build());
        }
        influxDB.close();

    }
}