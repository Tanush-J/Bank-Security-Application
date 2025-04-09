package com.security.bank.accounts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.security.bank.dto.AccountDto;
import com.security.bank.dto.KycDto;
import com.security.bank.dto.NomineeDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.Nominee;
import com.security.bank.entity.User;
import com.security.bank.service.AccountService;

@RestController
@RequestMapping("/account")
public class UserAccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/create/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CUSTOMER')")
    public void createAccount(@RequestBody AccountDto accountDto, @PathVariable Long userId) {
        accountService.createAccount(accountDto, userId);
    }

    @GetMapping("/all/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Account> getAllAccountForUser(@PathVariable Long userId) {
        return accountService.getAllAccountForUser(userId);
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('CUSTOMER')")
    public Double getAccountBalance(@RequestParam Long accountNumber) {
        return accountService.getAccountBalance(accountNumber);
    }

    @GetMapping("/nominee")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('CUSTOMER')")
    public Nominee getAccountNominee(@RequestParam Long accountNumber) {
        return accountService.getAccountNominee(accountNumber);
    }

    @PutMapping("/updateNominee/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('CUSTOMER')")
    public void updateNominee(@RequestBody NomineeDto nomineeDto, @PathVariable Long accountId) {
        accountService.updateNominee(nomineeDto, accountId);
    }

    @GetMapping("/getKycDetails")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('CUSTOMER')")
    public User getKycDetails(@RequestParam Long accountNumber) {
        //set accountList and investmentList to null when returning user
        return accountService.getKycDetails(accountNumber);
    }

    @PutMapping("/updateKyc/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('CUSTOMER')")
    public void updateKyc(@RequestBody KycDto kycDto,@PathVariable Long accountId) {
        accountService.updateKyc(kycDto, accountId);
    }

    @GetMapping("/getAccount/summary")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('CUSTOMER')")
    public Account getAccountSummary(@RequestParam Long accountNumber) {
        return accountService.getAccountSummary(accountNumber);
    }
}
