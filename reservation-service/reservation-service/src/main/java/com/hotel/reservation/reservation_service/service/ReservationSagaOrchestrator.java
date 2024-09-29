package com.hotel.reservation.reservation_service.service;

import com.hotel.reservation.reservation_service.entity.Reservation;
import com.hotel.reservation.reservation_service.exception.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReservationSagaOrchestrator {

    public void initiateReservationSaga(ReservationService reservationService, Reservation reservation) throws Exception {
        try {
            reservationService.reserveRoom(reservation);
            reservationService.processReservationPayment(reservation);
            reservationService.finalizeReservation(reservation);
            reservationService.sendReservationNotification(reservation);
            log.info("Reservation successfully completed.");
        } catch (Exception e) {
            log.error("Error occurred: {}, initiating compensation.", e.getMessage());
            compensateReservation(reservationService, reservation);
            throw e; // Propagate the exception
        }
    }

    public void compensateReservation(ReservationService reservationService, Reservation reservation) throws NoDataFoundException {
        if (reservation.getPaymentId() != null) {
            reservationService.refundPayment(reservation);
        }
        if (reservation.getReservationId() != null) {
            reservationService.cancelRoomReservation(reservation);
            reservationService.sendCancellationNotification(reservation);
        }
    }
}