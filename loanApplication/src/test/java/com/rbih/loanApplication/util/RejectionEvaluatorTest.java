package com.rbih.loanApplication.util;

import com.rbih.loanApplication.enums.RejectionReason;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RejectionEvaluatorTest {


    @Test
    void shouldPassAllChecksForGoodApplicant() {
        List<RejectionReason> r = RejectionEvaluator.evaluate(720, 30, 36, new BigDecimal("500000"), new BigDecimal("75000"));
        assertTrue(r.isEmpty());
    }

    @Test
    void shouldRejectForLowCreditScore() {
        List<RejectionReason> r = RejectionEvaluator.evaluate(550, 30, 36, new BigDecimal("500000"), new BigDecimal("75000"));
        assertTrue(r.contains(RejectionReason.LOW_CREDIT_SCORE));
    }

    @Test
    void shouldRejectWhenAgePlusTenureExceeds65() {
        List<RejectionReason> r = RejectionEvaluator.evaluate(720, 55, 132, new BigDecimal("500000"), new BigDecimal("75000"));
        assertTrue(r.contains(RejectionReason.AGE_TENURE_LIMIT_EXCEEDED));
    }

    @Test
    void shouldRejectWhenEmiExceeds60Percent() {
        List<RejectionReason> r = RejectionEvaluator.evaluate(720, 30, 36, new BigDecimal("5000000"), new BigDecimal("50000"));
        assertTrue(r.contains(RejectionReason.EMI_EXCEEDS_50_PERCENT));
    }

    @Test
    void shouldReturnMultipleReasons() {
        List<RejectionReason> r = RejectionEvaluator.evaluate(500, 60, 120, new BigDecimal("5000000"), new BigDecimal("30000"));
        assertTrue(r.size() >= 2);
        assertTrue(r.contains(RejectionReason.LOW_CREDIT_SCORE));
        assertTrue(r.contains(RejectionReason.AGE_TENURE_LIMIT_EXCEEDED));
    }

    @Test
    void shouldPassAtBoundaryAgePlusTenure() {
        List<RejectionReason> r = RejectionEvaluator.evaluate(720, 53, 144, new BigDecimal("500000"), new BigDecimal("75000"));
        assertFalse(r.contains(RejectionReason.AGE_TENURE_LIMIT_EXCEEDED));
    }
}
