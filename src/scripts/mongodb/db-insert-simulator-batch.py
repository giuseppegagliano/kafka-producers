from pymongo import MongoClient
import time
from os import system
import argparse

parser = argparse.ArgumentParser()
parser.add_argument(
	"--src",
	type=str,
	default="iris",
	help="Source collection name."
)
parser.add_argument(
	"--timeout",
	type=int,
	default="3",
	help="Inter-insertion time."
)
parser.add_argument(
	"--batch",
	type=int,
	default="3",
	help="Number of lines to insert."
)
args, unparsed = parser.parse_known_args()


system('title streamer')
MONGO_SERV = 'localhost:27017'
SRC_COLLECTION = args.src
OUT_COLLECTION = 'current'
TIMEOUT = args.timeout     # number of seconds
BATCH_SIZE = args.batch

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
