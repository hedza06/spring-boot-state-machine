package com.hedza06.statemachine.domain.loan.application.ports.in;

import com.hedza06.statemachine.domain.loan.Loan;
import com.hedza06.statemachine.domain.loan.adapters.web.dto.UserDTO;

public interface DeclineLoanRequestUseCase
{
    Loan decline(Integer loanId, UserDTO decliner);
}
