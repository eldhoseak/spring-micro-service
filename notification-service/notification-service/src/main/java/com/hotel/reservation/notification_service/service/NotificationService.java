package com.hotel.reservation.notification_service.service;


import com.hotel.reservation.notification_service.entity.Notification;
import com.hotel.reservation.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	private final NotificationRepository notificationRepository;

	private final MailSender mailSender;

	public List<Notification> getAll() {
		return notificationRepository.findAll();
	}

	public Notification getById(UUID id) {
		return notificationRepository.findByReferenceId(id);
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