#!/usr/bin/env bash
kcat -b localhost:9092 -t pg-dev.public.dog -s value=avro -r http://localhost:8081
