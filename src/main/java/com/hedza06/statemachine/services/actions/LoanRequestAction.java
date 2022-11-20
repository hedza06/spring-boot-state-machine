package com.hedza06.statemachine.services.actions;

import com.hedza06.statemachine.domain.loan.Loan;
import com.hedza06.statemachine.domain.loan.LoanEvent;
import com.hedza06.statemachine.domain.loan.LoanState;
import com.hedza06.statemachine.domain.loan.adapters.persistence.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

import static com.hedza06.statemachine.services.impls.LoanRequestServiceImpl.APPROVED_BY;
import static com.hedza06.statemachine.services.impls.LoanRequestServiceImpl.ASSIGNEE;
import static com.hedza06.statemachine.services.impls.LoanRequestServiceImpl.CLAIMER;
import static com.hedza06.statemachine.services.impls.LoanRequestServiceImpl.DECLINED_BY;
import static com.hedza06.statemachine.services.impls.LoanRequestServiceImpl.LOAN_ID;

@Component
@RequiredArgsConstructor
public class LoanRequestAction {

    private final LoanRepository loanRepository;


    public Action<LoanState, LoanEvent> invokeClaim()
    {
        return context -> {
            Integer loanId = (Integer) context.getMessageHeader(LOAN_ID);
            String claimer = (String) context.getMessageHeader(CLAIMER);

            Loan loan = loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
            loan.setUpdatedAt(new Date());
            loan.setAssignee(claimer);
            loan.setState(LoanState.IN_PROGRESS);

            loanRepository.save(loan);
        };
    }


    public Action<LoanState, LoanEvent> invokeAssignment()
    {
        return context -> {
            Integer loanId = (Integer) context.getMessageHeader(LOAN_ID);
            String assignee = (String) context.getMessageHeader(ASSIGNEE);

            Loan loan = loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
            loan.setUpdatedAt(new Date());
            loan.setAssignee(assignee);
            loan.setState(LoanState.DELEGATED);

            loanRepository.save(loan);
        };
    }

    public Action<LoanState, LoanEvent> invokeApprovement()
    {
        return context -> {
            Integer loanId = (Integer) context.getMessageHeader(LOAN_ID);
            String approvedBy = (String) context.getMessageHeader(APPROVED_BY);

            Loan loan = loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
            loan.setUpdatedAt(new Date());
            loan.setApprovedBy(approvedBy);
            loan.setState(LoanState.APPROVED);

            loanRepository.save(loan);
        };
    }

    public Action<LoanState, LoanEvent> invokeRejection()
    {
        return context -> {
            Integer loanId = (Integer) context.getMessageHeader(LOAN_ID);
            String declinedBy = (String) context.getMessageHeader(DECLINED_BY);

            Loan loan = loanRepository.findById(loanId).orElseThrow(EntityNotFoundException::new);
            loan.setUpdatedAt(new Date());
            loan.setDeclinedBy(declinedBy);
            loan.setState(LoanState.DECLINED);

            loanRepository.save(loan);
        };
    }
}
