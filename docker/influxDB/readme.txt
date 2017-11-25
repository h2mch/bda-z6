
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
## Get list of fields

```
select * from SERIESNAME limit 1

field names should be alphanumeric with maybe few special chars such as _ to avoid any issues
```