package com.hedza06.statemachine.services;

import com.hedza06.statemachine.domain.payment.Payment;
import com.hedza06.statemachine.domain.payment.PaymentEvent;
import com.hedza06.statemachine.domain.payment.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService
{
    Payment create(Payment payment);
    StateMachine<PaymentState, PaymentEvent> preAuthorize(Integer paymentId);
    StateMachine<PaymentState, PaymentEvent> authorize(Integer paymentId);
    StateMachine<PaymentState, PaymentEvent> declineAuth(Integer paymentId);
}
