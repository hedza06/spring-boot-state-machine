package com.hedza06.statemachine.services;

import com.hedza06.statemachine.domain.payment.Payment;
import com.hedza06.statemachine.domain.payment.PaymentEvent;
import com.hedza06.statemachine.domain.payment.PaymentState;
import com.hedza06.statemachine.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentServiceImplTest {

    private Payment payment;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp()
    {
        payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100_12));
    }

    @Test
    void testPaymentFlow()
    {
        Payment savedPayment = paymentService.create(payment);
        StateMachine<PaymentState, PaymentEvent> stateMachine = paymentService.preAuthorize(savedPayment.getId());

        Payment preAuthorizedPayment = paymentRepository
            .findById(savedPayment.getId())
            .orElseThrow(EntityNotFoundException::new);

        assertThat(preAuthorizedPayment).isNotNull();
        assertThat(stateMachine.getState().getId()).isNotNull().isEqualTo(PaymentState.PRE_AUTH);
        assertThat(preAuthorizedPayment.getPaymentState()).isEqualTo(PaymentState.PRE_AUTH);
    }
}