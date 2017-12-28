package ch.hslu.cas.bda.ingestion;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Future;

public class ElementExecutor {


    private static Logger logger = LoggerFactory.getLogger(ElementExecutor.class);

    private final AvroProcessor processor;

    public ElementExecutor(AvroProcessor proccessor) {
        this.processor = proccessor;
    }


    public void process(List list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List of files is empty");
        }

        try {
            long count = 0;
            long procStartTime = System.currentTimeMillis();
            processor.onStart();

            for (Object o : list) {
                count++;
                try {
                    // Use of ExecutorService service = Executors.newFixedThreadPool...
                    Future<RecordMetadata> result = processor.process(count, o);
                    if (count % 1000 == 0) {
                        RecordMetadata recordMetadata = result.get();
                        long procTimeInMs = System.currentTimeMillis() - procStartTime;
                        logger.info("\t\tElement {} in {} s into '{}'", count, procTimeInMs * 1000.0, recordMetadata.topic() );
                    }
                } catch (Exception e) {
                    logger.error("Error processing element {}", count, e);
                }
            }
        } finally {
            processor.onEnd();
        }

    }

}
