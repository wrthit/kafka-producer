package com.kafka.example.producer.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.example.producer.model.LogType;
import com.kafka.example.producer.model.Notification;
import com.kafka.example.producer.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationController.class)
public class NotificationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    NotificationService notificationServiceMock;

    @Test
    public void postNotificationCallsService() throws JsonProcessingException, Exception {
        Notification notification = Notification.builder()
                                                .service("some service name")
                                                .clazz("some class name")
                                                .message("this is a test")
                                                .type(LogType.INFO)
                                                .build();
        
        doNothing().when(notificationServiceMock).logNotification(notification);

        mockMvc.perform(post("/notification/log")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(new ObjectMapper().writeValueAsString(notification)))
            .andExpect(status().isOk())
            .andReturn();
        
        verify(notificationServiceMock).logNotification(notification);
        verifyNoMoreInteractions(notificationServiceMock);
    }
}
