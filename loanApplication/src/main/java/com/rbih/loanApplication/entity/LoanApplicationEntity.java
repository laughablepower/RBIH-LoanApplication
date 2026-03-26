package com.rbih.loanApplication.entity;

import com.rbih.loanApplication.enums.ApplicationStatus;
import com.rbih.loanApplication.enums.EmploymentType;
import com.rbih.loanApplication.enums.LoanPurpose;
import com.rbih.loanApplication.enums.RiskBand;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loan_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationEntity {

    @Id
    private UUID id;

    private String applicantName;
    private int applicantAge;
    private double monthlyIncome;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    private int creditScore;
    private BigDecimal loanAmount;
    private int tenureMonths;

    @Enumerated(EnumType.STRING)
    private LoanPurpose purpose;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    private RiskBand riskBand;

    private BigDecimal interestRate;
    private BigDecimal emi;
    private BigDecimal totalPayable;
    private String rejectionReasons;

    @Getter
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

}
