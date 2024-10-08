package com.hotel.reservation.notification_service.controller;

import com.hotel.reservation.notification_service.entity.Notification;
import com.hotel.reservation.notification_service.exception.NoDataFoundException;
import com.hotel.reservation.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<Notification> getAll() {
        return notificationService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Notification getById(@PathVariable("id") Long notificationId) throws NoDataFoundException {
        return notificationService.getById(notificationId);
    }

}