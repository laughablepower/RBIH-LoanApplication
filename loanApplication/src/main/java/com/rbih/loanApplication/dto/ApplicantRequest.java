package com.rbih.loanApplication.dto;

import com.rbih.loanApplication.enums.EmploymentType;
import jakarta.validation.constraints.*;

public record ApplicantRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Min(value = 21, message = "Age must be at least 21")
        @Max(value = 60, message = "Age must be at most 60")
        int age,

        @Positive(message = "Monthly income must be greater than 0")
        double monthlyIncome,

        @NotNull(message = "Employment type is required")
        EmploymentType employmentType,

        @Min(value = 300, message = "Credit score must be at least 300")
        @Max(value = 900, message = "Credit score must be at most 900")
        int creditScore
) {}
