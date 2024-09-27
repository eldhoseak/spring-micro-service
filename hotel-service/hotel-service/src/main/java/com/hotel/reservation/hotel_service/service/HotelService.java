package com.hotel.reservation.hotel_service.service;

import com.hotel.reservation.hotel_service.entity.HotelRoom;
import com.hotel.reservation.hotel_service.exception.NoDataFoundException;
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
		log.debug("Fetching all hotel rooms.");
		List<HotelRoom> rooms = hotelRoomRepository.findAll();
		log.debug("Returning {} hotel rooms.", rooms.size());
		return rooms;
	}

	public HotelRoom getRoomDetails(Long roomId) throws NoDataFoundException {
		log.debug("Fetching details for room id: {}", roomId);
		Optional<HotelRoom> roomDetails = hotelRoomRepository.findById(roomId);
		HotelRoom room = roomDetails.orElseThrow(() -> new NoDataFoundException("Invalid room id"));
		log.debug("Returning details for room id: {}", roomId);
		return room;
	}
}