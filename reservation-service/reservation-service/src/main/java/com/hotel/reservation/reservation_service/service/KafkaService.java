package com.hotel.reservation.reservation_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    // Method to send messages without a key
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}