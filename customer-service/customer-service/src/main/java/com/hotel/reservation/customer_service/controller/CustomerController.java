package com.hotel.reservation.customer_service.controller;

import com.hotel.reservation.customer_service.entity.Customer;
import com.hotel.reservation.customer_service.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")

@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Get all customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Get a customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        log.debug("Fetching customer with ID: {}", id);
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(c -> {
                    c.setPassword("");
                    return ResponseEntity.ok(c);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        log.debug("Fetching customer with email: {}", email);
        Customer customer = customerService.getCustomerByEmail(email);
        if(customer == null){
           return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(customer);
        }
    }

    // Update customer details (partial update)
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        log.debug("Updating customer with ID: {}", id);
        Optional<Customer> updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return updatedCustomer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer) {
        log.debug("Saving customer details :");
        return customerService.saveCustomer(customer);
    }

}