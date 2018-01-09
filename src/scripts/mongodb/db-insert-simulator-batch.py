from pymongo import MongoClient
import time
from os import system

system('title streamer')
MONGO_SERV = 'localhost:27017'
SRC_COLLECTION = 'iris'
OUT_COLLECTION = 'current'
TIMEOUT = 3     # number of seconds
BATCH_SIZE = 3

client = MongoClient(MONGO_SERV)
db = client.datasets
srcColl = db[SRC_COLLECTION]
outColl = db[OUT_COLLECTION]
outColl.delete_many({})

docs = []
for doc in srcColl.find():
    if len(docs) == BATCH_SIZE:
        for d in docs:
            print (d)
            outColl.insert_one(d)
        docs = []
        time.sleep(TIMEOUT)
    else:
        docs.append(doc)
        
client.close()
