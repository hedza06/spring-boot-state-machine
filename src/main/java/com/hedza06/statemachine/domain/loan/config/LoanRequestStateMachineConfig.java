package com.hedza06.statemachine.domain.loan.config;

import com.hedza06.statemachine.domain.loan.LoanEvent;
import com.hedza06.statemachine.domain.loan.LoanState;
import com.hedza06.statemachine.services.actions.LoanRequestAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = "LoanStateMachineFactory")
public class LoanRequestStateMachineConfig extends StateMachineConfigurerAdapter<LoanState, LoanEvent> {

    private final LoanRequestAction loanRequestAction;


    @Override
    public void configure(StateMachineStateConfigurer<LoanState, LoanEvent> states) throws Exception
    {
        states
            .withStates()
            .initial(LoanState.CREATED)
            .states(EnumSet.allOf(LoanState.class))
            .end(LoanState.DECLINED)
            .end(LoanState.APPROVED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<LoanState, LoanEvent> transitions) throws Exception
    {
        transitions
            .withExternal()
            .source(LoanState.CREATED).target(LoanState.IN_PROGRESS).event(LoanEvent.CLAIM)
            .action(loanRequestAction.invokeClaim())

            .and()
            .withExternal()
            .source(LoanState.IN_PROGRESS).target(LoanState.DELEGATED).event(LoanEvent.ASSIGN)
            .action(loanRequestAction.invokeAssignment())

            .and()
            .withExternal()
            .source(LoanState.DELEGATED).target(LoanState.IN_PROGRESS).event(LoanEvent.CLAIM)
            .action(loanRequestAction.invokeApprovement())

            .and()
            .withExternal()
            .source(LoanState.IN_PROGRESS).target(LoanState.APPROVED).event(LoanEvent.APPROVE)
            .action(loanRequestAction.invokeApprovement())

            .and()
            .withExternal()
            .source(LoanState.IN_PROGRESS).target(LoanState.DECLINED).event(LoanEvent.DECLINE)
            .action(loanRequestAction.invokeRejection());
    }
}
