#!/usr/bin/env bash
curl -i -X POST -H "Content-Type:application/json" \
  http://localhost:8083/connectors/ \
  -d '{
        "name": "source-debezium-dog",
        "config": {
            "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
            "tasks.max": "1",
            "database.hostname": "postgres",
            "database.port": "5432",
            "database.user": "docker",
            "database.password": "docker",
            "database.dbname" : "rescueme",
            "database.server.name": "pg-dev",
            "table.include.list": "public.dog",
            "key.converter": "org.apache.kafka.connect.storage.StringConverter",
            "value.converter": "org.apache.kafka.connect.json.JsonConverter",
            "value.converter.schemas.enable": "false",
            "slot.name" : "1"
            }
       }'


