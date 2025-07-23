#!/bin/sh

sleep 3 # ждём старта Consul

for file in /configs/*.json; do
  echo "Importing $file..."
  consul kv import -http-addr=http://consul:8500 @$file
done