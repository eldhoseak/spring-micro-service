package com.hotel.reservation.payment_service.controller;

import com.hotel.reservation.payment_service.entity.Payment;
import com.hotel.reservation.payment_service.exception.NoDataFoundException;
import com.hotel.reservation.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public Payment processPayment(@RequestBody Payment payment) {
        payment.setPaymentId(null);
        return paymentService.pay(payment);
    }

    @PostMapping("/refund/{paymentId}")
    public Payment processRefund(@PathVariable("paymentId") Long paymentId) throws NoDataFoundException {
        return paymentService.refund(paymentId);
    }

}