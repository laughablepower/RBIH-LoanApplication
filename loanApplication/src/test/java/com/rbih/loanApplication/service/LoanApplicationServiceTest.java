package com.rbih.loanApplication.service;

import com.rbih.loanApplication.dto.ApplicantRequest;
import com.rbih.loanApplication.dto.LoanApplicationRequest;
import com.rbih.loanApplication.dto.LoanApplicationResponse;
import com.rbih.loanApplication.dto.LoanRequest;
import com.rbih.loanApplication.enums.*;
import com.rbih.loanApplication.repository.LoanApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository repository;

    @InjectMocks
    private LoanApplicationService service;

    private ApplicantRequest goodApplicant;
    private LoanRequest standardLoan;

    @BeforeEach
    void setUp() {
        goodApplicant = new ApplicantRequest("John Doe", 30, 75000, EmploymentType.SALARIED, 720);
        standardLoan = new LoanRequest(500000, 36, LoanPurpose.PERSONAL);
    }

    @Test
    void shouldApproveGoodApplication() {
        when(repository.save(any())).thenReturn(null);
        LoanApplicationRequest request = new LoanApplicationRequest(goodApplicant, standardLoan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.APPROVED, response.status());
        assertNotNull(response.applicationId());
        assertNotNull(response.riskBand());
        assertNotNull(response.offer());
        assertNull(response.rejectionReasons());
    }

    @Test
    void shouldReturnMediumRiskBandForScore720() {
        when(repository.save(any())).thenReturn(null);
        LoanApplicationRequest request = new LoanApplicationRequest(goodApplicant, standardLoan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(RiskBand.MEDIUM, response.riskBand());
    }

    @Test
    void shouldReturnLowRiskForHighCreditScore() {
        when(repository.save(any())).thenReturn(null);
        ApplicantRequest applicant = new ApplicantRequest("Jane Doe", 30, 75000, EmploymentType.SALARIED, 780);
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, standardLoan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.APPROVED, response.status());
        assertEquals(RiskBand.LOW, response.riskBand());
        assertEquals(new BigDecimal("12.00"), response.offer().interestRate());
    }

    @Test
    void shouldCalculateCorrectTenureInOffer() {
        when(repository.save(any())).thenReturn(null);
        LoanApplicationRequest request = new LoanApplicationRequest(goodApplicant, standardLoan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(36, response.offer().tenureMonths());
    }

    @Test
    void shouldCalculateEmiWithScale2() {
        when(repository.save(any())).thenReturn(null);
        LoanApplicationRequest request = new LoanApplicationRequest(goodApplicant, standardLoan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(2, response.offer().emi().scale());
        assertEquals(2, response.offer().totalPayable().scale());
    }

    @Test
    void shouldApplyEmploymentPremiumForSelfEmployed() {
        when(repository.save(any())).thenReturn(null);
        ApplicantRequest applicant = new ApplicantRequest("Self Emp", 30, 100000, EmploymentType.SELF_EMPLOYED, 780);
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, standardLoan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(new BigDecimal("13.00"), response.offer().interestRate());
    }

    @Test
    void shouldApplyLoanSizePremiumForLargeLoans() {
        when(repository.save(any())).thenReturn(null);
        ApplicantRequest applicant = new ApplicantRequest("Big Loan", 30, 200000, EmploymentType.SALARIED, 780);
        LoanRequest loan = new LoanRequest(1500000, 60, LoanPurpose.HOME);
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, loan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(new BigDecimal("12.50"), response.offer().interestRate());
    }

    @Test
    void shouldRejectForLowCreditScore() {
        when(repository.save(any())).thenReturn(null);
        ApplicantRequest applicant = new ApplicantRequest("Low Score", 30, 75000, EmploymentType.SALARIED, 550);
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, standardLoan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.REJECTED, response.status());
        assertNull(response.riskBand());
        assertNull(response.offer());
        assertTrue(response.rejectionReasons().contains(RejectionReason.LOW_CREDIT_SCORE));
    }

    @Test
    void shouldRejectWhenAgePlusTenureExceeds65() {
        when(repository.save(any())).thenReturn(null);
        ApplicantRequest applicant = new ApplicantRequest("Old Borrower", 55, 75000, EmploymentType.SALARIED, 720);
        LoanRequest loan = new LoanRequest(500000, 132, LoanPurpose.PERSONAL);
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, loan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.REJECTED, response.status());
        assertTrue(response.rejectionReasons().contains(RejectionReason.AGE_TENURE_LIMIT_EXCEEDED));
    }

    @Test
    void shouldRejectWhenEmiExceeds50PercentOfIncome() {
        when(repository.save(any())).thenReturn(null);
        ApplicantRequest applicant = new ApplicantRequest("High EMI", 30, 30000, EmploymentType.SALARIED, 720);
        LoanRequest loan = new LoanRequest(4000000, 36, LoanPurpose.HOME);
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, loan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.REJECTED, response.status());
        assertTrue(response.rejectionReasons().contains(RejectionReason.EMI_EXCEEDS_50_PERCENT));
    }

    @Test
    void shouldRejectWithMultipleReasons() {
        when(repository.save(any())).thenReturn(null);
        ApplicantRequest applicant = new ApplicantRequest("Multi Fail", 58, 30000, EmploymentType.SALARIED, 500);
        LoanRequest loan = new LoanRequest(4000000, 120, LoanPurpose.HOME);
        LoanApplicationRequest request = new LoanApplicationRequest(applicant, loan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.REJECTED, response.status());
        assertTrue(response.rejectionReasons().size() >= 2);
    }
}