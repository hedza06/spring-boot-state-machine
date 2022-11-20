package com.hedza06.statemachine.domain.loan.adapters.web;

import com.hedza06.statemachine.domain.loan.Loan;
import com.hedza06.statemachine.domain.loan.application.ports.in.ApproveLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.application.ports.in.ClaimLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.application.ports.in.CreateLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.application.ports.in.DeclineLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.application.ports.in.DelegateLoanRequestUseCase;
import com.hedza06.statemachine.domain.loan.adapters.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loan")
public class LoanController {

    private final CreateLoanRequestUseCase createLoanRequestUseCase;
    private final ClaimLoanRequestUseCase claimLoanRequestUseCase;
    private final DelegateLoanRequestUseCase delegateLoanRequestUseCase;
    private final ApproveLoanRequestUseCase approveLoanRequestUseCase;
    private final DeclineLoanRequestUseCase declineLoanRequestUseCase;


    @PostMapping
    public ResponseEntity<Loan> create(@RequestBody Loan loanRequest)
    {
        Loan loan = createLoanRequestUseCase.create(loanRequest);
        return new ResponseEntity<>(loan, HttpStatus.CREATED);
    }

    @PutMapping("{id}/claim")
    public ResponseEntity<Loan> claim(@PathVariable(value = "id") Integer loanId, @RequestBody UserDTO user)
    {
        log.info("Claiming loan with id: {}. User who claims: {}", loanId, user);

        Loan loan = claimLoanRequestUseCase.claim(loanId, user);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @PutMapping("{id}/assign")
    public ResponseEntity<Loan> assign(@PathVariable(value = "id") Integer loanId, @RequestBody UserDTO user)
    {
        log.info("Assigning loan with id: {}. User who delegated loan: {}", loanId, user);

        Loan loan = delegateLoanRequestUseCase.delegate(loanId, user);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @PutMapping("{id}/approve")
    public ResponseEntity<Loan> approve(@PathVariable(value = "id") Integer loanId, @RequestBody UserDTO user)
    {
        log.info("Approving loan with id: {}. User who approved the loan: {}", loanId, user);

        Loan loan = approveLoanRequestUseCase.approve(loanId, user);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @PutMapping("{id}/decline")
    public ResponseEntity<Loan> decline(@PathVariable(value = "id") Integer loanId, @RequestBody UserDTO user)
    {
        log.info("Declining the loan with id: {}. User who declined the loan request: {}", loanId, user);

        Loan loan = declineLoanRequestUseCase.decline(loanId, user);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }
}
