package com.hotel.reservation.hotel_service.controller;

import com.hotel.reservation.hotel_service.entity.HotelRoom;
import com.hotel.reservation.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rooms")
@RequiredArgsConstructor
public class HotelController {

	private final HotelService hotelService;

	@GetMapping("/{id}")
	public HotelRoom getHotelById(@PathVariable Long id) {
		return hotelService.getRoomDetails(id);
	}
	@GetMapping
	public List<HotelRoom> getAllRooms() {
		return hotelService.getAllRooms();
	}
}