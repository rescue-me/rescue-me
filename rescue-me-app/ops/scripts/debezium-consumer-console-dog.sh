#!/usr/bin/env bash
kafka-console-consumer \
  --bootstrap-server localhost:29092 \
  --from-beginning \
  --formatter io.confluent.kafka.formatter.AvroMessageFormatter \
  --topic pg-dev.public.dog