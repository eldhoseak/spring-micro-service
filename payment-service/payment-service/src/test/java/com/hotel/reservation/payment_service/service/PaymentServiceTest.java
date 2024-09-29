package com.hotel.reservation.payment_service.service;

import com.hotel.reservation.payment_service.entity.Payment;
import com.hotel.reservation.payment_service.exception.NoDataFoundException;
import com.hotel.reservation.payment_service.model.PaymentStatus;
import com.hotel.reservation.payment_service.model.PaymentType;
import com.hotel.reservation.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;

    @Test
    public void pay_WhenCalled_ShouldReturnTheSavedPaymentStatus() {
        // Arrange
        Payment payment = new Payment();
        payment.setCustomerId(1L);
        payment.setPaymentId(1L);
        payment.setAmount(new BigDecimal("100.0"));

        Payment returnedPayment = new Payment();
        returnedPayment.setCustomerId(1L);
        returnedPayment.setPaymentId(1L);
        returnedPayment.setAmount(new BigDecimal("100.0"));
        returnedPayment.setPaymentType(PaymentType.PAY);
        returnedPayment.setPaymentStatus(PaymentStatus.SUCCESS);

        when(paymentRepository.save(any(Payment.class))).thenReturn(returnedPayment);

        // Act
        Payment result = paymentService.pay(payment);

        // Assert
        assertNotNull(result);
        assertEquals(returnedPayment, result);
        assertEquals(PaymentType.PAY, result.getPaymentType());
        assertTrue(result.getPaymentStatus().equals(PaymentStatus.SUCCESS) || result.getPaymentStatus().equals(PaymentStatus.FAILED));
    }


    @Test
    public void refund_ValidPaymentId_ShouldReturnThePaymentWithRefundStatus() throws NoDataFoundException {
        // Arrange
        Payment payment = new Payment();
        payment.setCustomerId(1L);
        payment.setPaymentId(1L);
        payment.setAmount(new BigDecimal("100.0"));
        payment.setPaymentType(PaymentType.REFUND);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        when(paymentRepository.getReferenceById(1L)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        Payment result = paymentService.refund(1L);

        // Assert
        assertNotNull(result);
        assertEquals(PaymentType.REFUND, result.getPaymentType());
    }

    @Test
    public void refund_InvalidPaymentId_ShouldThrowNoDataFoundException() {
        // Arrange
        when(paymentRepository.getReferenceById(1L)).thenReturn(null);

        // Act and Assert
        assertThrows(NoDataFoundException.class, () -> paymentService.refund(1L));
    }
}