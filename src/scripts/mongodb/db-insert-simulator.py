from pymongo import MongoClient
import time
from os import system

system('title streamer')
MONGO_SERV = 'localhost:27017'
SRC_COLLECTION = 'iris'
OUT_COLLECTION = 'current'
TIMEOUT = 3    # number of seconds

client = MongoClient(MONGO_SERV)
db = client.datasets
srcColl = db[SRC_COLLECTION]
outColl = db[OUT_COLLECTION]
outColl.delete_many({})

for doc in srcColl.find():
    print (doc)
    outColl.insert_one(doc)
    time.sleep(TIMEOUT)
        
client.close()
