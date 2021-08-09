topic=${1}

if [ "" = "$topic" ]; then
  topic="BTCUSD-index-price"
fi
echo "${topic}"
cd /data/kafka/kafka_2.13-2.8.0
bin/kafka-topics.sh --delete --topic ${topic} --bootstrap-server kafka:9092