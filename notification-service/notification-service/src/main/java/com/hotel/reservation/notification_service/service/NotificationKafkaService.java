package com.hotel.reservation.notification_service.service;

import com.google.gson.Gson;
import com.hotel.reservation.notification_service.entity.Notification;
import com.hotel.reservation.notification_service.model.NotificationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class NotificationKafkaService {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void listen(String message) {

        // Add logic to process the notification
        processNotification(message);
    }

    private void processNotification(String message) {

        Gson gson = new Gson();
        NotificationContext context = gson.fromJson(message, NotificationContext.class);
        notificationService.sendSimpleEmail(context.getContext().get("to"), context.getContext().get("sub"), context.getBody());
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setReferenceId(UUID.randomUUID());
        notification.setCreateDate(LocalDate.now());
        notificationService.add(notification);

    }
}