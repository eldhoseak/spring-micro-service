package com.hotel.reservation.payment_service.service;

import com.hotel.reservation.payment_service.entity.Payment;
import com.hotel.reservation.payment_service.exception.NoDataFoundException;
import com.hotel.reservation.payment_service.model.PaymentStatus;
import com.hotel.reservation.payment_service.model.PaymentType;
import com.hotel.reservation.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment pay(Payment payment) {
        log.debug("Processing payment : {}", payment);
        payment.setPaymentType(PaymentType.PAY);
        payment.setPaymentStatus(PaymentStatus.FAILED);
        if (isSuccessful()) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }
        log.debug("Payment status : {}", payment.getPaymentStatus());
        return paymentRepository.save(payment);
    }


    public Payment refund(Long paymentId) throws NoDataFoundException {
        log.debug("Refund payment : {}", paymentId);
        Payment payment = paymentRepository.getReferenceById(paymentId);
        if (payment != null) {
            payment.setPaymentType(PaymentType.REFUND);
            return paymentRepository.save(payment);
        } else {
            throw new NoDataFoundException(String.format("Payment Refund failed as payment reference %s is not found",
                    paymentId));
        }
    }

    public boolean isSuccessful() {
        Random random = new Random();
        return random.nextBoolean();
    }

}