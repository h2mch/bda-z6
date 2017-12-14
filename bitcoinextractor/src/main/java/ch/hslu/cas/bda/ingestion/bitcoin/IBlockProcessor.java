package ch.hslu.cas.bda.ingestion.bitcoin;

import java.io.IOException;

public interface IBlockProcessor<T> {

    void onStart();

    void process(long blockCount, T block) throws IOException;

    void onEnd();
}
