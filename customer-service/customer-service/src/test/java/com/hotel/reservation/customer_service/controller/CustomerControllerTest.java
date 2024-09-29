package com.hotel.reservation.customer_service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hotel.reservation.customer_service.entity.Customer;
import com.hotel.reservation.customer_service.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private CustomerController customerController;

    @MockBean
    private CustomerService customerService;

    @Test
    void testGetCustomerById_found() {
        Customer c = new Customer();
        c.setId(1L);

        when(customerService.getCustomerById(any())).thenReturn(Optional.of(c));

        ResponseEntity<Customer> response = customerController.getCustomerById(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void testGetCustomerById_notFound() {
        when(customerService.getCustomerById(any())).thenReturn(Optional.empty());

        ResponseEntity<Customer> response = customerController.getCustomerById(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
}