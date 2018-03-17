package ch.hslu.cas.bda.ingestion;

import ch.hslu.cas.bda.ingestion.bitcoin.KafkaAvroBlockProducer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.StringUtils;
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
        String dbName = "bitcoin";
        influxDB.setDatabase(dbName);

        for (ExchangeRateCSV exchangeRateCSV : parse) {

            LocalDate date = LocalDate.parse(exchangeRateCSV.getDate_nr(), formatter);
            long unixTimeInSeconds = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();

            try {
                // Tags are meta data information
                // Tagset ist the collection of all tags
                influxDB.write(Point.measurement("gold")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getXau()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getXau_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getXau_prc()).movePointRight(2))
                        .build());

                influxDB.write(Point.measurement("silber")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getXag()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getXag_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getXag_prc()).movePointRight(2))
                        .build());


                influxDB.write(Point.measurement("chf")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getChf()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getChf_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getChf_prc()).movePointRight(2))
                        .build());

                influxDB.write(Point.measurement("eur")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getEur()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getEur_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getEur_prc()).movePointRight(2))
                        .build());


                influxDB.write(Point.measurement("jpy")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getJpy()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getJpy_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getJpy_prc()).movePointRight(2))
                        .build());

                influxDB.write(Point.measurement("gbp")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getGbp()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getGbp_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getGbp_prc()).movePointRight(2))
                        .build());

                influxDB.write(Point.measurement("hkd")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getHkd()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getHkd_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getHkd_prc()).movePointRight(2))
                        .build());

                influxDB.write(Point.measurement("krw")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getKrw()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getKrw_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getKrw_prc()).movePointRight(2))
                        .build());


                influxDB.write(Point.measurement("mxn")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getMxn()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getMxn_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getMxn_prc()).movePointRight(2))
                        .build());

                influxDB.write(Point.measurement("btc")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getBtc_close()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getBtc_close_20_volty()))
                        .addField("prc", new BigDecimal(exchangeRateCSV.getBtc_close_prc_nextday()).movePointRight(2))
                        .addField("volume", new BigDecimal(exchangeRateCSV.getBtc_volume()).movePointRight(4))
                        .build());


                influxDB.write(Point.measurement("eth")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getEth_close()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getEth_close_20_volty()))
                        .addField("volume", new BigDecimal(exchangeRateCSV.getEth_volume()).movePointRight(4))
                        .build());

                influxDB.write(Point.measurement("ltc")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("price", new BigDecimal(exchangeRateCSV.getLtc_close()))
                        .addField("volty", new BigDecimal(exchangeRateCSV.getLtc_close_20_volty()))
                        .addField("volume", new BigDecimal(exchangeRateCSV.getLtc_volume()).movePointRight(4))
                        .build());

                influxDB.write(Point.measurement("events")
                        .time(unixTimeInSeconds, TimeUnit.SECONDS)
                        .addField("CHN", new BigDecimal(exchangeRateCSV.getCHN()).movePointRight(4))
                        .addField("RUS", new BigDecimal(exchangeRateCSV.getRUS()).movePointRight(4))
                        .addField("KOR", new BigDecimal(exchangeRateCSV.getKOR()).movePointRight(4))
                        .addField("NGA", new BigDecimal(exchangeRateCSV.getNGA()).movePointRight(4))
                        .addField("AF", StringUtils.isEmpty(exchangeRateCSV.getAF()) ? 0 : new BigDecimal(exchangeRateCSV.getAF()))
                        .addField("AN", StringUtils.isEmpty(exchangeRateCSV.getAN()) ? 0 : new BigDecimal(exchangeRateCSV.getAN()))
                        .addField("AS", StringUtils.isEmpty(exchangeRateCSV.getAS()) ? 0 : new BigDecimal(exchangeRateCSV.getAS()))
                        .addField("EU", StringUtils.isEmpty(exchangeRateCSV.getEU()) ? 0 : new BigDecimal(exchangeRateCSV.getEU()))
                        .addField("NA", StringUtils.isEmpty(exchangeRateCSV.getNA()) ? 0 : new BigDecimal(exchangeRateCSV.getNA()))
                        .addField("OC", StringUtils.isEmpty(exchangeRateCSV.getOC()) ? 0 : new BigDecimal(exchangeRateCSV.getOC()))
                        .addField("SA", StringUtils.isEmpty(exchangeRateCSV.getSA()) ? 0 : new BigDecimal(exchangeRateCSV.getSA()))
                        .build());

            } catch (Exception e) {

                logger.error("error writting :\n {}", exchangeRateCSV, e);
            }
        }
        influxDB.close();

    }
}
