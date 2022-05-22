#!/usr/bin/env bash
docker run --rm --tty --network rescue-me-app_default confluentinc/cp-kafkacat kafkacat -b kafka:19092 -C -s key=s -s value=s -t shelter