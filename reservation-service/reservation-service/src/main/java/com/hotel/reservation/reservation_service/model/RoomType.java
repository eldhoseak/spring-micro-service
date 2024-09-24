package com.hotel.reservation.reservation_service.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoomType {

	SINGLE("1 Single Bed"), DOUBLE("1 Double Bed"),
	DELUXE("1 Queen Bed");

	private final String name;

	public String toString() {
		return this.name;
	}

}