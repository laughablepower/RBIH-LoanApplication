package com.rbih.loanApplication.util;

import com.rbih.loanApplication.enums.EmploymentType;
import com.rbih.loanApplication.enums.RiskBand;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class InterestRateCalculatorTest {
        @Test
        void shouldReturnBaseRateForLowRiskSalaried() {
        assertEquals(new BigDecimal("12.00"), InterestRateCalculator.calculate(RiskBand.LOW, EmploymentType.SALARIED, new BigDecimal("500000")));
    }

        @Test
        void shouldAddRiskPremiumForMedium() {
        assertEquals(new BigDecimal("13.50"), InterestRateCalculator.calculate(RiskBand.MEDIUM, EmploymentType.SALARIED, new BigDecimal("500000")));
    }

        @Test
        void shouldAddEmploymentPremium() {
        assertEquals(BigDecimal.valueOf(13).setScale(2, RoundingMode.HALF_UP), InterestRateCalculator.calculate(RiskBand.LOW, EmploymentType.SELF_EMPLOYED, new BigDecimal("500000")));
    }

        @Test
        void shouldAddLoanSizePremium() {
        assertEquals(new BigDecimal("12.50"), InterestRateCalculator.calculate(RiskBand.LOW, EmploymentType.SALARIED, new BigDecimal("1500000")));
    }

        @Test
        void shouldStackAllPremiums() {
        assertEquals(new BigDecimal("16.50"), InterestRateCalculator.calculate(RiskBand.HIGH, EmploymentType.SELF_EMPLOYED, new BigDecimal("2000000")));
    }
}
