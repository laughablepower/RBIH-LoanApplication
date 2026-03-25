package com.rbih.loanApplication.dto;

import com.rbih.loanApplication.enums.LoanPurpose;
import jakarta.validation.constraints.*;

public record LoanRequest(
        @DecimalMin(value = "10000", message = "Loan amount must be at least 10,000")
        @DecimalMax(value = "5000000", message = "Loan amount must be at most 50,00,000")
        double amount,

        @Min(value = 6, message = "Tenure must be at least 6 months")
        @Max(value = 360, message = "Tenure must be at most 360 months")
        int tenureMonths,

        @NotNull(message = "Loan purpose is required")
        LoanPurpose purpose

) {
}
