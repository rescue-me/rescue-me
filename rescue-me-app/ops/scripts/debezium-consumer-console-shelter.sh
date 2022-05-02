#!/usr/bin/env bash
docker run --tty --network rescue-me-app_default confluentinc/cp-kafkacat kafkacat -b kafka:19092 -C -s key=s -s value=avro -r http://schema-registry:8081 -t pg-dev.public.shelter