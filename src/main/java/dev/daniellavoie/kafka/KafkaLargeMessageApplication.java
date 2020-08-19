package dev.daniellavoie.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaLargeMessageApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaLargeMessageApplication.class, args);
	}
}
