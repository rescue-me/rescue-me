#!/usr/bin/env bash
docker compose exec connect bash -c 'kafka-topics --bootstrap-server kafka:9092 --list'