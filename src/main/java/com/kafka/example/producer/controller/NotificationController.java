package com.kafka.example.producer.controller;

import com.kafka.example.producer.model.Notification;
import com.kafka.example.producer.service.NotificationService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/notification")
public class NotificationController {
    
    private NotificationService notificationService;

    public NotificationController (NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping(value = "/log")
    public void logNotification(@RequestBody Notification notification) {
        notificationService.logNotification(notification);
    }
}
