package it.gagliano.giuseppe.kafka.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class MongoProducer {

	private static KafkaProducer<Integer, String> producer = null;
	private static int messageNo = 1;
	private static String messageStr = "";
	private static long startTime = System.currentTimeMillis();
	public static Map<String, String> conf = new HashMap<String, String>(){{
		put("topic","milof");
		put("mongo-server","localhost:27017");
		put("kafka-server","localhost:9092");
		put("database","datasets");
		put("collection","current");
		}};

	public static void main(String[] args) {
        parseArgs(args);
        
		Block<ChangeStreamDocument<Document>> printBlock = new Block<ChangeStreamDocument<Document>>() {
			@Override
			public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {
				try {
					messageStr = changeStreamDocument.getFullDocument().toJson();
					producer.send(new ProducerRecord<>(conf.get("topic"), messageNo, messageStr),
							new DemoCallBack(startTime, messageNo, messageStr));

					System.out.println("Sent " + changeStreamDocument);
					++messageNo;
				} catch(NullPointerException e) {}
			}
		};
		producer = createProducer();
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + conf.get("mongo-server")));
		MongoDatabase database = mongoClient.getDatabase(conf.get("database"));
		MongoCollection<Document> collection = database.getCollection(conf.get("collection"));
		collection.watch().forEach(printBlock);
		mongoClient.close();
	}

	public static void parseArgs(String[] args) {
		ArgumentParser parser = ArgumentParsers.newFor("MongoProducer").build()
                .defaultHelp(true)
                .description("Watches a MongoDB collection and streams through "
                		+ "Kafka its changes");
        parser.addArgument("-t", "--topic")
                .setDefault("milof")
                .help("Specify the topic where to stream");
        parser.addArgument("-k", "--kafka-server")
		        .setDefault("localhost:9092")
		        .help("Specify the Kafka server");
        parser.addArgument("-m", "--mongo-server")
		        .setDefault("localhost:27017")
		        .help("Specify the Mongo server");
        parser.addArgument("-d", "--database")
		        .setDefault("datasets")
		        .help("Specify the database where to find the collection");
        parser.addArgument("-c", "--collection")
		        .setDefault("current")
		        .help("Specify the collection to watch");
    	Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        conf.put("collection", ns.getString("collection"));
        conf.put("database", ns.getString("database"));
        conf.put("kafka-server", ns.getString("kafka_server"));
        conf.put("mongo-server", ns.getString("mongo_server"));
        conf.put("topic", ns.getString("topic"));
	}
	
	public static KafkaProducer<Integer, String> createProducer() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", conf.get("kafka-server"));
		properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return new KafkaProducer<>(properties);
	}
	
}

class DemoCallBack implements Callback {

	public DemoCallBack(long startTime, int key, String message) {}

	/**
	 * onCompletion method will be called when the record sent to the Kafka Server
	 * has been acknowledged.
	 * 
	 * @param metadata
	 *            The metadata contains the partition and offset of the record. Null
	 *            if an error occurred.
	 * @param exception
	 *            The exception thrown during processing of this record. Null if no
	 *            error occurred.
	 */
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		if (metadata == null)	exception.printStackTrace();
	}
}
