package it.gagliano.giuseppe.kafka.producer;

import java.util.Properties;

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

public class MongoProducer {

	private static KafkaProducer<Integer, String> producer = null;
	// private static final Boolean isAsync = true;
	private static int messageNo = 1;
	private static String messageStr = "";
	private static long startTime = System.currentTimeMillis();

	public static void main(String[] args) {

		Block<ChangeStreamDocument<Document>> printBlock = new Block<ChangeStreamDocument<Document>>() {
			@Override
			public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {
				try {
					messageStr = changeStreamDocument.getFullDocument().toJson();
					producer.send(new ProducerRecord<>(KafkaProducerParams.TOPIC, messageNo, messageStr),
							new DemoCallBack(startTime, messageNo, messageStr));

					System.out.println("Sent " + changeStreamDocument);
					++messageNo;
				} catch(NullPointerException e) {}
			}
		};
		producer = createProducer();
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + KafkaProducerParams.MONGO_SERVER));
		MongoDatabase database = mongoClient.getDatabase(KafkaProducerParams.DATABASE);
		MongoCollection<Document> collection = database.getCollection(KafkaProducerParams.COLLECTION);
		collection.watch().forEach(printBlock);
		mongoClient.close();
	}

	public static KafkaProducer<Integer, String> createProducer() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", KafkaProducerParams.KAFKA_SERVER);
		// properties.put("client.id", CLIENT_ID);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return new KafkaProducer<>(properties);
	}
}

class DemoCallBack implements Callback {

//	private final long startTime;
//	private final int key;
//	private final String message;

	public DemoCallBack(long startTime, int key, String message) {
//		this.startTime = startTime;
//		this.key = key;
//		this.message = message;
	}

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
//		long elapsedTime = System.currentTimeMillis() - startTime;
		if (metadata != null) {
			// System.out.println(
			// "message(" + key + ", " + message + ") sent to partition(" +
			// metadata.partition() +
			// "), " +
			// "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
		} else {
			exception.printStackTrace();
		}
	}
}
