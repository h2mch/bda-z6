#!/bin/bash

./run.sh "${@}" &
timeout 10 bash -c "until </dev/tcp/localhost/3000; do sleep 1; done"

curl -s -H "Content-Type: application/json" \
    -XPOST http://admin:admin@localhost:3000/api/datasources \
    -d @- <<EOF
{
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
}
EOF

pkill grafana-server
timeout 10 bash -c "while </dev/tcp/localhost/3000; do sleep 1; done"

exec ./run.sh "${@}"