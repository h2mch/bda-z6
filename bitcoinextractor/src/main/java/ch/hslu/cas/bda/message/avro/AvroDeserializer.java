package ch.hslu.cas.bda.message.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.File;
import java.io.IOException;

public class AvroDeserializer<T> {

    public T fromFile(String filename, Class clazz) throws IOException {
        DatumReader<T> datumReader = new SpecificDatumReader<>(clazz);
        try (DataFileReader<T> dataFileReader = new DataFileReader<>(new File(filename), datumReader)) {
            T obj = dataFileReader.next();
            return obj;
        }
    }

    public T fromByteArray(byte[] bytes, Class clazz) throws IOException {
        DatumReader<T> datumReader = new SpecificDatumReader<>(clazz);
        Decoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
        T obj = datumReader.read(null, decoder);
        return obj;
    }
}

