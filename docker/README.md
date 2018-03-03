
# Getting Started
## Bitcoin Client
To fetch all blockchain files, start the bitcoind client.
```bash
:~/docker/bitcoind$ docker run -v /media/heinz/Elements/docker-share/bitcoin:/home/bitcoin/.bitcoin -it --rm h2m/bitcoind -printtoconsole
2018-02-23 08:03:39 Bitcoin version v0.15.0.1
2018-02-23 08:03:39 InitParameterInteraction: parameter interaction: -whitelistforcerelay=1 -> setting -whitelistrelay=1
2018-02-23 08:03:39 Assuming ancestors of block 0000000000000000003b9ce759c2a087d52abc4266f8f4ebd6d768b89defa50a have valid signatures.
2018-02-23 08:03:39 Using the 'standard' SHA256 implementation
2018-02-23 08:03:39 Using RdRand as an additional entropy source
2018-02-23 08:03:39 Default data directory /home/bitcoin/.bitcoin
2018-02-23 08:03:39 Using data directory /home/bitcoin/.bitcoin
2018-02-23 08:03:39 Using config file /home/bitcoin/.bitcoin/bitcoin.conf
2018-02-23 08:03:39 Using at most 125 automatic connections (1048576 file descriptors available)
...
2018-02-23 08:24:38 UpdateTip: new best=000000000000000000567514524e2f6da509d697fdaa54c1cec09f9f9c87d7e2 height=508760 version=0x20000000 log2_work=88.101815 tx=298952773 date='2018-02-12 03:07:30' progress=0.990048 cache=42.6MiB(316480txo)
2018-02-23 08:24:57 UpdateTip: new best=00000000000000000027abbf33624a3043e6098166ca8e875246f123c40ee471 height=508761 version=0x20000000 log2_work=88.101868 tx=298953421 date='2018-02-12 03:12:34' progress=0.990050 cache=43.3MiB(322324txo)
2018-02-23 08:25:11 UpdateTip: new best=000000000000000000190f9245fea4f58e2f393b549447252353f5f2ee54cac5 height=508762 version=0x20000000 log2_work=88.101922 tx=298954435 date='2018-02-12 03:21:52' progress=0.990056 cache=44.1MiB(328659txo)
2018-02-23 08:25:23 UpdateTip: new best=000000000000000000033e39baaf5f5e638935d8c9dc433ae7eebb16b22ba1fc height=508763 version=0x20000000 log2_work=88.101976 tx=298954947 date='2018-02-12 03:25:57' progress=0.990058 cache=44.9MiB(334894txo)
2018-02-23 08:25:31 Pre-allocating up to position 0x500000 in rev01175.dat
```
The bitcoin files are generated into the mounted files directory `/media/heinz/Elements/docker-share/bitcoin`. This will take some hours/days. Depends on your machine and internet connection.


## Landscape
Start-up
```bash
:~/docker$ docker-compose up -d
:~/docker$ docker ps -a
CONTAINER ID        IMAGE                                   COMMAND                  CREATED             STATUS              PORTS                                                                                            NAMES
2fddda2e84c3        docker_kafka-manager                    "./start-kafka-manag…"   19 seconds ago      Up 15 seconds       0.0.0.0:9000->9000/tcp                                                                           kafka-manager
11befb52753e        docker_kafka-connect                    "/etc/confluent/dock…"   19 seconds ago      Up 17 seconds       0.0.0.0:8083->8083/tcp, 9092/tcp                                                                 kafka-connect
b5cbb0285c67        confluentinc/cp-kafka-rest:4.0.0        "/etc/confluent/dock…"   19 seconds ago      Up 16 seconds       0.0.0.0:8080->8080/tcp, 8082/tcp                                                                 kafka-rest
2d8ef83bc090        confluentinc/cp-schema-registry:4.0.0   "/etc/confluent/dock…"   19 seconds ago      Up 17 seconds       0.0.0.0:8081->8081/tcp                                                                           schema-registry
d2faa5c670ed        confluentinc/cp-kafka:4.0.0             "/etc/confluent/dock…"   20 seconds ago      Up 19 seconds       0.0.0.0:9092->9092/tcp                                                                           kafka
10b6d51937a1        grafana/grafana:latest                  "/run.sh"                20 seconds ago      Up 19 seconds       0.0.0.0:3000->3000/tcp                                                                           grafana
3466f2647c66        confluentinc/cp-zookeeper:4.0.0         "/etc/confluent/dock…"   21 seconds ago      Up 19 seconds       2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp                                                       zookeeper
4a6bac9c71a2        influxdb:1.4.3                          "/entrypoint.sh infl…"   21 seconds ago      Up 20 seconds       0.0.0.0:8086->8086/tcp, 0.0.0.0:8090->8090/tcp, 0.0.0.0:8099->8099/tcp, 0.0.0.0:8085->8083/tcp   influx
...
```

Update to Latest Version
```bash
:~/docker$ docker-compose pull
```

### Start ingestion

KafkaAvroBlockProducer.java
17:40:55.632 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Reading blocks from blk00000.dat to blk01180.dat
17:40:55.633 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Step 1: Pre-processing
17:40:55.852 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Start 2018-02-23T16:40:55.832Z
17:40:55.853 WARN  [org.bitcoinj.core.Context] - Implicitly creating context. This is a migration step and this message will eventually go away.
17:40:55.853 INFO  [org.bitcoinj.core.Context] - Creating bitcoinj 0.15-SEGWIT context.
17:41:06.976 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Block 10000
17:41:06.976 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- TxCount: 10092
17:41:06.977 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Tx/Time: 0.0 Tx/s
17:41:06.977 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Processing time: 11.069s
17:41:06.983 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Date: Mon Apr 06 13:01:39 CEST 2009
17:41:06.983 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Size: 216
[...]
20:55:28.892 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Last block processed: No: 509403 - Date: Thu Feb 15 00:37:27 CET 2018 - Hash: 00000000000000000017938aa6fccde47920ab566efa5487ee07f1579d15640b
20:55:28.892 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Total blocks processed: 509404
20:55:28.893 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Total execution time: 11673s
20:55:29.096 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Block No Map size: 505225
20:55:29.096 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Step 2: Processing
20:55:29.573 WARN  [org.apache.kafka.clients.producer.ProducerConfig] - The configuration 'application.id' was supplied but isn't a known config.
20:55:29.577 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Start 2018-02-23T19:55:29.577Z
20:55:30.322 WARN  [org.apache.kafka.clients.NetworkClient] - [Producer clientId=BitcoinBlockProducer] Error while fetching metadata with correlation id 1 : {bitcoin.block=LEADER_NOT_AVAILABLE}
20:55:30.440 WARN  [org.apache.kafka.clients.NetworkClient] - [Producer clientId=BitcoinBlockProducer] Error while fetching metadata with correlation id 3 : {bitcoin.block=LEADER_NOT_AVAILABLE}
20:55:30.550 WARN  [org.apache.kafka.clients.NetworkClient] - [Producer clientId=BitcoinBlockProducer] Error while fetching metadata with correlation id 4 : {bitcoin.block=LEADER_NOT_AVAILABLE}
20:56:20.336 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Block 10000
20:56:20.336 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- TxCount: 10092
20:56:20.336 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Tx/Time: 0.0 Tx/s
20:56:20.336 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Processing time: 50.759s
20:56:20.336 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Date: Mon Apr 06 13:01:39 CEST 2009
20:56:20.336 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Size: 216
[...]
01:01:46.253 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Block 500000
01:01:46.253 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- TxCount: 20912870
01:01:46.253 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Tx/Time: 25000.0 Tx/s
01:01:46.253 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Processing time: 819.623s
01:01:46.253 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Date: Sat Dec 16 20:48:38 CET 2017
01:01:46.253 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - 	- Size: 1082168

01:12:58.191 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Last block processed: No: 509491 - Date: Fri Feb 16 21:23:56 CET 2018 - Hash: 000000000000000000189b29b1bd90d2911936118b36cafa420fd1d8a8275fc2
01:12:58.192 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Total blocks processed: 509492
01:12:58.192 INFO  [ch.hslu.cas.bda.ingestion.bitcoin.BlockChainProcessorExecutor] - Total execution time: 15448s


KafkaBlockToInfluxStreamTxInput.java
06:36:58.931 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 0 / 000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f
06:36:59.378 WARN  [org.apache.kafka.clients.NetworkClient] - [Producer clientId=BitcoinBlockConsumerTxInput-StreamThread-1-producer] Error while fetching metadata with correlation id 1 : {bitcoin.influx.tx.input=LEADER_NOT_AVAILABLE}
06:36:59.790 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 10000 / 0000000099c744455f58e6c6e98b671e1bf7f37346bfd4cf5d0274ad8ee660cb
06:37:00.102 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 20000 / 00000000770ebe897270ca5f6d539d8afb4ea4f4e757761a34ca82e17207d886
06:37:00.290 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 30000 / 00000000de1250dc2df5cf4d877e055f338d6ed1ab504d5b71c097cdccd00e13
06:37:00.573 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 40000 / 00000000504d5fa0ad2cb90af16052a4eb2aea70fa1cba653b90a4583c5193e4
[...]
07:01:23.642 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 460000 / 000000000000000000ef751bbce8e744ad303c47ece06c8d863e4d417efc258c
07:03:22.157 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 470000 / 0000000000000000006c539c722e280a0769abd510af0073430159d71e6d7589
07:06:05.615 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 480000 / 000000000000000001024c5d7a766b173fc9dbb1be1a4dc7e039e631fd96a8b1
07:07:53.484 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 490000 / 000000000000000000de069137b17b8d5a3dfbd5b145b2dcfb203f15d0c4de90
07:10:03.041 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxInput] - Block: 500000 / 00000000000000000024fb37364cbf81fd49cc2d51c09c75c35433c3a1945d04




KafkaBlockToInfluxStreamTxOutput.java
09:20:49.054 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxOutput] - Block: 0 / 000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f
09:20:49.543 WARN  [org.apache.kafka.clients.NetworkClient] - [Producer clientId=BitcoinBlockConsumerTxOutput-StreamThread-1-producer] Error while fetching metadata with correlation id 1 : {bitcoin.influx.tx.output=LEADER_NOT_AVAILABLE}
09:20:50.231 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxOutput] - Block: 10000 / 0000000099c744455f58e6c6e98b671e1bf7f37346bfd4cf5d0274ad8ee660cb
09:20:50.721 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxOutput] - Block: 20000 / 00000000770ebe897270ca5f6d539d8afb4ea4f4e757761a34ca82e17207d886
09:20:51.074 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxOutput] - Block: 30000 / 00000000de1250dc2df5cf4d877e055f338d6ed1ab504d5b71c097cdccd00e13
[...]
09:48:34.323 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxOutput] - Block: 480000 / 000000000000000001024c5d7a766b173fc9dbb1be1a4dc7e039e631fd96a8b1
09:50:26.865 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxOutput] - Block: 490000 / 000000000000000000de069137b17b8d5a3dfbd5b145b2dcfb203f15d0c4de90
09:52:56.857 INFO  [ch.hslu.cas.bda.aggregation.bitcoin.KafkaBlockToInfluxStreamTxOutput] - Block: 500000 / 00000000000000000024fb37364cbf81fd49cc2d51c09c75c35433c3a1945d04






create influxDB
curl -X POST 'http://docker:8086/query?q=CREATE%20DATABASE%20bitcoin'



running kafka connect
[2018-02-24 12:39:27,674] INFO WorkerSinkTask{id=influxdb-sink-output-0} Committing offsets asynchronously using sequence number 1: {bitcoin.influx.tx.output-0=OffsetAndMetadata{offset=129827, metadata=''}} (org.apache.kafka.connect.runtime.WorkerSinkTask)
[2018-02-24 12:39:27,733] INFO WorkerSinkTask{id=influxdb-sink-input-0} Committing offsets asynchronously using sequence number 1: {bitcoin.influx.tx.input-0=OffsetAndMetadata{offset=7674314, metadata=''}} (org.apache.kafka.connect.runtime.WorkerSinkTask)
[2018-02-24 12:39:27,774] INFO WorkerSinkTask{id=influxdb-sink-block-notags-0} Committing offsets asynchronously using sequence number 1: {bitcoin.block-0=OffsetAndMetadata{offset=426684, metadata=''}} (org.apache.kafka.connect.runtime.WorkerSinkTask)
[2018-02-24 12:39:37,651] INFO WorkerSinkTask{id=influxdb-sink-input-0} Committing offsets asynchronously using sequence number 2: {bitcoin.influx.tx.input-0=OffsetAndMetadata{offset=7906849, metadata=''}} (org.apache.kafka.connect.runtime.WorkerSinkTask)
[2018-02-24 12:39:37,654] INFO Empty list of records received. (com.datamountaineer.streamreactor.connect.influx.InfluxSinkTask)



root@kafka-connect:/# connect-cli ps





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
Send Message to this Topic
```bash
root@kafka:/# seq 10 | kafka-console-producer --request-required-acks 1 --broker-list zookeeper:2181 --topic foo && echo 'Produced 10 messages.'
```

Delete Topic
```bash
root@kafka:/kafka-topics --delete --topic foo --zookeeper zookeeper:2181
```
or outside the container
```bash
docker exec -it kafka kafka-topics --zookeeper zookeeper:2181 --delete --topic TOPICNAME
```

Empty topic workaround (if deletion is not enabled)
```bash
root@kafka:/kafka-topics --alter --topic foo --config retention.ms=1000 --zookeeper zookeeper:2181
```

Show Content of a topic
```bash
root@kafka:/# kafka-console-consumer --zookeeper zookeeper:2181 --topic bitcoin.block -from-beginning
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
```
or via REST

> use bitcoin
Using database bitcoin

> precision 

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

A new updated version is public available. see: https://lenses.stream/connectors/sink/influx.html


Install Sink
```bash
curl -X POST \
  http://docker:8083/connectors \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 2a54b9ec-d92a-ce40-ed4e-78864eb98622' \
  -d '{
  "name": "influxdb-sink-block",
  "config": {
    "connector.class": "com.datamountaineer.streamreactor.connect.influx.InfluxSinkConnector",
    "tasks.max": "1",
    "topics": "bitcoin.block",
    "connect.influx.kcql": "INSERT INTO block SELECT time, difficultyTarget, version, blockNo, blockHash FROM bitcoin.block WITHTIMESTAMP time TIMESTAMPUNIT MILLISECONDS WITHTAG (difficultyTarget)",
	"connect.influx.url": "http://influx:8086",
	"connect.influx.db": "bitcoin",
	"connect.influx.username": "root",
	"connect.influx.password": "root"
  }
}'
```



# Past Bin
## Pre Configured Examples

```bash
docker run --rm -p 2181:2181 -p 3030:3030 -p 8081-8083:8081-8083 -p 9581-9585:9581-9585 -p 9092:9092 -e ADV_HOST=192.168.99.100  landoop/fast-data-dev:latest
```
```bash
docker run -e ADV_HOST=192.168.99.100 -e LICENSE_URL="https://milou.landoop.com/download/lensesdl/?id=2b698b12-...." -e SAMPLEDATA=0 --rm -p 3030:3030 -p 9092:9092 -p 2181:2181 -p 8081:8081 -p 9581:9581 -p 9582:9582 -p 9584:9584 -p 9585:9585 landoop/kafka-lenses-dev
```
- Lenses and Kafka will be available in about 30-45 seconds at http://localhost:3030. 
- The default credentials are admin / admin. 

## Neo4J
```bash
docker run --publish=7474:7474 --publish=7687:7687 --volume=/docker-share/neo4j/data:/data --volume=/docker-share/neo4j/logs:/logs neo4j:3.0
```

## Docker on Ubuntu 17.10
```bash
$ sudo apt-get install apt-transport-https ca-certificates curl software-properties-common
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
$ sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu zesty stable"
$ sudo apt-get update

$ sudo groupadd docker
$ sudo usermod -aG docker $USER

# 3. Log out and log back in so that your group membership is re-evaluated.
```


heinz@x1-carbon:~/source/junk$ git clone https://github.com/confluentinc/ksql.git  
heinz@x1-carbon:~/source/junk$ cd ksql  
heinz@x1-carbon:~/source/junk/ksql$ mvn clean compile install -DskipTests
heinz@x1-carbon:~/source/junk/ksql$ ./bin/ksql-cli local --bootstrap-server localhost:9092





bitcoin.block	            1	1	100	0	0	1	0	0.00	505,215
bitcoin.influx.tx.input	    1	1	100	0	0	1	0	0.00	731,479,402
bitcoin.influx.tx.output	1	1	100	0	0	1	0	0.00	799,454,392