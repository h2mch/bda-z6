FROM confluentinc/cp-kafka-connect

MAINTAINER Heinz Marti, github@h2m.ch

COPY ./kafka-connect-fs/*.jar /usr/local/share/kafka/plugins/

RUN wget -P /usr/bin/ https://github.com/Landoop/kafka-connect-tools/releases/download/v1.0.6/connect-cli
RUN chmod 777 /usr/bin/connect-cli
RUN chmod 777 /usr/local/share/kafka/plugins/*.jar