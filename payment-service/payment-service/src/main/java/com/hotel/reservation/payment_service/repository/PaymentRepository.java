package com.hotel.reservation.payment_service.repository;

import com.hotel.reservation.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}