package ch.hslu.cas.bda.ingestion.bitcoin;

import org.bitcoinj.core.Block;

import java.io.IOException;

public interface IBlockProcessor {
    void process(long blockCount, Block block) throws IOException;
}
