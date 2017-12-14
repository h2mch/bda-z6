package ch.hslu.cas.bda.ingestion;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

public class ElementExecutor {

    private final AvroProcessor processor;

    public ElementExecutor(AvroProcessor proccessor) {
        this.processor = proccessor;
    }


    public void process(List list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List of files is empty");
        }
        System.out.println("Start processing " + list.size() + " Elements");


        long count = 0;
        try {
            long procStartTime = System.currentTimeMillis();
            System.out.println("Start" + new Date(procStartTime));
            processor.onStart();

            for (Object o : list) {
                count++;
                try {
                    // Use of ExecutorService service = Executors.newFixedThreadPool...
                    Future<RecordMetadata> result = processor.process(count, o);
                    result.get();
                } catch (Exception e) {
                    System.err.println("Error processing element " + count);
                    System.err.println("Element:\n" + o.toString());
                    e.printStackTrace();
                }

                if (count % 10000 == 0) {
                    long procTimeInMs = System.currentTimeMillis() - procStartTime;
                    System.out.print(String.format("Element %6d in %6.0f s", count, procTimeInMs * 1000.0));
                    System.out.println();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            processor.onEnd();
        }

    }

}
