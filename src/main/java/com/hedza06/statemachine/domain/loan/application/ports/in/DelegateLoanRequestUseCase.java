package com.hedza06.statemachine.domain.loan.application.ports.in;

import com.hedza06.statemachine.domain.loan.Loan;
import com.hedza06.statemachine.domain.loan.adapters.web.dto.UserDTO;

public interface DelegateLoanRequestUseCase
{
    Loan delegate(Integer loanId, UserDTO assignee);
}
