package dev.daniellavoie.kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);

	@Bean
	public Consumer<String, String> payloadConsumer(LargePayloadTopic largePayloadTopic,
			KafkaProperties kafkaProperties, @Value("${spring.kafka.client-id}") String clientId) {
		createTopicIfMissing(largePayloadTopic, AdminClient.create(kafkaProperties.buildAdminProperties()));

//		Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();
//		consumerProperties.put("client.id", clientId);
		
		return new DefaultKafkaConsumerFactory<String, String>(kafkaProperties.buildConsumerProperties())
				.createConsumer();
	}

	@Bean
	public KafkaTemplate<String, String> payloadTemplate(KafkaProperties kafkaProperties) {
		return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties()));
	}

	private void createTopicIfMissing(TopicConfiguration topicConfiguration, AdminClient adminClient) {
		try {
			if (!adminClient.listTopics().names().get().stream()
					.filter(existingTopic -> existingTopic.equals(topicConfiguration.getName())).findAny()
					.isPresent()) {
				LOGGER.info("Creating topic {}.", topicConfiguration.getName());

				NewTopic topic = new NewTopic(topicConfiguration.getName(), topicConfiguration.getPartitions(),
						topicConfiguration.getReplicationFactor());

				topic.configs(new HashMap<>());
				if (topicConfiguration.isCompacted()) {
					topic.configs().put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);
				}
				adminClient.createTopics(Arrays.asList(topic)).all().get();
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
