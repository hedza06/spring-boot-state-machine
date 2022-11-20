package com.hedza06.statemachine.domain.payment.repository;

import com.hedza06.statemachine.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> { }
