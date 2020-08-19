package dev.daniellavoie.kafka;

public class PayloadGenerator {
	public String generateString(int sizeInKb) {
		int targetSize = ((sizeInKb * 1000));

		StringBuffer stringBuffer = new StringBuffer();
		do {
			stringBuffer.append(Math.random());
		} while (stringBuffer.length() < targetSize);

		return stringBuffer.toString();
	}
}
