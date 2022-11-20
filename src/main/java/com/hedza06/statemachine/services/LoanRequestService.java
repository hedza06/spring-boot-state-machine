package com.hedza06.statemachine.services;


import com.hedza06.statemachine.domain.loan.LoanEvent;
import com.hedza06.statemachine.domain.loan.LoanState;
import org.springframework.statemachine.StateMachine;

public interface LoanRequestService
{
    StateMachine<LoanState, LoanEvent> claim(Integer loanId, String claimer);
    StateMachine<LoanState, LoanEvent> assign(Integer loanId, String assignee);
    StateMachine<LoanState, LoanEvent> approve(Integer loanId, String approvedBy);
    StateMachine<LoanState, LoanEvent> decline(Integer loanId, String declinedBy);
}
