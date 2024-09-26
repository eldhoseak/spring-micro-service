package com.hotel.reservation.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationContext {
    private String body;
    private String type;
    private String severity;
    private Date createdAt;
    private Map<String, String> context;
}