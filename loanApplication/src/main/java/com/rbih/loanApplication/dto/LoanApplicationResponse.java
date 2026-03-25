package com.rbih.loanApplication.dto;

import com.rbih.loanApplication.enums.ApplicationStatus;
import com.rbih.loanApplication.enums.RejectionReason;
import com.rbih.loanApplication.enums.RiskBand;

import java.util.List;
import java.util.UUID;

public record LoanApplicationResponse(
        UUID applicationId,
        ApplicationStatus status,
        RiskBand riskBand,
        OfferResponse offer,
        List<RejectionReason> rejectionReasons

) {
    public static LoanApplicationResponse approved(UUID id, RiskBand band, OfferResponse offer) {
        return new LoanApplicationResponse(id, ApplicationStatus.APPROVED, band, offer, null);
    }

    public static LoanApplicationResponse rejected(UUID id, List<RejectionReason> reasons) {
        return new LoanApplicationResponse(id, ApplicationStatus.REJECTED, null, null, reasons);
    }
}
