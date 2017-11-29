package ch.hslu.cas.bda.message.avro;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AvroSerializer<T extends SpecificRecord> {

    public void toFile(T obj, Class<T> clazz, String filename) throws IOException {
        DatumWriter<T> datumWriter = new SpecificDatumWriter<>(clazz);
        try (DataFileWriter<T> dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(obj.getSchema(), new File(filename));
            dataFileWriter.append(obj);
        }
    }

    public byte[] toByteArray(T obj, Class<T> clazz) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            DatumWriter<T> writer = new SpecificDatumWriter<>(clazz);

            writer.write(obj, encoder);
            encoder.flush();
            return out.toByteArray();
        }
    }
}
