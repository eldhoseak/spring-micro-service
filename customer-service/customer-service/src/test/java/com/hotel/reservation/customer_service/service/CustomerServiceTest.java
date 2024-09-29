package com.hotel.reservation.customer_service.service;

import com.hotel.reservation.customer_service.entity.Customer;
import com.hotel.reservation.customer_service.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testUpdateCustomer() {
        // Given
        Customer originalCustomer = new Customer();
        originalCustomer.setId(1L);
        originalCustomer.setFirstName("John");
        originalCustomer.setLastName("Doe");
        originalCustomer.setEmail("john.doe@example.com");

        Customer newCustomerDetails = new Customer();
        newCustomerDetails.setFirstName("Jane");
        newCustomerDetails.setLastName("Doe");
        newCustomerDetails.setEmail("jane.doe@example.com");

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(originalCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomerDetails);

        // When
        Optional<Customer> updatedCustomerOptional = customerService.updateCustomer(1L, newCustomerDetails);

        // Then
        Customer updatedCustomer = updatedCustomerOptional.orElse(null);
        assertEquals(newCustomerDetails.getFirstName(), updatedCustomer.getFirstName());
        assertEquals(newCustomerDetails.getLastName(), updatedCustomer.getLastName());
        assertEquals(newCustomerDetails.getEmail(), updatedCustomer.getEmail());
    }
}