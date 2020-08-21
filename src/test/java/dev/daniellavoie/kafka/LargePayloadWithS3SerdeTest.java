package dev.daniellavoie.kafka;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(classes = KafkaLargeMessageApplication.class, properties = "spring.profiles.active=s3-serde")
public class LargePayloadWithS3SerdeTest extends LargePayloadTest {
	@Test
	public void sendLargeMessage() throws InterruptedException, ExecutionException {
		super.sendLargeMessage();
	}
}