package com.hotel.reservation.payment_service.controller;

import com.hotel.reservation.payment_service.entity.Payment;
import com.hotel.reservation.payment_service.model.PaymentStatus;
import com.hotel.reservation.payment_service.model.PaymentType;
import com.hotel.reservation.payment_service.service.PaymentService;
import com.hotel.reservation.payment_service.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void processPaymentTest() throws Exception {
        // Given
        Payment payment = new Payment();
        payment.setCustomerId(1L);
        payment.setAmount(new BigDecimal("150.00"));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentType(PaymentType.PAY);

        when(paymentService.pay(any(Payment.class))).thenReturn(payment);

        // When & Then
        mockMvc.perform(post("/payments/pay")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isOk());
    }

    @Test
    void processRefundTest() throws Exception {
        // Given
        Payment payment = new Payment();
        payment.setCustomerId(1L);
        payment.setAmount(new BigDecimal("150.00"));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentType(PaymentType.REFUND);
        payment.setPaymentId(1L);

        when(paymentService.refund(anyLong())).thenReturn(payment);

        // When & Then
        mockMvc.perform(post("/payments/refund/{paymentId}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void processRefundNoDataFoundExceptionTest() throws Exception {
        // Given
        when(paymentService.refund(anyLong())).thenThrow(new NoDataFoundException("No data found"));

        // When & Then
        mockMvc.perform(post("/payments/refund/{paymentId}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }
}