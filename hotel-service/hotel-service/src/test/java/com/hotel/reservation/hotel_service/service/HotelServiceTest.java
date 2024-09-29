package com.hotel.reservation.hotel_service.service;

import com.hotel.reservation.hotel_service.entity.HotelRoom;
import com.hotel.reservation.hotel_service.exception.NoDataFoundException;
import com.hotel.reservation.hotel_service.model.RoomType;
import com.hotel.reservation.hotel_service.repository.HotelRoomRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
class HotelServiceTest {

    @Mock
    private HotelRoomRepository hotelRoomRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    public void givenValidRoomId_whenGetRoomDetails_thenReturnHotelRoom() throws NoDataFoundException {
        HotelRoom mockRoom = HotelRoom.builder()
                .roomId(1L)
                .rent(BigDecimal.valueOf(200))
                .roomType(RoomType.DELUXE)
                .build();

        when(hotelRoomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        HotelRoom hotelRoom = hotelService.getRoomDetails(1L);

        assertEquals(1L, hotelRoom.getRoomId());
        assertEquals(200, hotelRoom.getRent().doubleValue());
        assertEquals(RoomType.DELUXE, hotelRoom.getRoomType());
    }

    @Test
    public void givenInvalidRoomId_whenGetRoomDetails_thenThrowNoDataFoundException() {
        when(hotelRoomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoDataFoundException.class, () -> hotelService.getRoomDetails(1L));
    }
}