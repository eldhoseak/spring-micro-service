package com.hotel.reservation.api_gateway.repository;

import com.hotel.reservation.api_gateway.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}