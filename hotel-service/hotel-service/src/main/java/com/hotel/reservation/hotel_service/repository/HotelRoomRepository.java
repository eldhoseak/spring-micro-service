package com.hotel.reservation.hotel_service.repository;

import com.hotel.reservation.hotel_service.entity.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long> {

}