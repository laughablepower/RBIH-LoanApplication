package com.rbih.loanApplication.util;

import com.rbih.loanApplication.enums.EmploymentType;
import com.rbih.loanApplication.enums.RiskBand;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class InterestRateCalculator {


    private static final BigDecimal BASE_RATE = new BigDecimal("12.00");

    private InterestRateCalculator() {}

    public static BigDecimal calculate(RiskBand riskBand, EmploymentType employment, BigDecimal loanAmount) {
        BigDecimal rate = BASE_RATE;

        rate = rate.add(switch (riskBand) {
            case LOW    -> BigDecimal.ZERO;
            case MODERATE -> new BigDecimal("1.50");
            case HIGH   -> new BigDecimal("3.00");
        });

        if (employment == EmploymentType.SELF_EMPLOYED) {
            rate = rate.add(new BigDecimal("1.00"));
        }

        if (loanAmount.compareTo(new BigDecimal("1000000")) > 0) {
            rate = rate.add(new BigDecimal("0.50"));
        }

        return rate.setScale(2, RoundingMode.HALF_UP);
    }
}
