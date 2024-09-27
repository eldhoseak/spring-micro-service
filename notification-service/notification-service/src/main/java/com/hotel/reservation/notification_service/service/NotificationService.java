package com.hotel.reservation.notification_service.service;


import com.hotel.reservation.notification_service.entity.Notification;
import com.hotel.reservation.notification_service.exception.NoDataFoundException;
import com.hotel.reservation.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    private final MailSender mailSender;

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public Notification getById(Long notificationId) throws NoDataFoundException {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isEmpty()) {
            throw new NoDataFoundException("No data found for notification id: " + notificationId);
        } else {
            return notification.get();
        }
    }

    public Notification add(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@myhotel.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}