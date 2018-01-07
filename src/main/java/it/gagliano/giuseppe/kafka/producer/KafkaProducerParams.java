package it.gagliano.giuseppe.kafka.producer;

public interface KafkaProducerParams {

	// KAFKA CONFIG
	public static final String KAFKA_SERVER = "localhost:9092"; // for more servers "localhost:9092,localhost:9093,localhost:9094"
	public static final String TOPIC = "milof";

	// MONGODB CONFIG
	public static final String DATABASE = "datasets";
	public static final String COLLECTION = "current";
	public static final String MONGO_SERVER = "localhost:27017";
}
