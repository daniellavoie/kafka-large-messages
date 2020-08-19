package dev.daniellavoie.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest(classes = KafkaLargeMessageApplication.class)
public class LargePayloadWithDefaultSettingsTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(LargePayloadWithDefaultSettingsTest.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private LargePayloadTopic largePayloadTopic;

	@Autowired
	private Consumer<String, String> payloadConsumer;

	@Test
	public void sendLargeMessage() throws InterruptedException, ExecutionException {
		payloadConsumer.subscribe(Arrays.asList(largePayloadTopic.getName()));
		do {
			LOGGER.info("Polling existing events from kafka topics.");
		} while (!payloadConsumer.poll(Duration.ofSeconds(1)).isEmpty());

		var largePayload = new PayloadGenerator().generateString(1 * 10000);

		kafkaTemplate.send(largePayloadTopic.getName(), largePayload).get();

		ConsumerRecords<String, String> records = payloadConsumer.poll(Duration.ofSeconds(10));

		Assertions.assertEquals(1, records.count());

		var tooLargePayload = new PayloadGenerator().generateString(1 * 100000);

		Assertions.assertThrows(KafkaException.class,
				() -> kafkaTemplate.send(largePayloadTopic.getName(), tooLargePayload).get());
	}
}
