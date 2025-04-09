package com.security.bank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.bank.dto.InvestmentDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.Investment;
import com.security.bank.entity.InvestmentType;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.InvestmentRepository;

@Service
public class InvestmentService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    InvestmentRepository investmentRepository;

    public String invest(Long accountId, InvestmentDto investmentDto) {
        Optional<Account> account = accountRepository.findById(accountId);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given id not found!");
        }
        Account account2 = account.get();
        if(account2.getBalance() < investmentDto.getAmount()) {
            throw new RuntimeException("Error in Investment");
        }
        Investment investment = new Investment();
        if(investmentDto.getInvestmentType() == InvestmentType.FIXED_DEPOSITS.toString()) {
            investment.setInvestmentType(InvestmentType.FIXED_DEPOSITS);
        } else if(investmentDto.getInvestmentType() == InvestmentType.GOLD.toString()) {
            investment.setInvestmentType(InvestmentType.GOLD);
        } else if(investmentDto.getInvestmentType() == InvestmentType.MUTUAL_FUND.toString()) {
            investment.setInvestmentType(InvestmentType.MUTUAL_FUND);
        } else if(investmentDto.getInvestmentType() == InvestmentType.STOCKS.toString()) {
            investment.setInvestmentType(InvestmentType.STOCKS);
        }     
        investment.setAmount(investmentDto.getAmount());
        investment.setDuration(investmentDto.getDuration());
        investment.setUser(account2.getUser());
        investmentRepository.save(investment);
        return "Investment successful";
    }
}
