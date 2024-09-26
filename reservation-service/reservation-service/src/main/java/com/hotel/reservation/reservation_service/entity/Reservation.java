package com.hotel.reservation.reservation_service.entity;

import com.hotel.reservation.reservation_service.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RESERVATIONS")
public class Reservation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_Id")
	private Long reservationId;

	@Column(name = "customer_Id")
	private Long customerId;

	@Column(name = "room_Id")
	private Long roomId;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "payment_id")
	private Long paymentId;

	@Column(name = "total_rent")
	private BigDecimal totalRent;

	@Column(name = "payment_status")
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	@Transient
	private BigDecimal rentRatePerNight;

}