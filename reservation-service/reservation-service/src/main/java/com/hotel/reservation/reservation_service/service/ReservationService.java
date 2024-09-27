package com.hotel.reservation.reservation_service.service;

import com.google.gson.Gson;
import com.hotel.reservation.reservation_service.entity.Reservation;
import com.hotel.reservation.reservation_service.exception.NoDataFoundException;
import com.hotel.reservation.reservation_service.model.*;
import com.hotel.reservation.reservation_service.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final HotelService hotelService;
    private final PaymentService paymentService;
    private final CustomerService customerService;
    private final KafkaService kafkaService;
    private final ReservationSagaOrchestrator sagaOrchestrator;

    public Reservation makeReservation(Reservation reservation) throws Exception {
        sagaOrchestrator.initiateReservationSaga(this, reservation);
        return reservation;
    }


    public Boolean cancelReservation(Long reservationId) throws Exception {
        sagaOrchestrator.compensateReservation(this, reservationRepository.findById(reservationId).get());
        return true;
    }

    public void reserveRoom(Reservation reservation) throws Exception {
        HotelRoom hotelRoom = hotelService.getRoom(reservation.getRoomId()).block();
        if (hotelRoom == null) {
            throw new NoDataFoundException("Requested hotel room is no more available.");
        }

        List<Reservation> reservations = reservationRepository.findByRoomIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(reservation.getRoomId(),
                reservation.getStartDate(), reservation.getEndDate());
        if (!reservations.isEmpty()) {
            throw new Exception("Other reservation exists for the selected room in this period.");
        }

        reservation.setRentRatePerNight(hotelRoom.getRent());
        BigDecimal totalRent = calculateTotalRent(reservation);
        reservation.setTotalRent(totalRent);
    }

    public void processReservationPayment(Reservation reservation) throws Exception {
        Payment payment = paymentService.processPayment(Payment.builder()
                .customerId(reservation.getCustomerId())
                .amount(reservation.getTotalRent())
                .paymentType(PaymentType.PAY)
                .build()).block();

        if (payment == null || !PaymentStatus.SUCCESS.equals(payment.getPaymentStatus())) {
            throw new NoDataFoundException("Payment failed.");
        }

        reservation.setPaymentStatus(payment.getPaymentStatus());
        reservation.setPaymentId(payment.getPaymentId());
    }

    public void finalizeReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public void sendReservationNotification(Reservation reservation) throws NoDataFoundException {
        Customer customer = customerService.getCustomerDetails(reservation.getCustomerId()).block();
        if (customer == null) {
            throw new NoDataFoundException("Invalid customer id #" + reservation.getCustomerId());
        }

        NotificationContext nc = new NotificationContext();
        String confirmationMessageTemplate = "Thanks for choosing us as your comfort partner.\n" +
                "Your booking for room no %s is confirmed starting %s and ending %s.\n" +
                "Why don’t you follow us on [social media] as well?\n" +
                "-Great Comfort Hotels\n";

        String confirmationMessage = String.format(confirmationMessageTemplate,
                reservation.getRoomId(),
                reservation.getStartDate(),
                reservation.getEndDate());

        nc.setBody(confirmationMessage);
        nc.setType("email");
        nc.setSeverity("Low");
        nc.setCreatedAt(new Date());
        Map<String, String> context = new HashMap<>();
        context.put("to", customer.getEmail());
        context.put("sub", String.format("Reservation Confirmed [#%s]", reservation.getReservationId()));
        nc.setContext(context);

        kafkaService.sendMessage("notification-topic", new Gson().toJson(nc));
    }

    public void cancelRoomReservation(Reservation reservation) {
        reservationRepository.deleteById(reservation.getReservationId());
    }

    public void refundPayment(Reservation reservation) {
        paymentService.processRefund(reservation.getPaymentId()).block();
    }

    private BigDecimal calculateTotalRent(Reservation reservation) {
        Long daysOfStay = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        return reservation.getRentRatePerNight().multiply(BigDecimal.valueOf(daysOfStay));
    }

    public void sendCancellationNotification(Reservation reservation) throws NoDataFoundException {
        Customer customer = customerService.getCustomerDetails(reservation.getCustomerId()).block();
        if (customer == null) {
            throw new NoDataFoundException("Invalid customer id #" + reservation.getCustomerId());
        }

        NotificationContext nc = new NotificationContext();
        String body = String.format("Your booking ref %s has been cancelled and payment refund initiated with ref %s. Wish to see you soon again!\n-Great Comfort Hotels",
                reservation.getReservationId(), reservation.getPaymentId());
        nc.setBody(body);
        nc.setType("email");
        nc.setSeverity("Low");
        nc.setCreatedAt(new Date());
        Map<String, String> context = new HashMap<>();
        context.put("to", customer.getEmail());
        context.put("sub", String.format("Reservation Cancellation [#%s]", reservation.getReservationId()));
        nc.setContext(context);

        kafkaService.sendMessage("notification-topic", new Gson().toJson(nc));
    }
}