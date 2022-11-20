package com.hedza06.statemachine.config;

import com.hedza06.statemachine.domain.payment.PaymentEvent;
import com.hedza06.statemachine.domain.payment.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class PaymentStateMachineConfigTest {

    @Autowired
    private StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;


    @Test
    void shouldBeInPreAuthStateAfterSendingPreAuthorizationApprovedEvent()
    {
        StateMachine<PaymentState, PaymentEvent> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());
        stateMachine.start();

        System.out.println(stateMachine.getState().toString());
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.NEW);

        stateMachine.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        System.out.println(stateMachine.getState().toString());
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.NEW);

        stateMachine.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
        System.out.println(stateMachine.getState().toString());
        assertThat(stateMachine.getState().getId()).isEqualTo(PaymentState.PRE_AUTH);
    }
}