package com.hotel.reservation.hotel_service.entity;

import com.hotel.reservation.hotel_service.model.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotel_room")
public class HotelRoom implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Room_Id")
	private Long roomId;

	@Column(name = "rent")
	private BigDecimal rent;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name ="Room_Type" )
	private RoomType roomType;
}