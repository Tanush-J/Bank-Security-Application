package com.security.bank.service;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.bank.dto.AccountDto;
import com.security.bank.dto.KycDto;
import com.security.bank.dto.NomineeDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;
import com.security.bank.entity.Card;
import com.security.bank.entity.CardType;
import com.security.bank.entity.Nominee;
import com.security.bank.entity.User;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.CardRepository;
import com.security.bank.repository.NomineeRepository;
import com.security.bank.repository.UserRepository;

@Service
public class AccountService {

    @Autowired
    UserService userService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    NomineeRepository nomineeRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    public void createAccount(AccountDto accountDto, Long userId) {
        User user = userService.getUserById(userId);
        Account account = new Account();
        account.setAccountNumber(Math.abs(new java.util.Random().nextLong()));
        account.setBalance(accountDto.getBalance());
        account.setProof(accountDto.getProof());
        account.setStatus("ACTIVE");
        account.setOpeningDate(new Date());
        account.setUser(user);

        Nominee nominee = new Nominee();
        nominee.setAccountNumber(accountDto.getNominee().getAccountNumber());
        nominee.setRelation(accountDto.getNominee().getRelation());
        nominee.setName(accountDto.getNominee().getName());
        nominee.setGender(accountDto.getNominee().getGender());
        nominee.setAge(accountDto.getNominee().getAge());
        nomineeRepository.save(nominee);
        account.setNominee(nominee);

        if (accountDto.getAccountType() == AccountType.PPF.toString()) {
            account.setAccountType(AccountType.PPF);
            account.setInterestRate((float)7.4);
            account.setBranch(BranchType.SBI);
        } else {
            Card card = new Card();
            long cardNumber = Math.abs(new java.util.Random().nextLong());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, 5);

            card.setCardNumber(cardNumber);
            card.setCvv((int)(Math.random() * 900) + 100);
            card.setAllocationDate(new Date());
            card.setPin(1122L);
            card.setStatus("ACTIVE");
            card.setExpiryDate(calendar.getTime());
            card.setCardHolderName(user.getName());
            if(accountDto.getAccountType() == AccountType.SAVINGS.toString()) {
                account.setAccountType(AccountType.SAVINGS);
                account.setInterestRate((float)2.70);
                account.setBranch(BranchType.BOB);
                card.setCardType(CardType.DEBIT_GLOBAL);
                card.setDailyLimit(40000);
            } else if (accountDto.getAccountType() == AccountType.CURRENT.toString()) {
                account.setAccountType(AccountType.CURRENT);
                account.setInterestRate((float)5.2);
                account.setBranch(BranchType.ICIC);
                card.setCardType(CardType.CREDIT_PREMIUM);
                card.setDailyLimit(50000);
            } else {
                account.setAccountType(AccountType.SALARY);
                account.setInterestRate((float)4.1);
                account.setBranch(BranchType.HDFC);
                card.setCardType(CardType.CREDIT_MASTER);
                card.setDailyLimit(75000);
            }
            cardRepository.save(card);
            account.setCard(card);
        }
        accountRepository.save(account);
        List<Account> accountList = new ArrayList<>(); 
        accountList.add(account);
        user.setAccountList(accountList);
        userRepository.save(user);
    }

    public List<Account> getAllAccountForUser(Long userId) {
        User user = userRepository.findById(userId).get();
        return user.getAccountList();
    }

    public Double getAccountBalance(Long accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given account number not found!");
        }
        return account.get().getBalance();
    }

    public Nominee getAccountNominee(Long accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given account number not found!");
        }
        return account.get().getNominee();
    }

    public void updateNominee(NomineeDto nomineeDto, Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given account number not found!");
        }
        Account account2 = account.get();
        Nominee nominee = account2.getNominee();
        nominee.setAccountNumber(nomineeDto.getAccountNumber());
        nominee.setAge(nomineeDto.getAge());
        nominee.setGender(nomineeDto.getGender());
        nominee.setName(nomineeDto.getName());
        nominee.setRelation(nomineeDto.getRelation());
        nomineeRepository.save(nominee);
        account2.setNominee(nominee);
        accountRepository.save(account2);
    }

    public User getKycDetails(Long accountNumber) {
        //set accountList and investmentList to null when returning user
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given account number not found!");
        }
        User user = account.get().getUser();
        user.setAccountList(null);
        user.setInvestmentList(null);
        return user;
    }

    public void updateKyc(KycDto kycDto, Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given account number not found!");
        }
        User user = account.get().getUser();
        if(!kycDto.getName().isEmpty()) user.setName(kycDto.getName());
        if(!kycDto.getIdentityProof().isEmpty()) user.setIdentityProof(kycDto.getIdentityProof());
        if(!kycDto.getAddress().isEmpty()) user.setAddress(kycDto.getAddress());
        if(kycDto.getNumber()!=null) user.setNumber(kycDto.getNumber());
        userRepository.save(user);
    }

    public Account getAccountSummary(Long accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given account number not found!");
        }
        Account account2 = account.get();
        account2.setUser(null);
        return account2;
    }
}
