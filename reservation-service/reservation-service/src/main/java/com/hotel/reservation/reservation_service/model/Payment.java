package com.hotel.reservation.reservation_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    private Long paymentId;

    private Long customerId;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private PaymentType paymentType;

}