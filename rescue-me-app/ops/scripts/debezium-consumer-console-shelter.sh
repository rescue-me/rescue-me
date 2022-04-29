#!/usr/bin/env bash
kcat -b localhost:29092 -t pg-dev.public.shelter -s value=avro -r http://localhost:8081