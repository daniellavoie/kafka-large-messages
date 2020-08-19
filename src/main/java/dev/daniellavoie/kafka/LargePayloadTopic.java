package dev.daniellavoie.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("topics.large-payload-test")
public class LargePayloadTopic extends TopicConfiguration {

}
