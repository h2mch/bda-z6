package ch.hslu.cas.bda.message.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.File;
import java.io.IOException;

public class AvBlockDeserializer<T> {

    public T fromFile(String filename, Class clazz) throws IOException {
        DatumReader<T> datumReader = new SpecificDatumReader<>(clazz);
        try (DataFileReader<T> dataFileReader = new DataFileReader<>(new File(filename), datumReader)) {
            T block = dataFileReader.next();
            return block;
        }
    }
}

