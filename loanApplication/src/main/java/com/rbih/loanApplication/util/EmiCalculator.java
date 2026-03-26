package com.rbih.loanApplication.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class EmiCalculator {


    private static final MathContext MC = MathContext.DECIMAL64;
    private static final int SCALE = 2;

    private EmiCalculator() {}

    public static BigDecimal calculate(BigDecimal principal, BigDecimal annualRate, int tenureMonths) {
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal power = onePlusR.pow(tenureMonths, MC);

        BigDecimal numerator = principal.multiply(monthlyRate, MC).multiply(power, MC);
        BigDecimal denominator = power.subtract(BigDecimal.ONE, MC);

        return numerator.divide(denominator, SCALE, RoundingMode.HALF_UP);
    }
}
