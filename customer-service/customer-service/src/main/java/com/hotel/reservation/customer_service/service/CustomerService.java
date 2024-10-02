package com.hotel.reservation.customer_service.service;

import com.hotel.reservation.customer_service.entity.Customer;
import com.hotel.reservation.customer_service.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        log.debug("Fetching all customers.");
        List<Customer> customers = customerRepository.findAll();
        log.debug("Returning {} customers.", customers.size());
        return customers;
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Customer> updateCustomer(Long id, Customer customerDetails) {
        log.debug("Updating details for customer id: {}", id);
        return customerRepository.findById(id).map(customer -> {
            customer.setFirstName(customerDetails.getFirstName());
            customer.setLastName(customerDetails.getLastName());
            customer.setEmail(customerDetails.getEmail());
            // Add other fields as necessary
            return customerRepository.save(customer);
        });
    }

    public Customer saveCustomer(Customer customerDetails) {
        log.debug("Saving details for customer ");
        return customerRepository.save(customerDetails);
    }
}