package com.rbih.loanApplication.util;

import com.rbih.loanApplication.enums.RiskBand;

public final class RiskBandCalculator {

    public static RiskBand calculateRiskBand(int creditScore) {
        if (creditScore >= 750) {
            return RiskBand.LOW;
        }
        if (creditScore >= 650) {
            return RiskBand.MEDIUM;
        }
        return RiskBand.HIGH;
    }
}
