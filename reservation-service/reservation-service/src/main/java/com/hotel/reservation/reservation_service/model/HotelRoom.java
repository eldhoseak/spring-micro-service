package com.hotel.reservation.reservation_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelRoom {
    private Long roomId;
    private BigDecimal rent;
    private com.hotel.reservation.reservation_service.model.RoomType roomType;
}