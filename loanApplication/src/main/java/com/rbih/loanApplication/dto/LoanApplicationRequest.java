package com.rbih.loanApplication.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LoanApplicationRequest(

        @Valid @NotNull(message = "Applicant details are required")
        ApplicantRequest applicant,

        @Valid @NotNull(message = "Loan details are required")
        LoanRequest loan

) {
}
