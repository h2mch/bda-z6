package ch.hslu.cas.bda.ingestion;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.util.concurrent.Future;

public interface AvroProcessor<T> {

    void onEnd();

    void onStart();

    Future<RecordMetadata> process(long number, T element) throws IOException;
}
