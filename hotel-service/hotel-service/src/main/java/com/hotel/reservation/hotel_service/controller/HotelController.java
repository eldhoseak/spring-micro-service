package com.hotel.reservation.hotel_service.controller;

import com.hotel.reservation.hotel_service.entity.HotelRoom;
import com.hotel.reservation.hotel_service.exception.NoDataFoundException;
import com.hotel.reservation.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("rooms")
@RequiredArgsConstructor

@Slf4j
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/{id}")
    public HotelRoom getHotelById(@PathVariable Long id) throws NoDataFoundException {
        log.debug("Fetching hotel room with ID: {}", id);
        return hotelService.getRoomDetails(id);
    }

    @GetMapping
    public List<HotelRoom> getAllRooms() {
        log.debug("Fetching all hotel rooms");
        return hotelService.getAllRooms();
    }
}

