package com.hedza06.statemachine.services;

import com.hedza06.statemachine.domain.payment.Payment;
import com.hedza06.statemachine.domain.payment.PaymentEvent;
import com.hedza06.statemachine.domain.payment.PaymentState;
import com.hedza06.statemachine.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.hedza06.statemachine.services.impls.PaymentServiceImpl.PAYMENT_ID_HEADER;

@Component
@RequiredArgsConstructor
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message,
                               Transition<PaymentState, PaymentEvent> transition,
                               StateMachine<PaymentState, PaymentEvent> stateMachine,
                               StateMachine<PaymentState, PaymentEvent> rootStateMachine)
    {
        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable((Integer) msg.getHeaders().getOrDefault(PAYMENT_ID_HEADER, -1))
                .ifPresent(paymentId -> {
                    Payment payment = paymentRepository.findById(paymentId).orElseThrow(EntityNotFoundException::new);
                    payment.setPaymentState(state.getId());
                    paymentRepository.save(payment);
                });
        });
    }
}
