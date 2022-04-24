#!/usr/bin/env bash

curl -i -X POST -H "Content-Type:application/json" \
  http://localhost:8083/connectors/ \
  -d '{
            "name": "shelters-connector",
            "config": {
                "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
                 "plugin.name": "pgoutput",
                "database.hostname": "postgres",
                "database.port": "5432",
                "database.user": "docker",
                "database.password": "docker",
                "database.dbname": "rescueme",
                "database.server.name": "postgres",
                "table.include.list": "public.shelters"
            }
    }'