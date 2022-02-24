#!/bin/bash
# check-kafka-topics-created.sh

apt-get update -y

yes | apt-get install kafkacat

kafkacatResult=$(kafkacat -L -b kafka-broker-1:9092)

echo "kafkacat result:" $kafkacatResult

while [[ ! $kafkacatResult == *"twitter-topic"* ]]; do
  dt=$(date '+%d/%m/%Y %H:%M:%S');
  >&2 echo "$dt Kafka topic has not been created yet!"
  sleep 10
  kafkacatResult=$(kafkacat -L -b kafka-broker-1:9092)
done

./cnb/process/web