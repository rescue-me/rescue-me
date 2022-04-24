#!/usr/bin/env bash

docker run --tty --network rescue-me-app_default confluentinc/cp-kafkacat kafkacat -b kafka:9092 -C -s key=s -s value=avro -r http://schema-registry:8081 -t postgres.public.shelters