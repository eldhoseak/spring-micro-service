package com.hotel.reservation.reservation_service.service;

import com.hotel.reservation.reservation_service.model.Customer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final WebClient webClient;

    @Autowired
    public CustomerService(@LoadBalanced WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://customer-service").build();
    }

    @CircuitBreaker(name = "customerServiceCircuitBreaker", fallbackMethod = "fallbackGetCustomerDetails")
    public Mono<Customer> getCustomerDetails(Long customerId) {
        return webClient.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    public Mono<Customer> fallbackGetCustomerDetails(Throwable t) {
        Customer customer = new Customer();
        customer.setId((long) -1);
        return Mono.just(customer);
    }
}