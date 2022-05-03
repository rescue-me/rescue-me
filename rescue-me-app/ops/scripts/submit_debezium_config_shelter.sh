#!/usr/bin/env bash
curl -i -X POST -H "Content-Type:application/json" \
  http://localhost:8083/connectors/ \
  -d '{
        "name": "rescueme-jdbc-source",
        "config": {
            "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
            "tasks.max": "1",
            "connection.url": "jdbc:postgresql://postgres:5432/rescueme",
            "connection.user": "docker",
            "connection.password": "docker",
            "table.whitelist": "public.shelter",
            "topic.prefix": "",
            "name": "rescueme-jdbc-source",
            "mode": "bulk",
            "key.converter": "org.apache.kafka.connect.storage.StringConverter",
            "value.converter": "org.apache.kafka.connect.json.JsonConverter",
            "value.converter.schemas.enable": "false",
            "slot.name" : "1"
            }
       }'


