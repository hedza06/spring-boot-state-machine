package com.hedza06.statemachine.domain.loan.adapters.persistence;

import com.hedza06.statemachine.domain.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Integer> { }
