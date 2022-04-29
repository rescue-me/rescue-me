#!/usr/bin/env bash
curl -i -X POST -H "Content-Type:application/json" \
  http://localhost:8083/connectors/ \
  -d '{
        "name": "source-debezium-shelters-00",
        "config": {
            "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
            "tasks.max": "1",
            "database.hostname": "postgres",
            "database.port": "5432",
            "database.user": "docker",
            "database.password": "docker",
            "database.dbname" : "postgres",
            "database.server.name": "dbserver1",
            "table.whitelist": "public.shelter",
            "key.converter": "io.confluent.connect.avro.AvroConverter",
            "key.converter.schema.registry.url": "http://schema-registry:8081",
            "value.converter": "io.confluent.connect.avro.AvroConverter",
            "value.converter.schema.registry.url": "http://schema-registry:8081"
            }
       }'


