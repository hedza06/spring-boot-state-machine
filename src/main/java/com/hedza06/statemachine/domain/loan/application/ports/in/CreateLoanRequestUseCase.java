package com.hedza06.statemachine.domain.loan.application.ports.in;

import com.hedza06.statemachine.domain.loan.Loan;

public interface CreateLoanRequestUseCase
{
    Loan create(Loan loanDetails);
}
