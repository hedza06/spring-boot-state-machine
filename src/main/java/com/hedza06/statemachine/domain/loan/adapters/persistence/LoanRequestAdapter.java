package com.hedza06.statemachine.domain.loan.adapters.persistence;

import com.hedza06.statemachine.domain.loan.Loan;
import com.hedza06.statemachine.domain.loan.LoanEvent;
import com.hedza06.statemachine.domain.loan.LoanState;
import com.hedza06.statemachine.domain.loan.application.ports.in.ApproveLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.application.ports.in.ClaimLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.application.ports.in.CreateLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.application.ports.in.DeclineLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.application.ports.in.DelegateLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.adapters.web.dto.UserDTO;
import com.hedza06.statemachine.services.LoanRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class LoanRequestAdapter implements CreateLoanRequestUseCase, ClaimLoanRequestUseCase,
        DelegateLoanRequestUseCase, ApproveLoanRequestUseCase, DeclineLoanRequestUseCase
{
    private final LoanRepository loanRepository;
    private final LoanRequestService loanRequestService;

    @Qualifier("LoanStateMachineFactory")
    private final StateMachineFactory<LoanState, LoanEvent> stateMachineFactory;


    @Override
    public Loan create(Loan loanDetails)
    {
        loanDetails.setState(LoanState.CREATED);
        return loanRepository.save(loanDetails);
    }

    @Override
    public Loan approve(Integer loanId, UserDTO approvedBy)
    {
        loanRequestService.approve(loanId, approvedBy.getUsername());
        return loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Loan claim(Integer loanId, UserDTO claimer)
    {
        loanRequestService.claim(loanId, claimer.getUsername());
        return loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Loan decline(Integer loanId, UserDTO decliner)
    {
        loanRequestService.decline(loanId, decliner.getUsername());
        return loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Loan delegate(Integer loanId, UserDTO assignee)
    {
        loanRequestService.decline(loanId, assignee.getUsername());
        return loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
    }
}
