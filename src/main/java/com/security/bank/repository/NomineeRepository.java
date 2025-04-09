package com.security.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.bank.entity.Nominee;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {

}
