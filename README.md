# Kafka Producers project
This repository contains a set of Kafka producer to stream datasets for my Spark projects.

## MongoDB Producer (MongoDB 3.6+)
This producer watch a collection and streams whenever a new document is added (but you can extend for more operations).
```
$ java -jar kafka-mongo-producer.jar --help
usage: MongoProducer [-h] [-t TOPIC] [-k KAFKA_SERVER] [-m MONGO_SERVER]
                     [-d DATABASE] [-c COLLECTION]

Watches a MongoDB collection and streams throughKafka its changes

named arguments:
  -h, --help             show this help message and exit
  -t TOPIC, --topic TOPIC
                         Specify the topic where to stream (default: milof)
  -k KAFKA_SERVER, --kafka-server KAFKA_SERVER
                         Specify the Kafka server (default: localhost:9092)
  -m MONGO_SERVER, --mongo-server MONGO_SERVER
                         Specify  the  Mongo  server  (default:  localhost:
                         27017)
  -d DATABASE, --database DATABASE
                         Specify the database where  to find the collection
                         (default: datasets)
  -c COLLECTION, --collection COLLECTION
                         Specify the collection to watch (default: current)
```                     
Steps:
1. Install the tools you need (Kafka, MongoDB, Python and Java)
3. Run "kafka-servers-start.bat"
4. Run MongoDB in replica mode and initiate the replica
5. java -jar kafka-mongo-producer.jar
6. Insert documents or run "insert-simulator.py"
