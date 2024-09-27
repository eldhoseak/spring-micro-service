package com.hotel.reservation.payment_service.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentType {

    PAY("PAY"), REFUND("REFUND");

    private final String name;

    public String toString() {
        return this.name;
    }

}