#!/usr/bin/env bash
curl -s -X GET http://localhost:8083/connector-plugins | jq '.[].class'