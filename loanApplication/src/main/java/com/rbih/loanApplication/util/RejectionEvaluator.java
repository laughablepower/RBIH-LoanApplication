package com.rbih.loanApplication.util;

import com.rbih.loanApplication.enums.RejectionReason;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RejectionEvaluator {


    private static final BigDecimal EMI_INCOME_RATIO = new BigDecimal("0.50");

    private RejectionEvaluator() {}

    public static List<RejectionReason> evaluate(int creditScore, int age, int tenureMonths, BigDecimal monthlyIncome, BigDecimal emi) {
        List<RejectionReason> rejectionReasons = new ArrayList<>();

        if (creditScore < 600) {
            rejectionReasons.add(RejectionReason.LOW_CREDIT_SCORE);
        }

        double tenureYears = tenureMonths / 12.0;
        if (age + tenureYears > 65) {
            rejectionReasons.add(RejectionReason.AGE_TENURE_LIMIT_EXCEEDED);
        }

        BigDecimal maxEmi = monthlyIncome.multiply(EMI_INCOME_RATIO);
        if (emi.compareTo(maxEmi) > 0) {
            rejectionReasons.add(RejectionReason.EMI_EXCEEDS_50_PERCENT);
        }

        return rejectionReasons;
    }
}
