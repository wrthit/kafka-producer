package com.kafka.example.producer;

import com.kafka.example.producer.configuration.KafkaProducerConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = KafkaProducerConfiguration.class)
class ProducerApplicationTests {

	@Test
	void contextLoads() {
	}

}
