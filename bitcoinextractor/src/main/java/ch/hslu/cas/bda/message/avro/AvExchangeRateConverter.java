package ch.hslu.cas.bda.message.avro;

import ch.hslu.cas.bda.ingestion.exchangerate.CoinbaseExchangeRate;
import ch.hslu.cas.bda.ingestion.exchangerate.GdaxExchangeRate;
import ch.hslu.cas.bda.message.bitcoin.AvExchangeRate;
import ch.hslu.cas.bda.message.bitcoin.AvExchangeRateGDAX;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AvExchangeRateConverter {

    private static final LocalDate epoch = LocalDate.ofEpochDay(0);
    private static final BigDecimal THOUSAND = new BigDecimal(1000l);
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");


    public static AvExchangeRate toExchangeRate(CoinbaseExchangeRate coinbaseExchangeRate) {
        AvExchangeRate avExchangeRate = new AvExchangeRate();
        avExchangeRate.setClose(new BigDecimal(coinbaseExchangeRate.getClose()).multiply(THOUSAND).longValue());
        avExchangeRate.setHigh(new BigDecimal(coinbaseExchangeRate.getHigh()).multiply(THOUSAND).longValue());
        avExchangeRate.setLow(new BigDecimal(coinbaseExchangeRate.getLow()).multiply(THOUSAND).longValue());
        avExchangeRate.setVolumeBTC(new BigDecimal(coinbaseExchangeRate.getVolumeBTC()).multiply(THOUSAND).longValue());
        avExchangeRate.setOpen(new BigDecimal(coinbaseExchangeRate.getOpen()).multiply(THOUSAND).longValue());
        avExchangeRate.setVolumeDollar(new BigDecimal(coinbaseExchangeRate.getVolumeDollar()).multiply(THOUSAND).longValue());
        avExchangeRate.setWeightedPrice(new BigDecimal(coinbaseExchangeRate.getWeightedPrice()).multiply(THOUSAND).longValue());
        avExchangeRate.setTime(Long.parseLong(coinbaseExchangeRate.getTimeStamp()) * 1000);
        return avExchangeRate;
    }


    public static AvExchangeRateGDAX toExchangeRate(GdaxExchangeRate gdaxExchangeRate) {
        LocalDate date = LocalDate.parse(gdaxExchangeRate.getDate_nr(), FORMATTER);

        AvExchangeRateGDAX avExchangeRateGDAX = new AvExchangeRateGDAX(
                // Other time unit (than millis since epoch) are not supported by the kafka connect influx connector
                date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000,
                toMilliPrecision(gdaxExchangeRate.getDailyValueXAU()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueXAG()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueXPT()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueCAD()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueEUR()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueJPY()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueGBP()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueCHF()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueAUD()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueHKD()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueNZD()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueKRW()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueMXN()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayxau_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayxag_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayxpt_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaycad_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayeur_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayjpy_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaygbp_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaychf_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayaud_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayhkd_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaynzd_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaykrw_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaymxn_prc()),
                toMilliPrecision(gdaxExchangeRate.getDailyValuebtc_low()),
                toMilliPrecision(gdaxExchangeRate.getDailyValuebtc_high()),
                toMilliPrecision(gdaxExchangeRate.getDailyValuebtc_open()),
                toMilliPrecision(gdaxExchangeRate.getDailyValuebtc_close()),
                toMilliPrecision(gdaxExchangeRate.getDailyValuebtc_volume()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaybtc_low_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaybtc_high_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaybtc_open_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaybtc_close_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDaybtc_volume_prc()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueeth_low()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueeth_high()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueeth_open()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueeth_close()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueeth_volume()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayeth_low_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayeth_high_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayeth_open_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayeth_close_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayeth_volume_prc()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueltc_low()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueltc_high()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueltc_open()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueltc_close()),
                toMilliPrecision(gdaxExchangeRate.getDailyValueltc_volume()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayltc_low_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayltc_high_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayltc_open_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayltc_close_prc()),
                toMilliPrecision(gdaxExchangeRate.getDiffToPreDayltc_volume_prc())
        );

        return avExchangeRateGDAX;

    }

    private static Long toMilliPrecision(String value) {
        if (StringUtils.isEmpty(value)) {
            return -1l;
        }
        BigDecimal milliValue = new BigDecimal(value).multiply(THOUSAND);
        return milliValue.longValue();
    }
}
