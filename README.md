# Kafka Producers project
This repository contains a set of kafka producer to stream datasets for my Spark projects.

## MongoDB Producer (MongoDB 3.6+)
This producer watch a collection and streams whenever a new document is added (but you can extend for more operations).
Steps:
1. Install the tools you need (Kafka, MongoDB, Python and Java)
2. Edit "mongo-import_csv.bat" as you need to load a dataset to MongoDB 
3. Run "kafka-servers-start.bat" (first time you need to remove EXIT to create the topic)
4. Run MongoDB in replica mode and initiate the replica (see src/script/instructions.md)
5. Run the MongoProducer
6. Insert documents or run "db-insert-simulator.py" ("db-insert-simulator-batch.py" for batch insertions)