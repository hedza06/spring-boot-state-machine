package com.hedza06.statemachine.services.impls;

import com.hedza06.statemachine.domain.loan.Loan;
import com.hedza06.statemachine.domain.loan.LoanEvent;
import com.hedza06.statemachine.domain.loan.LoanState;
import com.hedza06.statemachine.domain.loan.adapters.persistence.LoanRepository;
import com.hedza06.statemachine.services.LoanRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;


@Component
@RequiredArgsConstructor
public class LoanRequestServiceImpl implements LoanRequestService {

    public static final String LOAN_ID = "loanId";
    public static final String CLAIMER = "claimer";
    public static final String ASSIGNEE = "assignee";
    public static final String APPROVED_BY = "approvedBy";
    public static final String DECLINED_BY = "declinedBy";

    private final LoanRepository loanRepository;

    @Qualifier("LoanStateMachineFactory")
    private final StateMachineFactory<LoanState, LoanEvent> stateMachineFactory;


    @Override
    public StateMachine<LoanState, LoanEvent> claim(Integer loanId, String claimer)
    {
        StateMachine<LoanState, LoanEvent> stateMachine = restore(loanId);
        var message = MessageBuilder
            .withPayload(LoanEvent.CLAIM)
            .setHeader(LOAN_ID, loanId)
            .setHeader(CLAIMER, claimer)
            .build();

        stateMachine.sendEvent(message);

        return stateMachine;
    }

    @Override
    public StateMachine<LoanState, LoanEvent> assign(Integer loanId, String assignee)
    {
        StateMachine<LoanState, LoanEvent> stateMachine = restore(loanId);
        var message = MessageBuilder
            .withPayload(LoanEvent.ASSIGN)
            .setHeader(LOAN_ID, loanId)
            .setHeader(ASSIGNEE, assignee)
            .build();

        stateMachine.sendEvent(message);

        return stateMachine;
    }

    @Override
    public StateMachine<LoanState, LoanEvent> approve(Integer loanId, String approvedBy)
    {
        StateMachine<LoanState, LoanEvent> stateMachine = restore(loanId);
        var message = MessageBuilder
            .withPayload(LoanEvent.APPROVE)
            .setHeader(LOAN_ID, loanId)
            .setHeader(APPROVED_BY, approvedBy)
            .build();

        stateMachine.sendEvent(message);

        return stateMachine;
    }

    @Override
    public StateMachine<LoanState, LoanEvent> decline(Integer loanId, String declinedBy)
    {
        StateMachine<LoanState, LoanEvent> stateMachine = restore(loanId);
        var message = MessageBuilder
            .withPayload(LoanEvent.DECLINE)
            .setHeader(LOAN_ID, loanId)
            .setHeader(DECLINED_BY, declinedBy)
            .build();

        stateMachine.sendEvent(message);

        return stateMachine;
    }

    private StateMachine<LoanState, LoanEvent> restore(Integer loanId)
    {
        Loan loan = loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
        StateMachine<LoanState, LoanEvent> stateMachine = stateMachineFactory.getStateMachine(loanId.toString());
        stateMachine.stop();

        stateMachine.getStateMachineAccessor().doWithAllRegions(accessor -> {
            accessor.resetStateMachine(new DefaultStateMachineContext<>(
                loan.getState(), null, null, null)
            );
        });

        stateMachine.start();

        return stateMachine;
    }
}
