# bda-z6
A Demo project to get some big data / analytics knowledge about: Bitcoin, Exchangerates, Kafka (Stream), Avro, InfluxDb. It is based on a [kappa streaming platform](https://www.oreilly.com/ideas/questioning-the-lambda-architecture).

The folder **bitcoinextracter** contains serveral small Java Application to 
 * parse the bitcoin blockchain files
 * send bitcoin blockchain data into a kafka topic
 * process events betweend different kafka topic
 * send exchangerate data into kafka topics or into InfluxDb
 
 The folder **docker** contains different images description (Dockerfile, docker compose) to setup the streaming platform
 
 The folder **resources** contains data which are extracted from an other process or generated by our self.
