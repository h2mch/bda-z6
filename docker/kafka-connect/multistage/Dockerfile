FROM alpine/git as clone
WORKDIR /app
RUN git clone https://github.com/Landoop/stream-reactor.git

FROM gradle as build
WORKDIR /app
COPY --from=clone /app/stream-reactor /app
USER root
RUN ./gradlew -x test :kafka-connect-influxdb:shadowJar


FROM confluentinc/cp-kafka-connect
MAINTAINER Heinz Marti, github@h2m.ch
COPY --from=build /app/kafka-connect-influxdb/build/libs/*.jar /usr/local/share/kafka/plugins/
RUN wget -P /usr/bin/ https://github.com/Landoop/kafka-connect-tools/releases/download/v1.0.6/connect-cli
RUN chmod 777 /usr/bin/connect-cli
RUN chmod 777 /usr/local/share/kafka/plugins/*.jar