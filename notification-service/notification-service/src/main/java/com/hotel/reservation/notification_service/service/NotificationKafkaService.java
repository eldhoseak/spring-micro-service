package com.hotel.reservation.notification_service.service;

import com.google.gson.Gson;
import com.hotel.reservation.notification_service.model.NotificationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationKafkaService {

    @Autowired
    private NotificationService notificationService;

    // Replace "your-topic" with the name of your Kafka topic.
    @KafkaListener(topics = "your-topic", groupId = "notification-group")
    public void listen(String message) {
        // Handle the received message
        System.out.println("Received message: " + message);

        // Add logic to process the notification
        processNotification(message);
    }

    private void processNotification(String message) {
        // Add your custom processing logic here
        System.out.println("Processing notification: " + message);

        Gson gson = new  Gson();
        NotificationContext context = gson.fromJson(message, NotificationContext.class);
        notificationService.sendSimpleEmail(context.getTo(),"test",context.getBody());
    }
}