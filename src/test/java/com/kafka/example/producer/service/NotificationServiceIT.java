package com.kafka.example.producer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kafka.example.producer.model.LogType;
import com.kafka.example.producer.model.Notification;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootTest
@EmbeddedKafka(topics = { "logging" }, partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092",
        "port=9092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotificationServiceIT {

    @Autowired
    private NotificationService notificationServiceMock;

    @Autowired
    private EmbeddedKafkaBroker kafkaEmbedded;

    private Consumer<String, Notification> consumer;

    @Value("${topic}")
    private String TOPIC;

    @BeforeAll
    public void setup() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", kafkaEmbedded);
        // Default is <Integer, String> so need to specify types. If don't specify, you
        // may see "data size received by IntegerDeserializer is not equal to 4" error.
        ConsumerFactory<String, Notification> cf = new DefaultKafkaConsumerFactory<String, Notification>(consumerProps,
                new StringDeserializer(), new JsonDeserializer<>(Notification.class));
        consumer = cf.createConsumer();
        kafkaEmbedded.consumeFromAnEmbeddedTopic(consumer, TOPIC);
    }

    @AfterAll
    public void teardown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    public void logNotificationSendsKafkaMessage() throws JsonMappingException, JsonProcessingException {
        Notification notification = Notification.builder()
                                                .service("some service name")
                                                .clazz("some class name")
                                                .message("this is a test")
                                                .type(LogType.INFO)
                                                .build();

        notificationServiceMock.logNotification(notification);

        ConsumerRecord<String, Notification> receivedNotification = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        assertNotNull(receivedNotification);

        Notification actualNotification = receivedNotification.value();
        assertEquals("some service name", actualNotification.getService());
        assertEquals("some class name", actualNotification.getClazz());
        assertEquals("this is a test", actualNotification.getMessage());
        assertEquals(LogType.INFO, actualNotification.getType());
    }
    
}
