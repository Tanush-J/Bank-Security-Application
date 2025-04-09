package com.security.bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("Select a from Account a where a.accountNumber = ?1")
    public Optional<Account> findByAccountNumber(Long accountNumber);

    @Query("Select a from Account a where a.status = 'ACTIVE'")
    public List<Account> findAllActiveAccounts();

    @Query("Select a from Account a where a.status = 'INACTIVE'")
    public List<Account> findAllInActiveAccounts();
    
    public List<Account> findAllByAccountType(AccountType accountType);
    
    public List<Account> findAllByBranch(BranchType branchType);
}
