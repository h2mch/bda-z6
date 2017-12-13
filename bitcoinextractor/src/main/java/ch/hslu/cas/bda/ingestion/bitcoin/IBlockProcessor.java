package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;

public interface IBlockProcessor {

    void onStart();

    void process(long blockCount, Block block);

    void onEnd();
}
