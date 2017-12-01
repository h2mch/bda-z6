
# Landscape
```bash
$ docker-compose up -d
...
$ docker logs kafka
...
$ docker logs -f kafka
...
```


# Commands
## Kafka Bash

Login into running kafka broker
```bash
docker exec -it kafka /bin/bash
```

Create new Topic
```bash
root@kafka:/kafka-topics --describe --topic foo --zookeeper zookeeper:2181
```
send Message to this Topic
```bash
root@kafka:/# seq 10 | kafka-console-producer --request-required-acks 1 --broker-list zookeeper:2181 --topic foo && echo 'Produced 10 messages.'
```

## Kafka HTTP
This is provided by the docker kafka-rest container


Create Consumer
```bash
$ curl -X POST \
  http://docker:8080/consumers/my_avro_rest_consumer \
  -H 'content-type: application/vnd.kafka.v1+json' \
  -d '{"name": "my_consumer_instance", "format": "avro", "auto.offset.reset": "smallest"}'

{
    "instance_id": "my_consumer_instance",
    "base_uri": "http://kafka-rest:8082/consumers/my_avro_rest_consumer/instances/my_consumer_instance"
}
$
```

Consume Data from Topic
```bash
$ curl -X GET \
  http://docker:8080/consumers/my_avro_rest_consumer/instances/my_consumer_instance/topics/foo \
  -H 'accept: application/vnd.kafka.avro.v1+json'
```



## Influx
### Bash
Create new DB
```bash
hem@zrhn1810 MINGW64 /c/source/CAS-BDA/docker/confluent (master)
$ docker exec -it influx /bin/bash
root@influx:/# influx
Visit https://enterprise.influxdata.com to register for updates, InfluxDB server management, and monitoring.
Connected to http://localhost:8086 version 1.0.0
InfluxDB shell version: 1.0.0
> CREATE DATABASE myFoo
>
```
or via REST
### REST

Create DB
http://docker:8086/query?q=CREATE DATABASE myFoo


## Influx
### REST
Create Datasource
```bash
curl -X POST \
  http://docker:3000/api/datasources \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '    {
        "name": "bitcoin",
        "type": "influxdb",
        "typeLogoUrl": "public/app/plugins/datasource/influxdb/img/influxdb_logo.svg",
        "access": "direct",
        "url": "http://docker:8086",
        "password": "root",
        "user": "root",
        "database": "bitcoin",
        "basicAuth": false,
        "isDefault": true,
        "jsonData": {}
    }' 
```



## Kafka Connect Bash
Login into kafka connect
```bash
hem@zrhn1810 MINGW64 /c/source/CAS-BDA/docker/confluent (master)
$ docker exec -it kafka-connect /bin/bash
```

### Install InfluxDB Sink
Build InfluxDB Sink from Source
```bash
git clone https://github.com/Landoop/stream-reactor.git

hem@zrhn1810 MINGW64 /c/temp/stream-reactor (master)
$./gradlew :kafka-connect-influxdb:shadowJar
```
Copy the jar file into the Docker `confluent\kafka-connect` folder

Install Sink
```bash
curl -X POST \
  http://docker:8083/connectors \
  -H 'content-type: application/json' \
  -d '{
  "name": "influxdb-sink",
  "config": {
    "connector.class": "com.datamountaineer.streamreactor.connect.influx.InfluxSinkConnector",
    "tasks.max": "1",
    "topics": "bitcoin.block",
    "connect.influx.kcql": "INSERT INTO block SELECT time, difficultyTarget, version, blockNo, blockHash FROM influx-topic WITHTIMESTAMP time",
	"connect.influx.url": "http://influx:8086",
	"connect.influx.db": "bitcoin",
	"connect.influx.username": "admin",
	"connect.influx.password": "admin"
  }
}'
```