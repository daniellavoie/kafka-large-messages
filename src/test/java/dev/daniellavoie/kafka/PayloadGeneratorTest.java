package dev.daniellavoie.kafka;

import org.junit.jupiter.api.Test;

public class PayloadGeneratorTest {
	
	@Test
	public void testGenerator() {
		System.out.println(new PayloadGenerator().generateString(1000));
	}
}
