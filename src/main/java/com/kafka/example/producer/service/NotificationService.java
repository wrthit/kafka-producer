package com.kafka.example.producer.service;

import com.kafka.example.producer.model.Notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private KafkaTemplate<String, Notification> kafkaTemplate;
    private static final String TOPIC = "logging";

    public NotificationService(KafkaTemplate<String, Notification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void logNotification(Notification notification) {
        kafkaTemplate.send(TOPIC, notification);
    }
}
