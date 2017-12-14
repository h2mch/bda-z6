package ch.hslu.cas.bda.message.avro;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import ch.hslu.cas.bda.ingestion.exchangerate.CoinbaseExchangeRate;
import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import ch.hslu.cas.bda.message.bitcoin.AvExchangeRate;
import ch.hslu.cas.bda.message.bitcoin.AvTransaction;
import ch.hslu.cas.bda.message.bitcoin.Input;
import ch.hslu.cas.bda.message.bitcoin.Output;

public class AvroConverter {

    public static AvBlock toAvBlock(Block block, long blockNo) {
        AvBlock avBlock = new AvBlock();
        avBlock.setBlockHash(block.getHashAsString());
        avBlock.setTime(block.getTimeSeconds() * 1000); //influxDB is using UNIX TimeStamp in ms
        avBlock.setVersion(block.getVersion());
        avBlock.setDifficultyTarget(block.getDifficultyTarget());
        avBlock.setPreviousBlockHash(block.getPrevBlockHash().toString());

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

        // avTx.setOutputSum(avTx.getVout().stream().mapToLong(t -> t.getValue()).sum());
        return avTx;
    }

    public static AvExchangeRate toExchangeRate(CoinbaseExchangeRate coinbaseExchangeRate) {
        AvExchangeRate avExchangeRate = new AvExchangeRate();
        avExchangeRate.setClose(new BigDecimal(coinbaseExchangeRate.getClose()));
        avExchangeRate.setHigh(new BigDecimal(coinbaseExchangeRate.getHigh()));
        avExchangeRate.setLow(new BigDecimal(coinbaseExchangeRate.getLow()));
        avExchangeRate.setVolumeBTC(new BigDecimal(coinbaseExchangeRate.getVolumeBTC()));
        avExchangeRate.setOpen(new BigDecimal(coinbaseExchangeRate.getOpen()));
        avExchangeRate.setVolumeDollar(new BigDecimal(coinbaseExchangeRate.getVolumeDollar()));
        avExchangeRate.setWeightedPrice(new BigDecimal(coinbaseExchangeRate.getWeightedPrice()));
        avExchangeRate.setTime(Long.parseLong(coinbaseExchangeRate.getTimeStamp()));
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