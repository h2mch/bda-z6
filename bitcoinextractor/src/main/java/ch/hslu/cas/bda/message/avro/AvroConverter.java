package ch.hslu.cas.bda.message.avro;

import ch.hslu.cas.bda.ingestion.exchangerate.CoinbaseExchangeRate;
import ch.hslu.cas.bda.ingestion.exchangerate.GdaxExchangeRate;
import ch.hslu.cas.bda.message.bitcoin.*;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class AvroConverter {

    private static final LocalDate epoch = LocalDate.ofEpochDay(0);
    private static final BigDecimal THOUSAND = new BigDecimal(1000l);
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static AvBlock toAvBlock(Block block, long blockNo) {
        AvBlock avBlock = new AvBlock();
        avBlock.setBlockHash(block.getHashAsString());
        avBlock.setTime(block.getTimeSeconds() * 1000); //influxDB is using UNIX TimeStamp in ms
        avBlock.setVersion(block.getVersion());
        avBlock.setDifficultyTarget(block.getDifficultyTarget());
        avBlock.setPreviousBlockHash(block.getPrevBlockHash().toString());
        avBlock.setSize(block.getMessageSize());

        avBlock.setTransactions(block.getTransactions().
                stream().map(AvroConverter::toAvTransaction).collect(Collectors.toList()));

        avBlock.setBlockNo(blockNo);
        return avBlock;
    }

    private static AvTransaction toAvTransaction(Transaction tx) {
        AvTransaction avTx = new AvTransaction();

        avTx.setVersion(tx.getVersion());
        // avTx.setOutputSum(tx.getOutputSum().getValue());
        avTx.setVin(tx.getInputs().
                stream().map(AvroConverter::toInput).filter(t -> t != null).collect(Collectors.toList()));

        avTx.setVout(tx.getOutputs().
                stream().map(AvroConverter::toOutput).filter(t -> t != null).collect(Collectors.toList()));

        return avTx;
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

    private static Input toInput(TransactionInput txInput) {

        // Don't treat coin base as a AvTransaction input
        if (txInput.isCoinBase()) {
            return null;
        }
        Input avInput = new Input();


        if (txInput.getOutpoint() != null) {
            avInput.setTxid(txInput.getOutpoint().getHash().toString());
            avInput.setVout(txInput.getOutpoint().getIndex());
        }

        return avInput;
    }


    private static Output toOutput(TransactionOutput txOutput) {
        Output avOutput = new Output();
        avOutput.setValue(txOutput.getValue().getValue());
        Script script = getScript(txOutput.getScriptBytes());

        if (script.getProgram().length == 0) {
            // System.err.println("Value: " + avOutput.getValue() + ", TxHash: " + txOutput.getParentAvTransaction().getHashAsString() + " - " + script.toString());
        }

        avOutput.setAddress(getScriptAddress(script));

        if ("".equals(avOutput.getAddress())) {
            // System.err.println("Value: " + avOutput.getValue() + ", TxHash: " + txOutput.getParentAvTransaction().getHashAsString() + " - " + script.toString());
        }

        return avOutput;
    }

    private static Script getScript(byte[] scriptBytes) {
        try {
            return new Script(scriptBytes);
        } catch (Exception e) {
            // System.err.println("getScript(): " + e.getMessage() + ":" + Utils.HEX.encode(scriptBytes));
            return new Script(new byte[0]);
        }
    }

    private static String getScriptAddress(Script script) {
        String address = "";
        try {
            if (script != null) {
                address = script.getToAddress(MainNetParams.get(), true).toString();
            }
        } catch (Exception e) {
            // System.out.println("getScriptAddress(): " + e.getMessage() + ":" + script.toString());
        }
        return address;
    }

}