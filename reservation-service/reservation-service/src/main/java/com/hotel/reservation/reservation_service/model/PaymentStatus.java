package com.hotel.reservation.reservation_service.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentStatus {

	FAILED("FAILED"), SUCCESS("SUCCESS");

	private final String name;

	public String toString() {
		return this.name;
	}

}