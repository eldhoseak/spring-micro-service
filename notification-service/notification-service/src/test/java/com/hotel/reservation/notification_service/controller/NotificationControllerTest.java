package com.hotel.reservation.notification_service.controller;

import com.hotel.reservation.notification_service.entity.Notification;
import com.hotel.reservation.notification_service.exception.NoDataFoundException;
import com.hotel.reservation.notification_service.service.NotificationService;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    public void testGetByIdValidId() throws Exception {

        Notification n = new Notification();
        n.setNotificationId(1L);
        n.setMessage("Test Message");
        n.setCreateDate(LocalDate.now());

        when(notificationService.getById(1L)).thenReturn(n);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/notifications/{id}", 1L)).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status, "Expected HTTP status 200");
        verify(notificationService, times(1)).getById(1L);
    }

    @Test
    public void testGetByIdInvalidId() throws Exception {

        when(notificationService.getById(10L)).thenThrow(NoDataFoundException.class);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/notifications/{id}", 10L)).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(404, status, "Expected HTTP status 404");
        verify(notificationService, times(1)).getById(10L);
    }
}