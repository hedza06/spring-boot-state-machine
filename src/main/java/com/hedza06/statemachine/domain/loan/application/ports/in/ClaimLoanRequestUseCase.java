package com.hedza06.statemachine.domain.loan.application.ports.in;

import com.hedza06.statemachine.domain.loan.Loan;
import com.hedza06.statemachine.domain.loan.adapters.web.dto.UserDTO;

public interface ClaimLoanRequestUseCase
{
    Loan claim(Integer loanId, UserDTO claimer);
}
