package com.hotel.reservation.notification_service.repository;

import com.hotel.reservation.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Notification findByReferenceId(UUID referenceId);

}