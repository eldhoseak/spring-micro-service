package com.hotel.reservation.reservation_service.service;

import com.hotel.reservation.reservation_service.model.HotelRoom;
import com.hotel.reservation.reservation_service.model.Payment;
import com.hotel.reservation.reservation_service.model.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final WebClient webClient;

    public PaymentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9003").build();
    }

    @CircuitBreaker(name = "paymentServiceCircuitBreaker", fallbackMethod = "fallbackProcessPayment")
    public Mono<Payment> processPayment(Payment payment) {

        Payment hardcodedPayment = new Payment();
        hardcodedPayment.setPaymentId(12345L);
        hardcodedPayment.setAmount(BigDecimal.valueOf(100.0));
        hardcodedPayment.setPaymentStatus(PaymentStatus.SUCCESS);

        return Mono.just(hardcodedPayment);
       /* return webClient.post()
                .uri("/payments")
                .bodyValue(payment)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error))))
                .bodyToMono(Payment.class);

        */
    }

    public Mono<Payment>fallbackProcessPayment(Throwable t) {
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.FAILED);
        return Mono.just(payment);
    }

    public Mono<Payment> processRefund(Long paymentId) {

        Payment hardcodedPayment = new Payment();
        hardcodedPayment.setPaymentId(3434L);
        hardcodedPayment.setAmount(BigDecimal.valueOf(500.0));
        hardcodedPayment.setPaymentStatus(PaymentStatus.SUCCESS);

        return Mono.just(hardcodedPayment);

       /* return webClient.get()
                .uri("/payments/refund/{paymentId}", paymentId)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error))))
                .bodyToMono(Payment.class);*/
    }
}