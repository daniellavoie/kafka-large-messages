package dev.daniellavoie.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest(classes = KafkaLargeMessageApplication.class, properties = "spring.profiles.active=s3-serde")
public abstract class LargePayloadTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(LargePayloadTest.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private LargePayloadTopic largePayloadTopic;

	@Autowired
	private Consumer<String, String> payloadConsumer;

	public void sendLargeMessage() throws InterruptedException, ExecutionException {
		payloadConsumer.subscribe(Arrays.asList(largePayloadTopic.getName()));

		do {
			LOGGER.info("Polling existing events from kafka topics.");
		} while (!payloadConsumer.poll(Duration.ofSeconds(1)).isEmpty());

		LOGGER.info("Generating a 10 mb message.");
		
		var largePayload = new PayloadGenerator().generateString(10 * 1000);

		LOGGER.info("Publishing the 10 mb message to Kafka.");
		
		kafkaTemplate.send(largePayloadTopic.getName(), largePayload).get();

		LOGGER.info("Polling the 10 mb message from Kafka.");
		
		ConsumerRecords<String, String> records = payloadConsumer.poll(Duration.ofMinutes(1));

		Assertions.assertNotEquals(0, records.count());
		
		LOGGER.info("Generating a 100 mb message.");

		var tooLargePayload = new PayloadGenerator().generateString(100 * 1000);
		
		LOGGER.info("Publishing the 100 mb message to Kafka.");

		kafkaTemplate.send(largePayloadTopic.getName(), tooLargePayload).get();
		
		LOGGER.info("Polling the 100 mb message from Kafka.");
		
		records = payloadConsumer.poll(Duration.ofMinutes(1));

		Assertions.assertNotEquals(0, records.count());
		
		LOGGER.info("Successfully polled all messages.");
	}
}
