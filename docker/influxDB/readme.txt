
# Docker
```bash
docker run -d --name=influx --volume=/source/CAS-BDA/docker/data/influxdb:/data -p 8083:8083 -p 8086:8086 -p 8090:8090 -p 8099:8099 tutum/influxdb
```


to update docker image:
```
docker stop influx
docker pull tutum/influxdb
docker run -d --name=influx ...
```

# Influx

## Setup DB
```bash
$ influx --host localhost --port 8086
> CREATE DATABASE devices
> CREATE USER root WITH PASSWORD 'root'
> GRANT ALL ON devices to root
```

## Get list of fields
```SQL
select * from SERIESNAME limit 1
```
field names should be alphanumeric with maybe few special chars such as _ to avoid any issues

## REST
```bash
curl -XPOST 'http://docker:8086/query' --data-urlencode "q=CREATE DATABASE myRestdb"
```
Insert some data
```bash
curl -XPOST 'http://docker:8086/write?db=myRestdb' -d 'cpu,host=server01,region=luzern load=42 1434055562000000000'
curl -XPOST 'http://docker:8086/write?db=myRestdb' -d 'cpu,host=server02,region=bern load=78 1434055562000000000'
```

Query for the data
```bash
curl -XGET http://docker:8086/query?pretty=true --data-urlencode "db=myRestdb" --data-urlencode "q=SELECT * FROM cpu WHERE host='server01' AND time < now() - 1d"
curl -XGET http://docker:8086/query?pretty=true&db=myRestdb&q=SELECT%20*%20FROM%20cpu%20WHERE%20host%3D'\''server01'\''%20AND%20time%20%3C%20now()%20-%201d
```
Analyze the data
```bash
curl -XGET http://docker:8086/query?pretty=true --data-urlencode "db=myRestdb" --data-urlencode "q=SELECT mean(load) FROM cpu WHERE region='luzern'"
```


# Java
https://github.com/influxdata/influxdb-java


# tableau
https://tagyoureit.github.io/InfluxDB_WDC/

# RStudio
see /../R/Intro/InfluxDB.R