package com.hotel.reservation.hotel_service.service;

import com.hotel.reservation.hotel_service.entity.HotelRoom;
import com.hotel.reservation.hotel_service.repository.HotelRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelService {
	@Autowired
	private HotelRoomRepository hotelRoomRepository;

	public List<HotelRoom> getAllRooms() {
		return hotelRoomRepository.findAll();
	}

	public HotelRoom getRoomDetails(Long roomId) {
		Optional<HotelRoom> roomDetails = hotelRoomRepository.findById(roomId);
		return roomDetails.orElse(null);
	}
}