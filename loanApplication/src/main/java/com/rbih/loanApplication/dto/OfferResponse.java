package com.rbih.loanApplication.dto;

import java.math.BigDecimal;

public record OfferResponse (
        BigDecimal interestRate,
        int tenureMonths,
        BigDecimal emi,
        BigDecimal totalPayable

){
}
