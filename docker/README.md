

Pre Configured Examples

```bash
docker run --rm -p 2181:2181 -p 3030:3030 -p 8081-8083:8081-8083 -p 9581-9585:9581-9585 -p 9092:9092 -e ADV_HOST=192.168.99.100  landoop/fast-data-dev:latest
```
```bash
docker run -e ADV_HOST=192.168.99.100 -e LICENSE_URL="https://milou.landoop.com/download/lensesdl/?id=2b698b12-...." -e SAMPLEDATA=0 --rm -p 3030:3030 -p 9092:9092 -p 2181:2181 -p 8081:8081 -p 9581:9581 -p 9582:9582 -p 9584:9584 -p 9585:9585 landoop/kafka-lenses-dev
```
- Lenses and Kafka will be available in about 30-45 seconds at http://localhost:3030. 
- The default credentials are admin / admin. 
