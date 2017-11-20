
FROM cassandra:latest

# Enable JMX monitoring
COPY ./jmxremote.password /etc/cassandra/jmxremote.password
RUN chmod 400 /etc/cassandra/jmxremote.password
COPY ./jmxremote.password /etc/java-8-openjdk/management/jmxremote.password