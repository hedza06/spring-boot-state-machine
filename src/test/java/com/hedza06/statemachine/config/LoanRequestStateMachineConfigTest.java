package com.hedza06.statemachine.config;

import com.hedza06.statemachine.domain.loan.LoanEvent;
import com.hedza06.statemachine.domain.loan.LoanState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class LoanRequestStateMachineConfigTest {

    @Autowired
    @Qualifier("LoanStateMachineFactory")
    private StateMachineFactory<LoanState, LoanEvent> stateMachineFactory;


    @Test
    void shouldBeInCreateState()
    {
        StateMachine<LoanState, LoanEvent> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());
        stateMachine.start();

        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.CREATED);
    }

    @Test
    void shouldBeInProgressAfterSendingClaimEvent()
    {
        StateMachine<LoanState, LoanEvent> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.CREATED);

        stateMachine.sendEvent(LoanEvent.CLAIM);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.IN_PROGRESS);
    }

    @Test
    void shouldBeInDelegatedStateAfterAssignment()
    {
        StateMachine<LoanState, LoanEvent> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.CREATED);

        stateMachine.sendEvent(LoanEvent.CLAIM);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.IN_PROGRESS);

        stateMachine.sendEvent(LoanEvent.ASSIGN);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.DELEGATED);
    }

    @Test
    void shouldBeApprovedAfterSendingApproveEvent()
    {
        StateMachine<LoanState, LoanEvent> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.CREATED);

        stateMachine.sendEvent(LoanEvent.CLAIM);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.IN_PROGRESS);

        stateMachine.sendEvent(LoanEvent.APPROVE);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.APPROVED);
    }

    @Test
    void shouldBeDeclinedAfterSendingDeclineEvent()
    {
        StateMachine<LoanState, LoanEvent> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.CREATED);

        stateMachine.sendEvent(LoanEvent.CLAIM);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.IN_PROGRESS);

        stateMachine.sendEvent(LoanEvent.DECLINE);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.DECLINED);
    }
}