package com.hotel.reservation.auth_service.repository;

import com.hotel.reservation.auth_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}