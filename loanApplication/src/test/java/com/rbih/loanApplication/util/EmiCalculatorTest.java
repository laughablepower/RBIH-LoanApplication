package com.rbih.loanApplication.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EmiCalculatorTest {


    @Test
    void shouldCalculateEmiForStandardLoan() {
        BigDecimal emi = EmiCalculator.calculate(new BigDecimal("500000"), new BigDecimal("12.00"), 36);
        assertNotNull(emi);
        assertTrue(emi.compareTo(new BigDecimal("16000")) > 0);
        assertTrue(emi.compareTo(new BigDecimal("17000")) < 0);
        assertEquals(2, emi.scale());
    }

    @Test
    void shouldCalculateEmiForHighRateLoan() {
        BigDecimal emi = EmiCalculator.calculate(new BigDecimal("500000"), new BigDecimal("16.00"), 36);
        assertTrue(emi.compareTo(new BigDecimal("17000")) > 0);
    }

    @Test
    void shouldCalculateEmiForMinTenure() {
        BigDecimal emi = EmiCalculator.calculate(new BigDecimal("100000"), new BigDecimal("12.00"), 6);
        assertTrue(emi.compareTo(new BigDecimal("17000")) > 0);
    }

    @Test
    void shouldReturnScale2() {
        BigDecimal emi = EmiCalculator.calculate(new BigDecimal("333333"), new BigDecimal("13.50"), 24);
        assertEquals(2, emi.scale());
    }

}
