package com.rbih.loanApplication.repository;

import com.rbih.loanApplication.entity.LoanApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanApplicationRepository extends JpaRepository<LoanApplicationEntity, UUID> {
}
