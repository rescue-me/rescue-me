#!/usr/bin/env bash
docker-compose exec connect bash -c "confluent-hub install --no-prompt debezium/debezium-connector-postgresql:1.8.1"