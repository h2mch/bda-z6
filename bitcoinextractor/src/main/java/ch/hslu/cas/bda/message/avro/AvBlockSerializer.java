package ch.hslu.cas.bda.message.avro;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.io.IOException;

public class AvBlockSerializer<T extends SpecificRecord> {

    public void toFile(T obj, Class<T> clazz, String filename) throws IOException {
        DatumWriter<T> datumWriter = new SpecificDatumWriter<>(clazz);
        try (DataFileWriter<T> dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(obj.getSchema(), new File(filename));
            dataFileWriter.append(obj);
        }
    }
}
