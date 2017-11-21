package ch.hslu.cas.bda;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraConnector {

    private Cluster cluster;
    private Session session;

    public CassandraConnector(String node, int port) {
        this.connect(node, port);
        this.session = getSession();
    }

    public void close() {
        session.close();
        cluster.close();
    }

    public void connect(String node, Integer port) {
        Cluster.Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();

        session = cluster.connect();
    }

    public void createKeyspace(String keyspaceName) {
        StringBuilder sb =
                new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                        .append(keyspaceName).append(" WITH replication = {")
                        .append("'class':'").append("SimpleStrategy")
                        .append("','replication_factor':").append(1)
                        .append("};");

        String query = sb.toString();
        session.execute(query);
    }

    public ResultSet execute(String query) {
        return session.execute(query);
    }

    public Session getSession() {
        return this.session;
    }
}
