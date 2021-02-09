package com.kafka.example.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.example.producer.model.Notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "logging";

    public NotificationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void logNotification(Notification notification) {
        String notificationAsString;
        
        try {
            notificationAsString = new ObjectMapper().writeValueAsString(notification);
            kafkaTemplate.send(TOPIC, notificationAsString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
