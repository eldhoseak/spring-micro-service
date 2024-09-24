package com.hotel.reservation.reservation_service.controller;

import com.hotel.reservation.reservation_service.entity.Reservation;
import com.hotel.reservation.reservation_service.exception.NoDataFoundException;
import com.hotel.reservation.reservation_service.repository.ReservationRepository;
import com.hotel.reservation.reservation_service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("reservations")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	private final ReservationRepository reservationRepository;

	@GetMapping("/{id}")
	public Reservation getReservationById(@PathVariable Long id) {
		return reservationRepository.getReferenceById(id);
	}

	@PostMapping("/make-reservation")
	public Reservation makeReservation(@RequestBody Reservation reservation) throws Exception {
		return reservationService.makeReservation(reservation);
	}

	/*@PostMapping("/update-reservation")
	public Reservation updateReservation(@RequestBody Reservation reservation) {
		return reservationService.updateReservation(reservation);
	}*/

	@GetMapping("/cancel-reservation/{id}")
	public String cancelReservation(@PathVariable("id") Long reservationId) throws NoDataFoundException {
		reservationService.cancelReservation(reservationId);
		return "Cancellation completed. Refund initiated";
	}

	@GetMapping("/customer/{id}")
	public List<Reservation> getReservationForCustomer(@PathVariable("id") Long customerId) {
		return reservationRepository.findByCustomerId(customerId);
	}

}