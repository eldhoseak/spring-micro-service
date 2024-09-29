package com.hotel.reservation.hotel_service.controller;

import com.hotel.reservation.hotel_service.entity.HotelRoom;
import com.hotel.reservation.hotel_service.exception.NoDataFoundException;
import com.hotel.reservation.hotel_service.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Test
    public void testGetHotelById_Success() throws Exception {
        HotelRoom room = HotelRoom.builder()
                .roomId(1L)
                .rent(BigDecimal.valueOf(100.00))
                .build();
        when(hotelService.getRoomDetails(1L)).thenReturn(room);
        mockMvc.perform(get("/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId", is(room.getRoomId().intValue())))
                .andExpect(jsonPath("$.rent", comparesEqualTo(100.0)));
    }

    @Test
    public void testGetHotelById_NotFound() throws Exception {
        when(hotelService.getRoomDetails(1L)).thenThrow(NoDataFoundException.class);
        mockMvc.perform(get("/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}