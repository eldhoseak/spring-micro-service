package com.hotel.reservation.payment_service.entity;


import com.hotel.reservation.payment_service.model.PaymentStatus;
import com.hotel.reservation.payment_service.model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PAYMENTS")
public class Payment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_Id")
	private Long paymentId;

	@Column(name = "customer_Id")
	private Long customerId;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(name = "payment_status")
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	@Column(name = "payment_type")
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

}