package com.hotel.reservation.reservation_service.repository;

import com.hotel.reservation.reservation_service.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCustomerId(Long customerId);

    List<Reservation> findByRoomIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(Long roomId, LocalDate startDate, LocalDate endDate);

}