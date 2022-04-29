#!/usr/bin/env bash
kafka-console-consumer \
  --bootstrap-server localhost:29092 \
  --from-beginning \
  --property value.converter=io.confluent.connect.avro.AvroConverter \
  --topic pg-dev.public.shelter