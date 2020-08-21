package dev.daniellavoie.kafka;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(classes = KafkaLargeMessageApplication.class, properties = "spring.profiles.active=oversized-broker")
public class LargePayloadWithOversizedBrokerTest extends LargePayloadTest {
	@Test
	public void sendLargeMessage() throws InterruptedException, ExecutionException {
		Assertions.assertThrows(ExecutionException.class, () -> super.sendLargeMessage());
	}
}
