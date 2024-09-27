package com.hotel.reservation.reservation_service.service;

import com.hotel.reservation.reservation_service.model.Payment;
import com.hotel.reservation.reservation_service.model.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    private final WebClient webClient;

    @Autowired
    public PaymentService(@LoadBalanced WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://payment-service").build();
    }

    @CircuitBreaker(name = "paymentServiceCircuitBreaker", fallbackMethod = "fallbackProcessPayment")
    public Mono<Payment> processPayment(Payment payment) {

        return webClient.post()
                .uri("/payments/pay")
                .bodyValue(payment)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error))))
                .bodyToMono(Payment.class);
    }

    public Mono<Payment> fallbackProcessPayment(Throwable t) {
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.FAILED);
        return Mono.just(payment);
    }

    public Mono<Payment> processRefund(Long paymentId) {

        return webClient.post()
                .uri("/payments/refund/{paymentId}", paymentId)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error))))
                .bodyToMono(Payment.class);
    }
}