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
	"--out",
	type=str,
	default="current",
	help="Output collection name."
)
parser.add_argument(
	"--db",
	type=str,
	default="datasets",
	help="Source database name."
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
parser.add_argument(
	"--server",
	type=str,
	default="localhost:27017",
	help="Mongo server IP:PORT."
)
args, unparsed = parser.parse_known_args()
system('title streamer')
client = MongoClient(args.server)
db = client[args.db]
srcColl = db[args.src]
outColl = db[args.out]
outColl.delete_many({})

docs = []
for doc in srcColl.find():
    if len(docs) == args.batch:
        for d in docs:
            print (d)
            outColl.insert_one(d)
        docs = []
        time.sleep(args.timeout)
    else:
        docs.append(doc)
        
client.close()
