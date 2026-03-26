package com.rbih.loanApplication.service;

import com.rbih.loanApplication.dto.*;
import com.rbih.loanApplication.entity.LoanApplicationEntity;
import com.rbih.loanApplication.enums.ApplicationStatus;
import com.rbih.loanApplication.enums.RejectionReason;
import com.rbih.loanApplication.enums.RiskBand;
import com.rbih.loanApplication.repository.LoanApplicationRepository;
import com.rbih.loanApplication.util.EmiCalculator;
import com.rbih.loanApplication.util.InterestRateCalculator;
import com.rbih.loanApplication.util.RejectionEvaluator;
import com.rbih.loanApplication.util.RiskBandCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;

    @Autowired
    public LoanApplicationService(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    public LoanApplicationResponse evaluate(LoanApplicationRequest request) {
        UUID applicationId = UUID.randomUUID();
        ApplicantRequest applicant = request.applicant();
        LoanRequest loan = request.loan();
        BigDecimal loanAmount = BigDecimal.valueOf(loan.amount()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthlyIncome = BigDecimal.valueOf(applicant.monthlyIncome()).setScale(2, RoundingMode.HALF_UP);


        RiskBand riskBand = RiskBandCalculator.calculateRiskBand(applicant.creditScore());
        BigDecimal interestRate = InterestRateCalculator.calculate(riskBand, applicant.employmentType(), loanAmount);
        BigDecimal emi = EmiCalculator.calculate(loanAmount, interestRate, loan.tenureMonths());

        List<RejectionReason> reasons = RejectionEvaluator.evaluate(
                applicant.creditScore(), applicant.age(), loan.tenureMonths(), monthlyIncome, emi);

        if (!reasons.isEmpty()) {
            persist(applicationId, request, ApplicationStatus.REJECTED, null, null, null, null, reasons);
            return LoanApplicationResponse.rejected(applicationId, reasons);
        }
        BigDecimal totalPayable = emi.multiply(BigDecimal.valueOf(loan.tenureMonths())).setScale(2, RoundingMode.HALF_UP);

        OfferResponse offer = new OfferResponse(interestRate, loan.tenureMonths(), emi, totalPayable);
        persist(applicationId, request, ApplicationStatus.APPROVED, riskBand, interestRate, emi, totalPayable, null);
        return LoanApplicationResponse.approved(applicationId, riskBand, offer);
    }

    private void persist(UUID id, LoanApplicationRequest req, ApplicationStatus status,
                         RiskBand riskBand, BigDecimal rate, BigDecimal emi,
                         BigDecimal totalPayable, List<RejectionReason> reasons) {
        LoanApplicationEntity entity = LoanApplicationEntity.builder()
                .id(id)
                .applicantName(req.applicant().name())
                .applicantAge(req.applicant().age())
                .monthlyIncome(req.applicant().monthlyIncome())
                .employmentType(req.applicant().employmentType())
                .creditScore(req.applicant().creditScore())
                .loanAmount(BigDecimal.valueOf(req.loan().amount()))
                .tenureMonths(req.loan().tenureMonths())
                .purpose(req.loan().purpose())
                .status(status)
                .riskBand(riskBand)
                .interestRate(rate)
                .emi(emi)
                .totalPayable(totalPayable)
                .rejectionReasons(reasons != null
                        ? reasons.stream().map(Enum::name).collect(Collectors.joining(","))
                        : null)
                .build();
        repository.save(entity);
    }

}
