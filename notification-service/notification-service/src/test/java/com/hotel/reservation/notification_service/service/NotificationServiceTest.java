package com.hotel.reservation.notification_service.service;

import com.hotel.reservation.notification_service.entity.Notification;
import com.hotel.reservation.notification_service.exception.NoDataFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.hotel.reservation.notification_service.repository.NotificationRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    public void testGetByIdWhenNotificationExists() throws NoDataFoundException {
        Notification notification = new Notification();
        notification.setNotificationId(1L);
        Mockito.when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Notification result = notificationService.getById(1L);
        assertEquals(notification, result);
    }

    @Test
    public void testGetByIdWhenNotificationDoesNotExist() {
        MockitoAnnotations.openMocks(this);
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoDataFoundException.class, () -> notificationService.getById(1L));
    }
}
