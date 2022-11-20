package com.hedza06.statemachine.services;

import com.hedza06.statemachine.domain.loan.Loan;
import com.hedza06.statemachine.domain.loan.LoanEvent;
import com.hedza06.statemachine.domain.loan.LoanState;
import com.hedza06.statemachine.domain.loan.adapters.persistence.LoanRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoanRequestServiceImplTest {

    private Loan loan;

    @Autowired
    private LoanRequestService loanRequestService;

    @Autowired
    private LoanRepository loanRepository;


    @BeforeEach
    void setUp()
    {
        loan = new Loan();
        loan.setAmount(BigDecimal.valueOf(5_000.00));
        loan.setCreatedAt(new Date());
    }

    @AfterEach
    void tearDown() {
        loanRepository.delete(loan);
    }

    @Test
    void shouldBeInProgressStateAndLoanDetailsShouldBeUpdated()
    {
        loan.setState(LoanState.CREATED);
        loanRepository.save(loan);

        String claimer = "hedza";
        StateMachine<LoanState, LoanEvent> stateMachine = loanRequestService.claim(loan.getId(), claimer);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.IN_PROGRESS);

        Loan loanRequest = loanRepository.findById(loan.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(loanRequest).isNotNull();
        assertThat(loanRequest.getState()).isEqualTo(LoanState.IN_PROGRESS);
        assertThat(loanRequest.getAssignee()).isEqualTo(claimer);
        assertThat(loanRequest.getDeclinedBy()).isNull();
        assertThat(loanRequest.getApprovedBy()).isNull();
    }

    @Test
    void shouldBeAssignedToUserAndStayInProgressState()
    {
        loan.setState(LoanState.IN_PROGRESS);
        loanRepository.save(loan);

        String assignee = "mark";
        StateMachine<LoanState, LoanEvent> stateMachine = loanRequestService.assign(loan.getId(), assignee);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.DELEGATED);

        Loan loanRequest = loanRepository.findById(loan.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(loanRequest).isNotNull();
        assertThat(loanRequest.getState()).isEqualTo(LoanState.DELEGATED);
        assertThat(loanRequest.getAssignee()).isEqualTo(assignee);
        assertThat(loanRequest.getDeclinedBy()).isNull();
        assertThat(loanRequest.getApprovedBy()).isNull();
    }

    @Test
    void shouldBeApprovedByGivenApprover()
    {
        loan.setState(LoanState.IN_PROGRESS);
        loanRepository.save(loan);

        String approvedBy = "georgina";
        StateMachine<LoanState, LoanEvent> stateMachine = loanRequestService.approve(loan.getId(), approvedBy);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.APPROVED);

        Loan loanRequest = loanRepository.findById(loan.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(loanRequest).isNotNull();
        assertThat(loanRequest.getState()).isEqualTo(LoanState.APPROVED);
        assertThat(loanRequest.getApprovedBy()).isEqualTo(approvedBy);
        assertThat(loanRequest.getDeclinedBy()).isNull();
    }

    @Test
    void shouldBeDeclinedByGivenDecliner()
    {
        loan.setState(LoanState.IN_PROGRESS);
        loanRepository.save(loan);

        String declinedBy = "jack";
        StateMachine<LoanState, LoanEvent> stateMachine = loanRequestService.decline(loan.getId(), declinedBy);
        assertThat(stateMachine.getState().getId()).isEqualTo(LoanState.DECLINED);

        Loan loanRequest = loanRepository.findById(loan.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(loanRequest).isNotNull();
        assertThat(loanRequest.getState()).isEqualTo(LoanState.DECLINED);
        assertThat(loanRequest.getApprovedBy()).isNull();
        assertThat(loanRequest.getDeclinedBy()).isEqualTo(declinedBy);
    }
}