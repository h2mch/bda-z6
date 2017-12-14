package ch.hslu.cas.bda.ingestion.bitcoin;

public interface IBlockProcessor<T> {

    void onStart();

    void process(long blockCount, T block);

    void onEnd();
}
