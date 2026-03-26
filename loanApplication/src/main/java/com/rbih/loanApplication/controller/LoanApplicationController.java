package com.rbih.loanApplication.controller;

import com.rbih.loanApplication.dto.LoanApplicationRequest;
import com.rbih.loanApplication.dto.LoanApplicationResponse;
import com.rbih.loanApplication.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanApplicationService service;

    public LoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> create(@Valid @RequestBody LoanApplicationRequest request) {
        LoanApplicationResponse response = service.evaluate(request);
        return ResponseEntity.ok(response);
    }
}