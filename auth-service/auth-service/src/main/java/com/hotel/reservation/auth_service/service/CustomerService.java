package com.hotel.reservation.auth_service.service;

import com.hotel.reservation.auth_service.entity.Customer;
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
    public Mono<Customer> getCustomerDetails(String email) {
        return webClient.get()
                .uri("/customers/email/{email}", email)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    public Mono<Customer> fallbackGetCustomerDetails(Throwable t) {
        Customer customer = new Customer();
        customer.setId((long) -1);
        return Mono.just(customer);
    }

    public Mono<Customer> saveCustomer(Customer customer) {
        return webClient.post()
                .uri("/customers")
                .bodyValue(customer)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error))))
                .bodyToMono(Customer.class);
    }
}