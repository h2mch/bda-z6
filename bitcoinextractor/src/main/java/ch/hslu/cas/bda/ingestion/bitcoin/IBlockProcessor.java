package ch.hslu.cas.bda.ingestion.bitcoin;

import java.io.IOException;

public interface IBlockProcessor<T> {

    void onStart();

    void process(long number, T element) throws IOException;

    void onEnd();
}
