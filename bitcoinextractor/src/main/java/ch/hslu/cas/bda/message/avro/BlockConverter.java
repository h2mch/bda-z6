package ch.hslu.cas.bda.message.avro;

import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import ch.hslu.cas.bda.message.bitcoin.AvTransaction;
import ch.hslu.cas.bda.message.bitcoin.Input;
import ch.hslu.cas.bda.message.bitcoin.Output;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;

import java.util.stream.Collectors;

public class BlockConverter {

    public AvBlock toAvBlock(Block block) {
        AvBlock message = new AvBlock();
        message.setBlockHash(block.getHashAsString());
        message.setTime(block.getTimeSeconds());
        message.setVersion(block.getVersion());
        message.setDifficultyTarget(block.getDifficultyTarget());
        message.setPreviousBlockHash(block.getPrevBlockHash().toString());

        message.setTransactions(block.getTransactions().
                stream().map(BlockConverter::toAvTransactionMessage).collect(Collectors.toList()));

        return message;
    }

    private static AvTransaction toAvTransactionMessage(Transaction tx) {
        AvTransaction avTx = new AvTransaction();

        avTx.setVersion(tx.getVersion());
        // avTx.setOutputSum(tx.getOutputSum().getValue());
        avTx.setVin(tx.getInputs().
                stream().map(BlockConverter::toInputMessage).filter(t -> t != null).collect(Collectors.toList()));

        avTx.setVout(tx.getOutputs().
                stream().map(BlockConverter::toOutputMessage).filter(t -> t != null).collect(Collectors.toList()));

        // avTx.setOutputSum(avTx.getVout().stream().mapToLong(t -> t.getValue()).sum());
        return avTx;
    }

    private static Input toInputMessage(TransactionInput txInput) {

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


    private static Output toOutputMessage(TransactionOutput txOutput) {
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