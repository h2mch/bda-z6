#Minified Docker image with Java
FROM anapsix/alpine-java


MAINTAINER wurstmeister

ENV KAFKA_VERSION=1.0.0
ENV SCALA_VERSION=2.12

RUN apk add --update unzip wget curl docker coreutils


RUN wget -q "http://mirror.switch.ch/mirror/apache/dist/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz" \
         -O "/tmp/kafka.tgz" && \
    tar xfz /tmp/kafka.tgz -C /opt && \
    rm /tmp/kafka.tgz && \
    ln -s /opt/kafka_${SCALA_VERSION}-${KAFKA_VERSION} /opt/kafka

VOLUME ["/kafka"]

ENV KAFKA_HOME /opt/kafka
ENV PATH ${PATH}:${KAFKA_HOME}/bin
ADD start-kafka.sh /usr/bin/start-kafka.sh
ADD broker-list.sh /usr/bin/broker-list.sh
ADD create-topics.sh /usr/bin/create-topics.sh
# The scripts need to have executable permission
RUN chmod a+x /usr/bin/start-kafka.sh && \
    chmod a+x /usr/bin/broker-list.sh && \
    chmod a+x /usr/bin/create-topics.sh
# Use "exec" form so that it runs as PID 1 (useful for graceful shutdown)
CMD ["start-kafka.sh"]