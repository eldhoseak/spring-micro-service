package com.hotel.reservation.reservation_service.service;

import com.hotel.reservation.reservation_service.entity.Reservation;
import com.hotel.reservation.reservation_service.exception.NoDataFoundException;
import com.hotel.reservation.reservation_service.model.*;
import com.hotel.reservation.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

	@Autowired
	private final ReservationRepository reservationRepository;

	@Autowired
	private final HotelService hotelService;

	@Autowired
	private final PaymentService paymentService;

	@Autowired
	private final CustomerService customerService;

	@Autowired
	private KafkaService kafkaService;

	public Reservation makeReservation(Reservation reservation) throws Exception {
		HotelRoom hotelRoom = hotelService.getRoom(reservation.getRoomId()).block();

		if (hotelRoom != null) {
			List<Reservation> reservations = reservationRepository.findByRoomIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(reservation.getRoomId(),
					reservation.getStartDate(), reservation.getEndDate());
			if(!CollectionUtils.isEmpty(reservations)) {
				throw new Exception("Other reservation exist for the selected room in this period.");
			}

			reservation.setRentRatePerNight(hotelRoom.getRent());
			BigDecimal totalRent = calculateTotalRent(reservation);
			reservation.setTotalRent(totalRent);

			Payment payment = paymentService.processPayment(Payment.builder()
				.customerId(reservation.getCustomerId())
				.amount(totalRent)
				.paymentType(PaymentType.PAY)
				.build()).block();
			if (payment != null) {
				reservation.setPaymentStatus(payment.getPaymentStatus());
				reservation.setPaymentId(payment.getPaymentId());
				if (PaymentStatus.SUCCESS.equals(payment.getPaymentStatus())) {
					reservation = reservationRepository.save(reservation);
					sendReservationNotification(reservation);
					return reservation;
				}
			}
			else {
				throw new NoDataFoundException("Payment failed.");
			}
		}
		throw new NoDataFoundException("Requested hotel room is no more available.");
	}

	private BigDecimal calculateTotalRent(Reservation reservation) {
		Long daysOfStay = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
		return reservation.getRentRatePerNight().multiply(BigDecimal.valueOf(daysOfStay));
	}


	public void cancelReservation(Long reservationId) throws NoDataFoundException {
		Optional<Reservation> reservation = reservationRepository.findById(reservationId);
		if (reservation.isPresent()) {
			initiatePaymentRefund(reservation.get().getPaymentId());
			reservationRepository.deleteById(reservationId);
		}
		else {
			throw new NoDataFoundException("Invalid registration id to process");
		}
	}

	private Payment initiatePaymentRefund(Long paymentId) {
		return paymentService.processRefund(paymentId).block();
	}

	public void sendReservationNotification(Reservation reservation) throws NoDataFoundException {
		Customer customer = customerService.getCustomerDetails(reservation.getCustomerId()).block();
		if (customer != null) {
			NotificationContext nc = new NotificationContext();

			String confirmationMessageTemplate = "Thanks for choosing us as your comfort partner.\n" +
					"Your booking for room no %s is confirmed starting %s and ending %s.\n" +
					"Why don’t you follow us on [social media] as well?\n" +
					"-Great Comfort Hotels\n";

			String confirmationMessage = String.format(confirmationMessageTemplate,
					reservation.getRoomId(),
					reservation.getStartDate(),
					reservation.getEndDate()
			);

			nc.setBody(confirmationMessage);
			nc.setType("email");
			nc.setSeverity("Low");
			nc.setCreatedAt(new Date());
			Map<String, String> context = new HashMap<>();
			context.put("to", customer.getEmail());
			context.put("sub", String.format("Reservation Confirmed [#%s]", reservation.getReservationId()));
			nc.setContext(context);
			Gson gson = new Gson();
			kafkaService.sendMessage("notification-topic",gson.toJson(nc));
		}
		else {
			throw new NoDataFoundException(
					"Reservation cancellation failed due to invalid customer id #" + reservation.getCustomerId());
		}
	}

	public void sendCancellationNotification(Reservation reservation) throws NoDataFoundException {
		Customer customer = customerService.getCustomerDetails(reservation.getCustomerId()).block();
		if (customer != null) {
			NotificationContext nc = new NotificationContext();
			nc.setBody(String.format(
					"Your booking ref %s has been cancelled and payment refund initiated with ref %s."
							+ "Wish to see you soon again!\n-Great Comfort Hotels",
					reservation.getReservationId(), reservation.getPaymentId()));
			nc.setType("email");
			nc.setSeverity("Low");
			nc.setCreatedAt(new Date());
			Map<String, String> context = new HashMap<>();
			context.put("to", customer.getEmail());
			context.put("sub", String.format("Reservation Cancellation [#%s]", reservation.getReservationId()));
			nc.setContext(context);
			Gson gson = new Gson();
			kafkaService.sendMessage("notification-topic",gson.toJson(nc));
		}
		else {
			throw new NoDataFoundException(
					"Reservation cancellation failed due to invalid customer id #" + reservation.getCustomerId());
		}
	}



}