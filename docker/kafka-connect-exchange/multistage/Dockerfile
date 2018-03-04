FROM alpine/git as clone
WORKDIR /app
RUN git clone https://github.com/mmolimar/kafka-connect-fs.git

FROM maven as build
WORKDIR /app
COPY --from=clone /app/kafka-connect-fs /app
USER root
RUN mvn clean package



FROM confluentinc/cp-kafka-connect

RUN wget -P /usr/bin/ https://github.com/Landoop/kafka-connect-tools/releases/download/v1.0.6/connect-cli
RUN chmod 777 /usr/bin/connect-cli

COPY ./*.csv /tmp/

COPY --from=build /app/target/kafka-connect-fs-0.3-SNAPSHOT-package/share/java/kafka-connect-fs/kafka-connect-fs-0.3-SNAPSHOT.jar /etc/kafka-connect/jars/
COPY --from=build /app/target/kafka-connect-fs-0.3-SNAPSHOT-package/share/java/kafka-connect-fs/*.jar /usr/share/java/
RUN chmod 777 /etc/kafka-connect/jars/*.jar