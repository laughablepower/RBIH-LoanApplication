package com.rbih.loanApplication.util;

import com.rbih.loanApplication.enums.RiskBand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public final class RiskBandCalculatorTest {

    @Test
    void shouldReturnHighForBoundary600() {
        assertEquals(RiskBand.HIGH, RiskBandCalculator.calculateRiskBand(600));
    }

    @Test
    void shouldReturnLowForBoundary750() {
        assertEquals(RiskBand.LOW, RiskBandCalculator.calculateRiskBand(750));
    }
}
