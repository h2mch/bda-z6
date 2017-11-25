package ch.hslu.cas.bda;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class InfluxConnector implements AutoCloseable {

    private InfluxDB influxDB;

    public InfluxConnector(String node, int port) {
        this.connect(node, port);
    }

    public void close() {
        influxDB.close();
    }

    public void connect(String node, Integer port) {
        influxDB = InfluxDBFactory.connect(String.format("http://%s:%s", node, port));
    }

    public void createDB(String dbName) {
        influxDB.createDatabase(dbName);
        influxDB.setDatabase(dbName);
    }

    public void write(Point point) {
        influxDB.write(point);
    }
}
