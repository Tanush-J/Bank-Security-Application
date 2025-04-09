package com.security.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.bank.entity.Investment;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

}
