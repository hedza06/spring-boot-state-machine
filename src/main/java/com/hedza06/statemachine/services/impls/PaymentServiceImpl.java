package com.hedza06.statemachine.services.impls;

import com.hedza06.statemachine.domain.payment.Payment;
import com.hedza06.statemachine.domain.payment.PaymentEvent;
import com.hedza06.statemachine.domain.payment.PaymentState;
import com.hedza06.statemachine.domain.payment.repository.PaymentRepository;
import com.hedza06.statemachine.services.PaymentService;
import com.hedza06.statemachine.services.PaymentStateChangeInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public static final String PAYMENT_ID_HEADER = "paymentId";
    private final PaymentRepository paymentRepository;

    @Qualifier("PaymentStateMachineFactory")
    private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;

    private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;


    @Override
    public Payment create(Payment payment)
    {
        payment.setPaymentState(PaymentState.NEW);
        return paymentRepository.save(payment);
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuthorize(Integer paymentId)
    {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.PRE_AUTHORIZE);

        return stateMachine;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> authorize(Integer paymentId)
    {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.AUTH_APPROVED);

        return stateMachine;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Integer paymentId)
    {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.AUTH_DECLINED);

        return stateMachine;
    }

    private StateMachine<PaymentState, PaymentEvent> build(Integer paymentId)
    {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(EntityNotFoundException::new);
        StateMachine<PaymentState, PaymentEvent> stateMachine = stateMachineFactory.getStateMachine(paymentId.toString());
        stateMachine.stop();

        stateMachine.getStateMachineAccessor().doWithAllRegions(accessor -> {
            accessor.addStateMachineInterceptor(paymentStateChangeInterceptor);
            accessor.resetStateMachine(new DefaultStateMachineContext<>(
                payment.getPaymentState(), null, null, null)
            );
        });

        stateMachine.start();

        return stateMachine;
    }

    public void sendEvent(Integer paymentId, StateMachine<PaymentState, PaymentEvent> stateMachine, PaymentEvent event)
    {
        var message = MessageBuilder
            .withPayload(event)
            .setHeader(PAYMENT_ID_HEADER, paymentId)
            .build();

        stateMachine.sendEvent(message);
    }
}
